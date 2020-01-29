(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
    [clojure.string :as str]))
(use 'transcendentalism.graph)

; Whether to generate debugging HTML or not.
(def debug true)

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

(defn- attr-aware
  "Adds attributes to a given opening tag."
  [tagname attrs]
  (if (empty? attrs)
    (str "<" tagname ">")
    (let [formatted-attrs (map #(str (first %) "=\"" (second %) "\"") (seq attrs))]
      (str "<" tagname " " (str/join " " formatted-attrs) ">"))))

(defn- html
  [& contents]
  (str "<html>" (apply str contents) "</html>"))

(defn- head
  [contents]
  (str "<head>" contents "</head>"))

(defn- link
  [rel href]
  (str "<link rel=\"" rel "\" href=\"" href "\">"))

(defn- body
  [& contents]
  (str "<body>" (apply str contents) "</body>"))

(defn- div
  [attrs & contents]
  (str (attr-aware "div" attrs) (apply str contents) "</div>"))

(defn- p
  [contents]
  (str "<p>" contents "</p>"))

(defn- ul
  [contents]
  (str "<ul>" contents "</ul>"))

(defn- li
  [contents]
  (apply str
    (map (fn [content] (str "<li>" content "</li>"))
      contents)))

(defn- stylesheet
  []
  (str "div.debug {"
    "font-family: Monaco, monospace;"
    "border-style: dashed;"
    "border-width: 1px;"
    "border-color: red;"
    "padding: 5px 10px 5px;"
    (if debug "" "visibility:hidden")
    "}"))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a given essay segment"
  [graph encodings sub]
  (html
    (head (link "stylesheet" "styles.css"))
    (body
      (if debug
        (div {"class" "debug"} (p "Triples") (ul (li (map print-triple (all-triples graph sub)))))
        ""))))

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
          (println "Generated" filename))))
    (spit "output/styles.css" (stylesheet))))