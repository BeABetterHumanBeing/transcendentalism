(ns transcendentalism.core
  (:require [clojure.java.io :as io]))
(use 'transcendentalism.graph)

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  [(->Triple :monad "/type/essay_segment" nil)
   (->Triple :monad "/essay/contains" :monad-contents)
   (->Triple :monad "/essay/title" "Transcendental Metaphysics")
   (->Triple :monad-contents "/type/item/ordered_set" nil)
   (->Triple :monad-contents "/item/order" :CHRONO)
   (->Triple :monad-contents "/item/contains" :monad-image)
   (->Triple :monad-contents "/item/contains" :monad-intro-text)
   (->Triple :monad-image "/type/item/image" nil)
   (->Triple :monad-image "/item/image/url" "todo_monad_image_url.svg")
   (->Triple :monad-intro-text "/type/item/text" nil)
   (->Triple :monad-intro-text "/item/text/text" (clojure.string/join " "
    ["\"The Monad is the symbol of unity."
     "It is the godhead, the point from which all things originate,"
     "and the point to which all things return.\"\n"
     "-Daniel Gierl"]))
   ; TODO - Add /essay/flow/next to the full introduction.
   ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
   ])

(def graph (construct-graph (concat monad)))

; Code for encoding sub values as random alphanumeric keys.
(defn gen-key
  [len]
  (let [my-key (char-array len)]
    (dotimes [n len]
      (aset my-key n (.charAt "0123456789abcdefghijklmnopqrstuvwxyz" (rand-int 36))))
    (apply str (seq my-key))))

(def encodings
  (reduce
    (fn [codings sub]
      (assoc
        codings
        sub
        (if (= sub :monad)
          "index"
          (gen-key 8))))
    {}
    (all-nodes graph)))

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

; Construct the schema.
(def schema
  (reify Schema
    (exists? [schema pred]
      (let [result (contains? schema-data pred)]
        (if (not result)
          (println (str "Schema doesn't contain pred \"" pred "\"")))
        result))))

; Code validation. The purpose of validation is to check the assumptions that
; are made by code generation.
(defn preds-all-valid?
  "Validates that all triples in the graph exist"
  [schema graph]
  (reduce
    (fn [all-exist triple]
      (and all-exist (exists? schema (:pred triple))))
    true
    (all-triples graph)))

(defn validate-graph
  "Validates that a given graph conforms to a given schema."
  [schema graph]
  (reduce
    (fn [valid validation-check]
      (and valid (validation-check schema graph)))
    true
    [preds-all-valid?]))

; Code generation.
(defn clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn generate-output
  "Convert a validated graph into the HTML, CSS, and JS files that compose the website"
  [graph]
  (do
    (clear-directory "output")
    (doseq
      [sub (filter #(has-type? graph % "/type/essay_segment") (all-nodes graph))]
      (let
        [filename (str "output/" (sub encodings) ".html")]
        (do
          (spit filename "<html>Test Output</html>")
          (println "Output" filename))))))

(defn -main
  "I don't do a whole lot."
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
