(ns transcendentalism.generate
  (:require [clojure.java.io :as io]))
(use 'transcendentalism.graph)

(defn- gen-key
  "Generates a random alphanumeric key of given length"
  [len]
  (let [my-key (char-array len)]
    (dotimes [n len]
      (aset my-key n (.charAt "0123456789abcdefghijklmnopqrstuvwxyz" (rand-int 36))))
    (apply str (seq my-key))))

(defn- create-encodings
  "Produces a sub->key map of encodings for the nodes in a graph"
  [graph]
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

(defn- clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn generate-output
  "Convert a validated graph into the HTML, CSS, and JS files that compose the website"
  [graph]
  (let [encodings (create-encodings graph)]
    (clear-directory "output")
    (doseq
      [sub (filter #(has-type? graph % "/type/essay_segment") (all-nodes graph))]
      (let
        [filename (str "output/" (sub encodings) ".html")]
        (do
          (spit filename "<html>Test Output</html>")
          (println "Output" filename))))))