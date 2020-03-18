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
  (exists? [schema pred] "Whether the given predicate exists")
  (is-type? [schema pred] "Whether the given predicate is a type")
  (is-abstract? [schema type] "Whether the given type is abstract")
  (pred-required-by-type? [schema pred type]
    "Whether the given predicate is required by the given type")
  (required-preds [schema type] "Returns the required preds of the given type")
  (exclusive-preds [schema pred]
    "Returns the preds that are excluded by the existence of the given pred")
  (exclusive-properties [schema pred property]
    "Returns the properties that are excluded by the existence of the given property")
  (is-unique? [schema pred] "Whether the given predicate is unique")
  (get-supertypes [schema type] "Returns the set of supertypes of a given type")
  (get-domain-type [schema pred] "Returns the domain type, or nil")
  (get-range-type [schema pred] "Returns the range type, or nil")
  (get-properties [schema pred]
    "Returns the sub-schema of properties for the given pred")
  (get-preds-with-property [schema property]
    "Returns all preds that accept the given property"))

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
    (exclusive-preds [schema pred] (schema-data pred) :exclusive #{})
    (exclusive-properties [schema pred property]
      ((((schema-data pred) :properties {}) property {}) :exclusive #{}))
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
          nil)))
    (get-properties [schema pred]
      ((schema-data pred) :properties {}))
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

(defn- properties-all-valid?
  "Validates that all properties in the graph exist"
  [sub-schema triple]
  (reduce-kv
    (fn [result k v]
      (conj result
        (if (contains? sub-schema k)
          nil
          (str "Schema for pred " (:pred triple) " doesn't contain property " k))))
    #{}
    (:p-vs triple)))

(defn- preds-all-valid?
  "Validates that all triples in the graph exist"
  [schema graph]
  (reduce
    (fn [result triple]
      (set/union result
        (if (exists? schema (:pred triple))
          (if enable-property-validation
            (properties-all-valid? (get-properties schema (:pred triple)) triple)
            #{})
          #{(str "Schema doesn't contain pred \"" (:pred triple) "\"")})))
    #{}
    (all-triples graph)))

(defn- required-properties-exist?
  "Validates that all required properties exist"
  [schema graph]
  (if enable-property-validation
    (reduce
      (fn [result triple]
        (let [triple-properties (set (keys (:p-vs triple))),
              property-data (get-properties schema (:pred triple)),
              required-properties
                (reduce-kv
                  (fn [result k v]
                    (if (v :required false)
                      (conj result k)
                      result))
                  #{} property-data)]
          (if (set/subset? required-properties triple-properties)
            result
            (conj result
              (str "pred " (:pred triple) " requires " required-properties
                   " but only has " triple-properties)))))
      #{}
      (all-triples graph))
    #{}))

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
                  (has-type-v1? graph (:sub triple) domain-type))
            nil
            (str (:sub triple) " has pred " (:pred triple)
              " but is missing required domain type " domain-type)))))
    #{}
    (all-triples graph)))

(defn- range-type-matches
  [graph range-type obj]
  (or (nil? range-type)
      (and (= range-type :string)
           (string? obj))
      (and (= range-type :number)
           (number? obj))
      (and (= range-type :bool)
           (instance? Boolean obj))
      (and (= range-type :time)
           (is-valid-time obj))
      (and (string? range-type)
           (has-type-v1? graph
                         (if (vector? obj) (first obj) obj)
                         range-type))
      (and (vector? range-type)
           (not (nil? (some #(= obj %) range-type))))))

(defn- property-range-type-exists?
  [schema graph triple]
  (let [property-data (get-properties schema (:pred triple))]
    (reduce-kv
      (fn [result k v]
        (let [range-type ((property-data k) :range-type nil)]
          (if (range-type-matches graph range-type v)
            result
            (conj result
                  (str (:sub triple) "-" (:pred triple) " has property " k
                       ", but value " v " doesn't match range-type " range-type)))))
      #{} (:p-vs triple))))

(defn- range-type-exists?
  "Validates that the object has the required range type"
  [schema graph]
  (reduce
    (fn [result triple]
      (let [range-type (get-range-type schema (:pred triple))]
        (set/union result
          (if (range-type-matches graph range-type (:obj triple))
            (if enable-property-validation
              (property-range-type-exists? schema graph triple)
              #{})
            #{(str (:sub triple) " has pred " (:pred triple)
                   " but obj " (:obj triple) " doesn't match required range type "
                   range-type)}))))
    #{}
    (all-triples graph)))

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

(defn- no-exclusive-property-violations?
  "Validates that properties which have exclusivity requirements are met"
  [schema graph]
  (reduce
    (fn [result triple]
      (let [p-vs (:p-vs triple)]
        (reduce
          (fn [result property]
            (reduce
              (fn [result exclusive-property]
                (if (contains? p-vs exclusive-property)
                  (conj result
                        (str (:pred triple) "-" property " excludes "
                             exclusive-property ", but found anyway"))
                  result))
              result (exclusive-properties schema (:pred triple) property)))
          result (keys p-vs))))
    #{} (all-triples graph)))

(defn- no-exclusive-violations?
  "Validates that predicates which have exclusivity requirements are met"
  [schema graph]
  (reduce
    (fn [result triple]
      (reduce
        (fn [result exclusive-pred]
          (let [excluded (all-triples graph (:sub triple) exclusive-pred)]
            (if (empty? excluded)
              result
              (conj result
                    (str (:pred triple) " excludes " excluded " on "
                         (:sub triple))))))
        result (exclusive-preds schema (:pred triple))))
    #{} (all-triples graph)))

; TODO - optimize graph validation by splitting it across
; 1) relation-validations
; 2) node-validations
; 3) triple-validations
; 4) property-validations

(defn validate-graph
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check schema graph)))
      #{}
      [preds-all-valid? required-preds-exist? required-properties-exist?
       unique-preds-unique? required-supertypes-exist? domain-type-exists?
       range-type-exists? order-conforms? events-occur-in-past?
       events-obey-causality? home-is-monad-rooted-dag? no-abstract-subs?
       no-exclusive-violations? no-exclusive-property-violations?]),
     ; nil ends up in the set, and ought to be weeded out.
     ; TODO - weed out nil. (conj #{} nil) adds nil to the set.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))

(def schema (create-schema schema-data))
