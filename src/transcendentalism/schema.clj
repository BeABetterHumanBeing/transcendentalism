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
  (is-abstract? [schema type] "Whether the given type is abstract")
  (get-supertypes [schema type] "Returns the set of supertypes of a given type"))

(defn create-schema
  [schema-data]
  (reify Schema
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
              (set/difference (set/union untested-types super-types) #{t}))))))))

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

; Code validation. The purpose of validation is to check the assumptions that
; are made by code generation.

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

(defn validate-graph-v1
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check schema graph)))
      #{}
      [events-occur-in-past? events-obey-causality? home-is-monad-rooted-dag?]),
     ; nil ends up in the set, and ought to be weeded out.
     ; TODO - weed out nil. (conj #{} nil) adds nil to the set.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))

(def schema-v1 (create-schema schema-data))
