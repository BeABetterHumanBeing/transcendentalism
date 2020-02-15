(ns transcendentalism.schema
  (:require [clojure.string :as str]
    [clojure.set :as set]))
(use 'transcendentalism.graph)

; The schema determines what predicates are allowed in the graph, as well as all
; constraints that bound the triples to which those predicates belong.
(def extra-schema {
  "/type/item" {
    :description "A piece of content",
  },
  "/type/item/text" {
    :description "Textual content",
    :super-type "/type/item",
  },
  "/type/item/big_emoji" {
    :description "A series of big emoji",
    :super-type "/type/item",
  },
  "/type/item/ordered_set" {
    :description "An ordered collection of items",
    :super-type "/type/item",
  },
  "/item/contains" {
    :description "Relation to a child node of an ordered set",
    :domain-type "/type/item/ordered_set",
    :range-type "/type/item",
  },
  "/item/big_emoji/emoji" {
    :description "The sequence of emoji to render",
    :domain-type "/type/item/big_emoji",
    :range-type :string,
    :required true,
    :unique true,
  },
  "/item/label" {
    :description "Symbol label that ascribes a metadata to the item",
    :domain-type "/type/item",
    :range-type [
      ; Content is tangential to the main flow.
      :note]
  },
  "/item/footnote" {
    :description "Relation to a piece of footnote content",
    :domain-type "/type/item",
    :range-type "/type/item",
  },
  "/item/internal_link" {
    :description "Relation to another essay segment",
    :domain-type "/type/item",
    :range-type "/type/essay",
  },
  "/item/text/text" {
    :description "The contents of a text item",
    :domain-type "/type/item/text",
    :range-type :string,
    :required true,
  },
  "/item/text/url" {
    :description "External URL to which a piece of text is linked",
    :domain-type "/type/item/text",
    :range-type :string,
  },
})

(defn- schematize-type
  "Expands a partial schema of a given type"
  [type type-schema schema]
  (let [full-type (str "/type" type)]
    (assoc
      (reduce-kv
        (fn [result k v]
          (assoc result
            (str type k)
            (assoc v
              :domain-type
              full-type
              :range-type
              (if (contains? v :range-type) (:range-type v) full-type))))
        {} schema)
      full-type type-schema)))

(def event-schema
  (schematize-type "/event"
    {
      :description "An event",
    }
    {
      "/leads_to" {
        :description "Relation from one event to its subsequent impacts",
      },
      "/time" {
        :description "When an event happened",
        :range-type :time,
        :required true,
        :unique true,
      },
    }))

(def essay-schema
  (schematize-type "/essay"
    {
      :description "Nodes that are externally link-able",
    }
    {
      "/title" {
        :description "The text that appears centered at the top of an essay segment",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/flow/next" {
        :description "Relation to the next essay segment",
      },
      "/flow/home" {
        :description "Relation to the monad",
        :required true,
        :unique true,
      },
      "/flow/see_also" {
        :description "Internal link to another essay segment",
      },
      "/contains" {
        :description "Relation from an essay segment to the item it contains",
        :range-type "/type/item",
        :unique true,
        :required true,
      },
      "/label" {
        :description "Symbol label that ascribes a metadata to the essay segment",
        :range-type [
          ; Content is about surrounding content.
          :meta
          ; Content is under construction.
          :under-construction
          ; Content is religious.
          :religion
          ; Content is political.
          :politics]
      },
    }))

(def image-schema
  (schematize-type "/item/image"
    {
      :description "Image content",
      :super-type "/type/item",
    }
    {
      "/url" {
        :description "URL of image",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/alt_text" {
        :description "Alt text of image",
        :range-type :string,
        :required true,
        :unique true,
      },
    }))

(def quote-schema
  (schematize-type "/item/quote"
    {
      :description "A quote",
      :super-type "/type/item",
    }
    {
      "/text" {
        :description "The text contents of the quote",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/author" {
        :description "To whom the quote is attributed",
        :range-type :string,
        :unique true,
      },
    }))

(def schema-data
  (merge extra-schema essay-schema event-schema image-schema quote-schema))

(defn- type-to-supertypes
  "Returns a set of all transitive supertypes of a given type"
  [type]
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

; The Schema protocal is the interface through which the schema data above is
; accessed.
(defprotocol Schema
  (exists? [schema pred] "Whether the given predicate exists")
  (is-type? [schema pred] "Whether the given predicate is a type")
  (pred-required-by-type? [schema pred type]
    "Whether the given predicate is required by the given type")
  (required-preds [schema type] "Returns the required preds of the given type")
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
    (get-supertypes [schema type] (type-to-supertypes type))
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
  [sub & type-suffixes]
  (let [full-types (map #(str "/type" %) type-suffixes),
        inferred-types (apply set/union (map #(type-to-supertypes %) full-types)),
        all-types (set/union inferred-types full-types)]
    (map #(->Triple sub % nil) all-types)))

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

(defn- ordered-sets-have-order?
  "Validates that all /item/contains triples have an order"
  [schema graph]
  (let [relation (get-relation graph "/item/contains")]
    (reduce
      (fn [result sub]
        (let [metadata (map meta (all-objs relation sub)),
              metadata-errors
                (reduce
                  (fn [result m-data]
                    (if (contains? m-data :order)
                      result
                      (conj result (str sub " is missing :order metadata"))))
                  #{} metadata)]
          (if (empty? metadata-errors)
            (let [ordinals (map :order metadata),
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
                (set/union result ordinal-errors)))
            (set/union result metadata-errors))))
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
       ordered-sets-have-order? events-obey-causality? events-occur-in-past?
       home-is-monad-rooted-dag?]),
     ; nil ends up in the set, and ought to be weeded out.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))

(def schema (create-schema))
