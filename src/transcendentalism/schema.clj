(ns transcendentalism.schema
  (:require [clojure.string :as str]
    [clojure.set :as set]))

(use 'transcendentalism.graph
     'transcendentalism.schema-data
     'transcendentalism.time)

; The Schema protocal is the interface through which the schema data above is
; accessed.
(defprotocol Schema
  (exists? [schema pred] "Whether the given predicate exists")
  (is-type? [schema pred] "Whether the given predicate is a type")
  (is-abstract? [schema type] "Whether the given type is abstract")
  (pred-required-by-type? [schema pred type]
    "Whether the given predicate is required by the given type")
  (required-preds [schema type] "Returns the required preds of the given type")
  (is-unique? [schema pred] "Whether the given predicate is unique")
  (get-supertypes [schema type] "Returns the set of supertypes of a given type")
  (get-domain-type [schema pred] "Returns the domain type, or nil")
  (get-range-type [schema pred] "Returns the range type, or nil"))

(defn create-schema
  [schema-data]
  (reify Schema
    (exists? [schema pred] (contains? schema-data pred))
    (is-type? [schema pred] (str/starts-with? pred "/type"))
    (is-abstract? [schema type] ((schema-data type) :abstract false))
    (pred-required-by-type? [schema pred type]
      (let [pred-data (schema-data pred)]
        (and (= (:domain-type pred-data) type)
          (contains? pred-data :required)
          (pred-data :required))))
    (required-preds [schema type]
      (reduce
        (fn [result pred]
          (if (pred-required-by-type? schema pred type)
            (conj result pred)
            result))
        #{}
        (keys schema-data)))
    (is-unique? [schema pred]
      (let [pred-data (schema-data pred)]
        (and (contains? pred-data :unique)
          (pred-data :unique))))
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
    (get-domain-type [schema pred]
      (let [pred-data (schema-data pred)]
        (if (contains? pred-data :domain-type)
          (pred-data :domain-type)
          nil)))
    (get-range-type [schema pred]
      (let [pred-data (schema-data pred)]
        (if (contains? pred-data :range-type)
          (pred-data :range-type)
          nil)))))

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
   (q-chain
     (q-kleene
       (q-or (q-pred "/segment/flow/inline")
             (q-pred "/segment/flow/block")))
     (q-pred "/segment/contains")))

(def gq-item-to-item
  "Returns a graph query that expands from /type/item to other /type/items that
   are nested within them"
  (q-kleene
     (q-chain
       (q-or (q-pred "/item/q_and_a/question")
             (q-pred "/item/q_and_a/answer")
             (q-pred "/item/bullet_list/point"))
       (q-kleene
         ; Assumes questions, answers, and points are single-blocked.
         (q-pred "/segment/flow/inline"))
       (q-pred "/segment/contains"))))

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

(defn- preds-all-valid?
  "Validates that all triples in the graph exist"
  [schema graph]
  (reduce
    (fn [result triple]
      (conj result
        (if (exists? schema (:pred triple))
          nil
          (str "Schema doesn't contain pred \"" (:pred triple) "\""))))
    #{}
    (all-triples graph)))

(defn- required-preds-exist?
  "Validates that all required predicates exist"
  [schema graph]
  (reduce
    (fn [result sub]
      (conj result
        (let
          [triple-preds (set (map :pred (all-triples graph sub))),
           types (filter #(is-type? schema %) triple-preds),
           required-preds (apply set/union (map #(required-preds schema %) types))]
          (if (set/subset? required-preds triple-preds)
            nil
            (str sub " requires " required-preds " but only has " triple-preds)))))
    #{}
    (all-nodes graph)))

(defn- unique-preds-unique?
  "Validates that unique predicates are not duplicated"
  [schema graph]
  (reduce
    (fn [result sub]
      (set/union result
        (let [pred-count (pred-counts (map :pred (all-triples graph sub)))]
          (reduce
            (fn [result pred-cnt]
              (conj result
                (if
                  (and
                    (is-unique? schema (first pred-cnt))
                    (not (= (second pred-cnt) 1)))
                  (str sub " has " (second pred-cnt) " " (first pred-cnt) ", but can only have 1")
                  nil)))
            #{}
            (seq pred-count)))))
    #{}
    (all-nodes graph)))

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

(defn- domain-type-exists?
  "Validates that the subject has the required domain type"
  [schema graph]
  (reduce
    (fn [result triple]
      (let [domain-type (get-domain-type schema (:pred triple))]
        (conj result
          (if (or (nil? domain-type)
                  (has-type? graph (:sub triple) domain-type))
            nil
            (str (:sub triple) " has pred " (:pred triple)
              " but is missing required domain type " domain-type)))))
    #{}
    (all-triples graph)))

(defn- range-type-exists?
  "Validates that the object has the required range type"
  [schema graph]
  (reduce
    (fn [result triple]
      (let [range-type (get-range-type schema (:pred triple))]
        (conj result
          (if (or (nil? range-type)
                  (and (= range-type :string)
                       (or (string? (:obj triple))
                           (and (vector? (:obj triple))
                                (string? (first (:obj triple))))))
                  (and (= range-type :time)
                       (is-valid-time (:obj triple)))
                  (and (string? range-type)
                       (has-type? graph
                        (let [obj (:obj triple)]
                          (if (vector? obj) (first obj) obj))
                        range-type))
                  (and (vector? range-type)
                       (not (nil? (some #(= (:obj triple) %) range-type)))))
            nil
            (str (:sub triple) " has pred " (:pred triple)
              " but obj " (:obj triple) " doesn't match required range type "
              range-type)))))
    #{}
    (all-triples graph)))

(defn- order-conforms?
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
      (let [types (get-types (get-node graph sub)),
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

; TODO(gierl) Add exclusive validation
(defn validate-graph
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check schema graph)))
      #{}
      [preds-all-valid? required-preds-exist? unique-preds-unique?
       required-supertypes-exist? domain-type-exists? range-type-exists?
       #(order-conforms? %1 %2 "/item/contains") events-occur-in-past?
       #(order-conforms? %1 %2 "/item/text/text") events-obey-causality?
       home-is-monad-rooted-dag? #(order-conforms? %1 %2 "/item/poem/line")
       no-abstract-subs?]),
     ; nil ends up in the set, and ought to be weeded out.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))

(def schema (create-schema schema-data))
