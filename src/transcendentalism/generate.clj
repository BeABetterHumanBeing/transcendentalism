(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
    [clojure.string :as str]))

(use 'transcendentalism.graph
     'transcendentalism.xml)

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

(defn- html [& contents] (xml-tag "html" {} (apply str contents)))

(defn- head [contents] (xml-tag "head" {} contents))

(defn- body [& contents] (xml-tag "body" {} (apply str contents)))

(defn- div [attrs & contents] (xml-tag "div" attrs (apply str contents)))

(defn- p [attrs contents] (xml-tag "p" attrs contents))

(defn- a [attrs contents] (xml-tag "a" attrs contents))

(defn- ul [contents] (xml-tag "ul" {} contents))

(defn- li
  [contents]
  (apply str
    (map #(xml-tag "li" {} %) contents)))

(defn- hr [] (str "<hr>"))

(defn- h1 [attrs contents] (xml-tag "h1" attrs contents))

(defn- img [attrs] (xml-open "img" attrs))

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

(defn- font-style
  [contents]
  (str "font-style: " contents ";"))

(defn- font-size
  [contents]
  (str "font-size: " contents ";"))

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
    (debug (css "div" {"class" "content"}
      (border-style "dashed")
      (border-width "1px")))
    (css "div" {"class" "quote"}
      (padding "0px" "100px")
      (font-size "large")
      (font-family "Times" "serif")
      (font-style "italic"))
    (css "p" {"class" "author"}
      (text-align "right"))
    (css "div" {"class" "footer"} "")]))

(defn- generate-item-text
  "Returns the HTML corresponding to a /type/item/text"
  [triples]
  (let [text-triples (filter #(= (:pred %) "/item/text/text") triples),
        text (str/join "\n" (map :obj text-triples))]
    (div {"class" "content"}
      ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
      ; TODO(gierl): Handle /item/text/url
      text)))

(defn- generate-item-quote
  "Returns the HTML corresponding to a /type/item/quote"
  [triples]
  (let [pred-triples (collect-triples-by-pred triples),
        quote-text (:obj (pred-triples "/item/quote/text")),
        author (:obj (pred-triples "/item/quote/author"
                                   [(->Triple nil nil "Anonymous")]))]
    (div {"class" "content quote"}
      (p {} (str "\"" quote-text "\""))
      (p {"class" "author"} (str "-" author)))))

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
      (case (:pred (first item-type))
        "/type/item/text" (generate-item-text triples),
        "/type/item/quote" (generate-item-quote triples),
        "/type/item/image" (generate-item-image triples),
        "/type/item/ordered_set" (generate-item-ordered-set triples),
        (assert false
          (str "ERROR - Type " (:pred (first item-type)) "not supported"))))))

(defrecord Cxn [link name])

(defn- build-cxns
  "Determines the connections available from a given sub"
  [graph encodings sub]
  (map
    (fn [triple]
      (let [obj (:obj triple),
            link (str (obj encodings) ".html"),
            title (get-unique graph obj "/essay/title")]
        (case (:pred triple)
          ; TODO(gierl) Differentiate the types of flows by their types.
          "/essay/flow/home" (->Cxn link title)
          "/essay/flow/next" (->Cxn link title)
          "/essay/flow/see_also" (->Cxn link title)
          (assert false (str "ERROR - Type" (:pred triple) "not supported")))))
    (filter #(str/starts-with? (:pred %) "/essay/flow") (all-triples graph sub))))

(defn- generate-link
  "Returns the HTML for a link in the footer"
  [cxn]
  (a {"href" (:link cxn)} (:name cxn)))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a /type/essay_segment"
  [graph encodings sub]
  (html
    (head (xml-open "link" {
      "rel" "stylesheet",
      "href" "styles.css"}))
    (body
      (debug
        ; Debugging information appears up top.
        (div {"class" "debug"}
          (p {} "Triples")
          (ul (li (map print-triple (all-triples graph sub))))))
      ; The contents of the segment appear within a single div.
      (div {"class" "segment"}
        (div "")
        (div {}
          (let [title (get-unique graph sub "/essay/title")]
            (h1 {"class" "header"} title))
          (hr)
          (let [content-sub (get-unique graph sub "/essay/contains")]
            (generate-item graph content-sub))
          (hr)
          (div {"class" "footer"}
            (let [cxns (build-cxns graph encodings sub)]
              (str/join " " (map #(generate-link %) cxns)))
            ))))))

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
