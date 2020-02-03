(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
    [clojure.string :as str]))
(use 'transcendentalism.graph)

; Whether to generate debugging HTML or not.
(def debugging-mode true)

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

(defn- debug
  "Passes through content if in debugging-mode, otherwise nullifies it"
  [content]
  (if debugging-mode content ""))

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

(defn- hr [] (str "<hr>"))

(defn- h1
  [attrs contents]
  (str (attr-aware "h1" attrs) contents "</h1>"))

(defn- img [attrs] (attr-aware "img" attrs))

(defn- css
  [tagname attrs & contents]
  (let [selector
        (if (contains? attrs "class")
          (str "." (attrs "class"))
          (if (contains? attrs "id")
            (str "#" (attrs "id"))
            ""))]
    (str/join "\n" [(str tagname selector) " {" (str/join "\n" contents) "}"])))

(defn- font-family
  [& contents]
  (str "font-family: " (str/join ", " contents) ";"))

(defn- border-style
  [contents]
  (str "border-style: " contents ";"))

(defn- border-width
  [contents]
  (str "border-width: " contents ";"))

(defn- border-color
  [contents]
  (str "border-color: " contents ";"))

(defn- padding
  [& contents]
  (str "padding: " (str/join " " contents) ";"))

(defn- display
  [contents]
  (str "display: " contents ";"))

(defn- text-align
  [contents]
  (str "text-align: " contents ";"))

(defn- grid-template-columns
  [& contents]
  (str "grid-template-columns: " (str/join " " contents) ";"))

(defn- media
  [condition & contents]
  (str/join "\n"
    [(str "@media (" condition ") {") (str/join "\n" contents) "}"]))

(defn- stylesheet
  []
  (str/join "\n"
    [(debug (css "div" {"class" "debug"}
      (font-family "Monaco" "monospace")
      (border-style "dashed")
      (border-width "1px")
      (border-color "red")
      (padding "5px" "10px" "5px")))
    (media "min-width: 1000px"
      (css "div" {"class" "segment"}
        (display "grid")
        (grid-template-columns "auto" "800px" "auto")))
    (media "max-width: 1000px"
      (css "div" {"class" "segment"}
        (display "grid")
        (grid-template-columns "100px" "auto" "100px")))
    (css "h1" {"class" "header"}
      (text-align "center"))
    (css "div" {"class" "content"}
      (border-style "dashed")
      (border-width "1px"))
    (css "div" {"class" "footer"} "")]))

(defn- generate-item-text
  "Returns the HTML corresponding to a /type/item/text"
  [triples]
  (div {"class" "content"}
    ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
    ; TODO(gierl): Handle /item/text/url
    "TODO text"))

(defn- generate-item-image
  "Returns the HTML corresponding to a /type/item/image"
  [triples]
  (div {"class" "content"}
    (let [image-url-triple (first (filter #(= (:pred %) "/item/image/url") triples))]
      ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
      ; TODO(gierl): Add alt-text.
      (img {"src" (:obj image-url-triple)}))))

(defn- generate-item
  "Returns the HTML corresponding to a /type/item"
  [graph sub]
  ; The call for generating ordered set items must be included here so that it
  ; can recursively call its parent function.
  (letfn [(generate-item-ordered-set [triples]
            (div {"class" "content"}
              ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
              (let [contents (sort #(< (:order (meta (:obj %1)))
                                       (:order (meta (:obj %2))))
                                   (filter #(= (:pred %) "/item/contains") triples))]
                (apply str (map #(generate-item graph (first (:obj %))) contents)))))]
    (let [triples (all-triples graph sub),
          item-type (filter #(and (str/starts-with? (:pred %) "/type")
                                  (not (= (:pred %) "/type/item"))) triples)]
      ({"/type/item/text" (generate-item-text triples),
        "/type/item/image" (generate-item-image triples),
        "/type/item/ordered_set" (generate-item-ordered-set triples)}
        (:pred (first item-type))))))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a /type/essay_segment"
  [graph encodings sub]
  (html
    (head (link "stylesheet" "styles.css"))
    (body
      (debug
        ; Debugging information appears up top.
        (div {"class" "debug"}
          (p "Triples")
          (ul (li (map print-triple (all-triples graph sub))))))
      ; The contents of the segment appear within a single div.
      (div {"class" "segment"}
        (div "")
        (div {}
          (let [title-triple (first (all-triples graph sub "/essay/title"))]
            (if (empty? title-triple)
              ""
              (h1 {"class" "header"} (:obj title-triple))))
          (hr)
          (let [content-sub (:obj (first (all-triples graph sub "/essay/contains")))]
            (generate-item graph content-sub))
          (hr)
          (div {"class" "footer"} "TODO - Relations go here"))))))

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