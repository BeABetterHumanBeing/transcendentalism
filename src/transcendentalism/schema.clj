(ns transcendentalism.schema
  (:require [clojure.string :as str]
    [clojure.set :as set]))
(use 'transcendentalism.graph)

; The schema determines what predicates are allowed in the graph, as well as all
; constraints that bound the triples to which those predicates belong.
; TODO(gierl): Pull out the symmetrically-typed relations into their omn
; 'specializations' under that type. It acts as a 'class' of knowledge for that
; type, which the main body of schema-data inter-operates with.
(def schema-data {
  "/type/essay_segment" {
    :description "Nodes that are externally link-able",
  },
  "/type/item" {
    :description "A piece of content",
  },
  "/type/item/text" {
    :description "Textual content",
    :super-type "/type/item",
  },
  "/type/item/image" {
    :description "Image content",
    :super-type "/type/item",
  },
  "/type/item/ordered_set" {
    :description "An ordered collection of items",
    :super-type "/type/item",
  },
  "/essay/title" {
    :description "The text that appears centered at the top of an essay segment",
    :domain-type "/type/essay_segment",
    :range-type :string
  },
  "/essay/flow/next" {
    :description "Relation to the next essay segment",
    :domain-type "/type/essay_segment",
    :range-type "/type/essay_segment",
  },
  "/essay/flow/home" {
    :description "Relation to the monad",
    :domain-type "/type/essay_segment",
    :range-type "/type/essay_segment",
    :unique true,
  },
  "/essay/flow/see_also" {
    :description "Internal link to another essay segment",
    :domain-type "/type/essay_segment",
    :range-type "/type/essay_segment",
  },
  "/essay/contains" {
    :description "Relation from an essay segment to the item it contains",
    :domain-type "/type/essay_segment",
    :range-type "/type/item",
    :unique true,
    :required true,
  },
  "/essay/label" {
    :description "Symbol label that ascribes a metadata to the essay segment",
    :domain-type "/type/essay_segment",
    :range-type [
      ; Content is about surrounding content.
      :META
      ; Content is religious.
      :RELIGION
      ; Content is political.
      :POLITICS]
  },
  "/item/contains" {
    :description "Relation to a child node of an ordered set",
    :domain-type "/type/item/ordered_set",
    :range-type "/type/item",
  },
  "/item/label" {
    :description "Symbol label that ascribes a metadata to the item",
    :domain-type "/type/item",
    :range-type [
      ; Content is tangential to the main flow.
      :NOTE]
  },
  "/item/footnote" {
    :description "Relation to a piece of footnote content",
    :domain-type "/type/item",
    :range-type "/type/item",
  },
  "/item/internal_link" {
    :description "Relation to another essay segment",
    :domain-type "/type/item",
    :range-type "/type/essay_segment",
  },
  "/item/text/text" {
    :description "The contents of a text item",
    :domain-type "/type/item/text",
    :range-type :string,
  },
  "/item/text/url" {
    :description "External URL to which a piece of text is linked",
    :domain-type "/type/item/text",
    :range-type :string,
  },
  "/item/order" {
    :description "Type of order of a set",
    :domain-type "/type/item/ordered_set",
    :range-type [
      ; Order is chronological.
      :CHRONO
      ; Order is random.
      :RANDOM],
    :unique true,
    :required true,
  },
  "/item/image/url" {
    :description "URL of image",
    :domain-type "/type/item/image",
    :range-type :string,
  },
})

; The Schema protocal is the interface through which the schema data above is
; accessed.
(defprotocol Schema
  (exists? [schema pred] "Whether the given predicate exists")
  (is-type? [schema pred] "Whether the given predicate is a type")
  (pred-required-by-type? [schema pred type]
    "Whether the given predicate is required by the given type")
  (is-unique? [schema pred] "Whether the given predicate is unique")
  (get-supertypes [schema type] "Returns the set of supertypes of a given type")
  (get-domain-type [schema pred] "Returns the domain type, or nil")
  (get-range-type [schema pred] "Returns the range type, or nil"))

(defn create-schema
  []
  (reify Schema
    (exists? [schema pred] (contains? schema-data pred))
    (is-type? [schema pred] (str/starts-with? pred "/type"))
    (pred-required-by-type? [schema pred type]
      (let [pred-data (schema-data pred)]
        (and (= (:domain-type pred-data) type)
          (contains? pred-data :required)
          (pred-data :required))))
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

(defn- required-preds
  "Returns the predicates that are required by a given type"
  [schema type]
  (reduce
    (fn [result pred]
      (if (pred-required-by-type? schema pred type)
        (conj result pred)
        result))
    #{}
    (keys schema-data)))

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
                       (string? (:obj triple)))
                  (and (string? range-type)
                       (has-type? graph (:obj triple) range-type))
                  (and (vector? range-type)
                       (not (nil? (some #(= (:obj triple) %) range-type)))))
            nil
            (str (:sub triple) " has pred " (:pred triple)
              " but obj " (:obj triple) " doesn't match required range type "
              range-type)))))
    #{}
    (all-triples graph)))

(defn validate-graph
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check schema graph)))
      #{}
      [preds-all-valid? required-preds-exist? unique-preds-unique?
       required-supertypes-exist? domain-type-exists? range-type-exists?]),
     ; nil ends up in the set, and ought to be weeded out.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))
