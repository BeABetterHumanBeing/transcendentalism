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

(defn- html
  [contents]
  (str "<html>" contents "</html>"))

(defn- ul
  [contents]
  (str "<ul>" contents "</ul>"))

(defn- li
  [contents]
  (apply str
    (map (fn [content] (str "<li>" content "</li>"))
      contents)))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a given essay segment"
  [graph encodings sub]
  (html (ul (li (map print-triple (all-triples graph sub))))))

(defn generate-output
  "Convert a validated graph into the HTML, CSS, and JS files that compose the website"
  [graph]
  (let [encodings (create-encodings graph)]
    (clear-directory "output")
    (doseq
      [sub (all-nodes graph "/type/essay_segment")]
      (let
        [filename (str "output/" (sub encodings) ".html")]
        (do
          (spit filename (generate-essay-segment graph encodings sub))
          (println "Generated" filename))))))