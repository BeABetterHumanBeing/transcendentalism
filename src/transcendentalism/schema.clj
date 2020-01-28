(ns transcendentalism.schema)

; The schema determines what predicates are allowed in the graph, as well as all
; constraints that bound the triples to which those predicates belong.
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
      :RANDOM]
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
  (exists? [schema pred] "Whether the given predicate exists"))

(defn create-schema
  []
  (reify Schema
    (exists? [schema pred]
      (let [result (contains? schema-data pred)]
        (if (not result)
          (println (str "Schema doesn't contain pred \"" pred "\"")))
        result))))