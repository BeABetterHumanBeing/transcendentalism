(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
    [clojure.string :as str]))

(use 'transcendentalism.css
     'transcendentalism.graph
     'transcendentalism.js
     'transcendentalism.xml)

(defn- enable-debugging
  "Adds the 'debug' class to attrs if debugging is enabled"
  [attrs]
  (if debugging-mode
    (assoc attrs "class" (str (attrs "class" "") " debug"))
    attrs))

; TODO(gierl): Come up with an encoding scheme that's more stable, so that links
; aren't broken every time the website is regenerated.
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

(defn- create-homes
  "Produces a sub->home map of /essay/flow/home"
  [graph]
  (reduce
    (fn [result sub]
      (assoc result sub
        (get-unique graph sub "/essay/flow/home")))
    {} (all-nodes graph "/type/essay_segment")))

(defn- find-transitive-homes
  "Returns the transitive closure of all homes of a sub"
  [homes sub]
  (loop [all-homes []
         curr sub]
    (if (= curr (homes curr))
      ; At the ur-home, the monad.
      all-homes
      (recur (conj all-homes (homes curr)) (homes curr)))))

(defn- clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn- html [& contents] (xml-tag "html" {} (apply str contents)))

(defn- head [contents] (xml-tag "head" {} contents))

(defn- body [attrs & contents] (xml-tag "body" attrs (apply str contents)))

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

(defn- button [attrs contents] (xml-tag "button" attrs contents))

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

(defrecord Cxn [encoded_obj name])

(defn- build-cxns
  "Determines the connections available from a given sub"
  [graph encodings sub]
  (map
    (fn [triple]
      (let [obj (:obj triple),
            encoded_obj (obj encodings),
            title (get-unique graph obj "/essay/title")]
        (case (:pred triple)
          ; TODO(gierl) Differentiate the types of flows by their types.
          "/essay/flow/home" (->Cxn encoded_obj title)
          "/essay/flow/next" (->Cxn encoded_obj title)
          "/essay/flow/see_also" (->Cxn encoded_obj title)
          (assert false (str "ERROR - Type" (:pred triple) "not supported")))))
    (filter #(str/starts-with? (:pred %) "/essay/flow") (all-triples graph sub))))

(defn- generate-link
  "Returns the HTML for a link in the footer"
  [encoded_id, cxn]
  (let [link-id (str encoded_id "-" (:encoded_obj cxn))]
    (if static-html-mode
      (a {"id" link-id,
          "href" (str (:encoded_obj cxn) ".html")}
        (:name cxn))
      (button {"id" link-id,
               "class" "link_segment",
               "onclick" (call-js "openSegment"
                           (format-as-string encoded_id)
                           (format-as-string (:encoded_obj cxn)))}
              (:name cxn)))))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a /type/essay_segment"
  [graph encodings homes sub]
  (html
    (head (str
      (xml-open "link" {
        "rel" "stylesheet",
        "href" "styles.css"})
      (if static-html-mode
        ""
        (str
          ; Include JQuery from Google CDN.
          (xml-tag "script" {"src" "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"} "")
          (xml-tag "script" {"src" "script.js"} "")))))
    (body
      (if static-html-mode
        {}
        {
          "onload" (call-js "segmentLoadedCallback"
                     (format-as-string (sub encodings))
                     (format-as-string (sub encodings))
                     (format-as-array
                       (map #(format-as-string (% encodings))
                            (find-transitive-homes homes sub)))),
        })
      (if (= sub :monad)
        "" ; No segments are inserted above the monad.
        (div (enable-debugging {"id" (top-insertion-id (sub encodings))})
          (debug "Insert Top Segments Here")))
      (debug
        ; Debugging information appears up top.
        (div {"class" "debug"}
          (p {} "Triples")
          (ul (li (map print-triple (all-triples graph sub))))))
      ; The contents of the segment appear within a single div.
      (div {"class" "segment",
            "id" (sub encodings)}
        (div "") ; Empty divs occupy first and last cells in grid.
        (div {}
          (let [title (get-unique graph sub "/essay/title")]
            (h1 {"class" "header"} title))
          (hr)
          (let [content-sub (get-unique graph sub "/essay/contains")]
            (generate-item graph content-sub))
          (hr)
          (div {"id" (str (sub encodings) "-footer"),
                "class" "footer"}
            (let [cxns (build-cxns graph encodings sub)]
              (str/join " " (map #(generate-link (sub encodings) %) cxns))))
          ; TODO(gierl): Add a cute watermark to the background of the buffer.
          (div {"id" (str (sub encodings) "-buffer"),
                "class" "buffer"}))))))

(defn generate-output
  "Convert a validated graph into the HTML, CSS, and JS files that compose the website"
  [graph]
  (let [encodings (create-encodings graph),
        homes (create-homes graph)]
    (clear-directory "output")
    (doseq
      [sub (all-nodes graph "/type/essay_segment")]
      (let
        [filename (str "output/" (sub encodings) ".html")]
        (do
          (spit filename (generate-essay-segment graph encodings homes sub))
          (println "Generated" filename))))
    (spit "output/styles.css" (stylesheet))
    (if static-html-mode nil (spit "output/script.js" (script)))))
