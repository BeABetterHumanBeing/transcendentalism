(ns transcendentalism.schema
  (:require [clojure.string :as str]
    [clojure.set :as set]))

(use 'transcendentalism.graph
     'transcendentalism.schema-data
     'transcendentalism.time)

; Whether or not to do property validation on the graph.
; TODO - move this to flags.
(def enable-property-validation true)

; The Schema protocal is the interface through which the schema data above is
; accessed.
(defprotocol Schema
  (is-type? [schema pred] "Whether the given predicate is a type")
  (is-abstract? [schema type] "Whether the given type is abstract")
  (get-supertypes [schema type] "Returns the set of supertypes of a given type")
  (get-preds-with-property [schema property]
    "Returns all preds that accept the given property"))

(defn create-schema
  [schema-data]
  (reify Schema
    (is-type? [schema pred] (str/starts-with? pred "/type"))
    (is-abstract? [schema type] ((schema-data type) :abstract false))
    (get-supertypes [schema type]
      (loop [supertypes #{},
             untested-types #{type}]
        (if (empty? untested-types)
          supertypes
          (let [t (first untested-types),
                pred-data (schema-data t),
                super-types (if (contains? pred-data :super-type)
                  (if (set? (pred-data :super-type))
                    (pred-data :super-type)
                    #{(pred-data :super-type)})
                  #{})]
            ; Note that this logic will recur indefinitely if the supertype
            ; graph has cycles.
            (recur
              (set/union supertypes super-types)
              (set/difference (set/union untested-types super-types) #{t}))))))
    (get-preds-with-property [schema property]
      (reduce-kv
        (fn [result k v]
          (if (contains? (v :properties {}) property)
            (conj result k)
            result))
        #{} schema-data))))

(defn types
  "Given a sub and list of types (without \"type\" prefix), returns the corresponding type triples."
  [schema sub & type-suffixes]
  (let [full-types (map #(str "/type" %) type-suffixes),
        inferred-types (apply set/union (map #(get-supertypes schema %) full-types)),
        all-types (set/union inferred-types full-types)]
    (map #(->Triple sub % nil {}) all-types)))

(def gq-segment-to-item
  "Returns a graph query that expands from /type/segment to all /type/item that
   it directly contains through its flows"
   (q-chain-v1
     (q-kleene
       (q-or-v1 (q-pred-v1 "/segment/flow/inline")
                (q-pred-v1 "/segment/flow/block")))
     (q-pred-v1 "/segment/contains")))

(def gq-item-to-item
  "Returns a graph query that expands from /type/item to other /type/items that
   are nested within them"
  (q-kleene
     (q-chain-v1
       (q-or-v1 (q-pred-v1 "/item/q_and_a/question")
                (q-pred-v1 "/item/q_and_a/answer")
                (q-pred-v1 "/item/bullet_list/header")
                (q-pred-v1 "/item/bullet_list/point"))
       (q-kleene
         ; Assumes questions, answers, and points are single-blocked.
         (q-pred-v1 "/segment/flow/inline"))
       (q-pred-v1 "/segment/contains"))))

(defn- pred-counts
  "Returns a map associating predicates with the number of times they appear"
  [preds]
  (reduce
    (fn [result pred]
      (assoc result pred (if (contains? result pred) (inc (result pred)) 1)))
    {}
    preds))

; Code validation. The purpose of validation is to check the assumptions that
; are made by code generation.

(defn- required-supertypes-exist?
  "Validates that required supertypes exist"
  [schema graph]
  (reduce
    (fn [result sub]
      (conj result
        (let [types (set (filter #(is-type? schema %)
                                 (map :pred (all-triples graph sub)))),
              supertypes (reduce
                (fn [result type]
                  (set/union result (get-supertypes schema type)))
                #{}
                types)]
          (if (set/subset? supertypes types)
            nil
            (str sub " has types " types " which require supertypes " supertypes)))))
    #{}
    (all-nodes graph)))

(defn- order-conforms-pred?
  "Validates that all preds that have order are correctly ordered"
  [schema graph pred]
  (let [relation (get-relation graph pred)]
    (reduce
      (fn [result sub]
        (let [ordinals (map #(property % "/order" 0) (all-triples graph sub pred)),
              ordinal-errors
                (reduce
                  (fn [result ordinal]
                    (if (number? ordinal)
                      result
                      (conj result
                        (str sub " has ordinal " ordinal ", but it's not a number"))))
                  #{} ordinals)]
          (if (empty? ordinal-errors)
            (if (or (empty? ordinals) (apply distinct? ordinals))
              result
              (conj result (str sub " has non-distict ordinals: " ordinals)))
            (set/union result ordinal-errors))))
      #{} (participant-nodes relation))))

(defn- order-conforms?
  "Checks that all preds that have property /order are correctly ordered"
  [schema graph]
  (reduce
    (fn [result pred]
      (set/union result (order-conforms-pred? schema graph pred)))
    #{} (get-preds-with-property schema "/order")))

(defn- events-obey-causality?
  "Validates that events' timestamps are strickly before their leads_to"
  [schema graph]
  (reduce
    (fn [result triple]
      (let [sub-time (get-time graph (:sub triple)),
            obj-time (get-time graph (:obj triple))]
        (conj result
          (if (before? sub-time obj-time)
            nil
            (str (:sub triple) " leads_to " (:obj triple)
                 ", but doesn't occur before it")))))
    #{}
    (all-triples graph "/event/leads_to")))

(defn- events-occur-in-past?
  "Validates that /event/leads_to goes from past to present"
  [schema graph]
  (let [relation (get-relation graph "/event/leads_to")]
    (set/union
      ; Check that the sources are in the past.
      (reduce
        (fn [result sub]
          (conj result
            (if (= (at (get-time graph sub)) "past")
              nil
              (str sub " has no events leading to it, but does not occur at 'past'"))))
        #{} (get-sources relation))
      ; Check that the sinks are in the present.
      (reduce
        (fn [result sub]
          (conj result
            (if (= (at (get-time graph sub)) "present")
              nil
              (str sub " leads to no event, but does not occur at 'present'"))))
        #{} (get-sinks relation)))))

(defn- home-is-monad-rooted-dag?
  "Validates that /essay/flow/home results in a monad-rooted DAG"
  [schema graph]
  (let [relation (get-relation graph "/essay/flow/home"),
        sinks (get-sinks relation)]
    (reduce
      (fn [result sub]
        (conj result
          (if (= sub :monad)
            nil
            (str sub "'s /essay/flow/home does not lead to :monad"))))
      #{} sinks)))

(defn- no-abstract-subs?
  "Validates that subs with abstract types have a non-abstract sub-type"
  [schema graph]
  (reduce
    (fn [result sub]
      (let [types (get-types-v1 (get-node-v1 graph sub)),
            abstract-types (filter #(is-abstract? schema %) types)]
        (reduce
          (fn [result abstract-type]
            (if (nil? (some #(contains? (get-supertypes schema %) abstract-type)
                            types))
              (conj result
                (str sub " has abstract type " abstract-type
                     " but no concrete sub-type"))
              result))
          result abstract-types)))
    #{} (all-nodes graph)))

(defn validate-graph
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check schema graph)))
      #{}
      [required-supertypes-exist? order-conforms? events-occur-in-past?
       events-obey-causality? home-is-monad-rooted-dag? no-abstract-subs?]),
     ; nil ends up in the set, and ought to be weeded out.
     ; TODO - weed out nil. (conj #{} nil) adds nil to the set.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))

(def schema-v1 (create-schema schema-data))
