(ns transcendentalism.core
  (:require [clojure.java.io :as io]))
(use
  'transcendentalism.graph
  'transcendentalism.schema)

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

(def schema (create-schema))

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
