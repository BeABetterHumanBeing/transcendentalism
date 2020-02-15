(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
    [clojure.string :as str]))

(use 'transcendentalism.css
     'transcendentalism.encoding
     'transcendentalism.graph
     'transcendentalism.js
     'transcendentalism.xml)

(defn- create-homes
  "Produces a sub->home map of /essay/flow/home"
  [graph]
  (reduce
    (fn [result sub]
      (assoc result sub
        (get-unique graph sub "/essay/flow/home")))
    {} (all-nodes graph "/type/essay")))

(defn- find-transitive-homes
  "Returns the transitive closure of all homes of a sub"
  [homes sub]
  (loop [all-homes []
         curr sub]
    (if (= curr (homes curr))
      ; At the ur-home, the monad.
      all-homes
      (recur (conj all-homes (homes curr)) (homes curr)))))

(defn- filter-and-order
  "Filters a group of triples by predicate, and orders them by metadata"
  [triples pred]
  (sort #(< (get-property :order (:obj %1) 0)
            (get-property :order (:obj %2) 0))
        (filter #(= (:pred %) pred) triples)))

(defn- clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn- html [& contents] (xml-tag "html" {} (apply str contents)))

(defn- head [contents] (xml-tag "head" {} contents))

(defn- body [attrs & contents] (xml-tag "body" attrs (apply str contents)))

(defn- div [attrs & contents] (xml-tag "div" attrs (apply str contents)))

(defn- p [attrs contents] (xml-tag "p" attrs contents))

(defn- span [attrs contents] (xml-tag "span" attrs contents))

(defn- a [attrs contents] (xml-tag "a" attrs contents))

(defn- ul [contents] (xml-tag "ul" {} contents))

(defn- li
  [contents]
  (apply str
    (map #(xml-tag "li" {} %) contents)))

(defn- hr [] (str "<hr>"))

(defn- h1 [attrs contents] (xml-tag "h1" attrs contents))

(defn- h2 [attrs contents] (xml-tag "h2" attrs contents))

(defn- img [attrs] (xml-open "img" attrs))

(defn- button [attrs contents] (xml-tag "button" attrs contents))

(defn- make-footnote-map
  "Returns a sub->encoding map for the footnotes associated with the given sub"
  [graph sub]
  (reduce
    (fn [result triple]
      (assoc result
        (:obj triple) (gen-key 8)))
    {} (all-triples graph sub "/item/footnote")))

(defn- generate-item-text
  "Returns the HTML corresponding to a /type/item/text"
  [triples footnote-map]
  (let [sub (:sub (first triples)),
        text-triples (filter-and-order triples "/item/text/text")]
    (div {"class" (str "content text"
                    (if (contains? footnote-map sub) " footnote" ""))}
      (str/join "\n"
        (map
          (fn [triple]
            (let [obj (:obj triple),
                  text (if (vector? obj) (first obj) obj),
                  footnote (get-property :footnote obj nil)]
              (if (nil? footnote)
                (span {} text)
                (span {"class" "tangent"} text))))
          text-triples)))))

(defn- generate-item-big-emoji
  [triples]
  (div {"class" "content emoji"}
    (:obj (first (filter #(= (:pred %) "/item/big_emoji/emoji") triples)))))

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
    (let [triples-by-pred (collect-triples-by-pred triples),
          image-url-triple (triples-by-pred "/item/image/url"),
          image-alt-text-triple (triples-by-pred "/item/image/alt_text")]
      ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
      (img {"src" (:obj image-url-triple),
            "alt" (:obj image-alt-text-triple)}))))

(defn- generate-item
  "Returns the HTML corresponding to a /type/item"
  [graph sub footnote-map]
  ; The call for generating ordered set items must be included here so that it
  ; can recursively call its parent function.
  (letfn [(generate-item-ordered-set [triples]
            (div {"class" "content"}
              ; TODO(gierl): Handle /item/internal_link, /item/footnote, and /item/label
              (let [contents (filter-and-order triples "/item/contains")]
                (apply str
                  (map #(generate-item graph (first (:obj %)) footnote-map)
                       contents)))))]
    (let [triples (all-triples graph sub),
          item-type (filter #(and (str/starts-with? (:pred %) "/type")
                                  (not (= (:pred %) "/type/item"))) triples)]
      (case (:pred (first item-type))
        "/type/item/text" (generate-item-text triples footnote-map),
        "/type/item/big_emoji" (generate-item-big-emoji triples),
        "/type/item/quote" (generate-item-quote triples),
        "/type/item/image" (generate-item-image triples),
        "/type/item/ordered_set" (generate-item-ordered-set triples),
        (assert false
          (str "ERROR - Type " (:pred (first item-type)) "not supported"))))))

(defrecord Cxn [encoded_obj name type])

(defn- build-cxns
  "Determines the connections available from a given sub"
  [graph encodings sub]
  (map
    (fn [triple]
      (let [obj (:obj triple),
            encoded_obj (obj encodings),
            title (get-unique graph obj "/essay/title")]
        (case (:pred triple)
          "/essay/flow/home" (->Cxn encoded_obj title "up")
          "/essay/flow/next" (->Cxn encoded_obj title "down")
          "/essay/flow/see_also" (->Cxn encoded_obj title "across")
          (assert false (str "ERROR - Type " (:pred triple) " not supported")))))
    (filter #(str/starts-with? (:pred %) "/essay/flow") (all-triples graph sub))))

(defn- generate-link
  "Returns the HTML for a link in the footer"
  [encoded_id, cxn]
  (let [link-id (str encoded_id "-" (:encoded_obj cxn)),
        name (str (case (:type cxn)
                    "up" "&#8593 ",
                    "down" "&#8595 ",
                    "across" "&#8594 ",
                    "")
                  (:name cxn))]
    (if static-html-mode
      (a {"id" link-id,
          "href" (str (:encoded_obj cxn) ".html")}
        name)
      (button {"id" link-id,
               "class" (str "link_segment " (:type cxn)),
               "onclick" (call-js "openSegment"
                           (js-str encoded_id)
                           (js-str (:encoded_obj cxn))
                           (js-str (:name cxn)))}
              name))))

(defn- generate-under-construction-splash
  "Returns a div that shows that the segment is under construction"
  []
  (div {"class" "construction-back"}
    (div {"class" "construction-front"}
      (h2 {} "UNDER CONSTRUCTION")
      (div {"class" "construction-separator"})
      (p {} "Connect with me if you want me to expedite its work"))))

(defn- generate-essay-segment
  "Returns the HTML corresponding to a /type/essay"
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
    (let [id (sub encodings)]
      (body
        (if static-html-mode
          {}
          {
            "onload" (call-js "segmentLoadedCallback"
                       (js-str id)
                       (js-str (get-unique graph sub "/essay/title"))
                       (js-str id)
                       (js-array
                         (map #(js-str (% encodings))
                              (find-transitive-homes homes sub)))),
          })
        (if (= sub :monad)
          "" ; No segments are inserted above the monad.
          (div {"id" (seg-id id "above")}
            (debug "Insert Top Segments Here")))
        ; The contents of the segment appear within a single div.
        (div {"class" "segment",
              "id" id}
          (div "") ; Empty divs occupy first and last cells in grid.
          (div {}
            (let [title (get-unique graph sub "/essay/title")]
              (h1 {"class" "header"} title))
            (hr)
            (let [labels (into #{}
                           (map :obj (all-triples graph sub "/essay/label")))]
              (if (contains? labels :under-construction)
                (generate-under-construction-splash)
                (let [content-sub (get-unique graph sub "/essay/contains"),
                      footnote-map (make-footnote-map graph sub)]
                  (generate-item graph content-sub footnote-map))))
            (hr)
            (div {"id" (seg-id id "footer"),
                  "class" "footer"}
              (let [cxns (build-cxns graph encodings sub)]
                (str/join " " (map #(generate-link id %) cxns))))
            (div {"id" (seg-id id "buffer"),
                  "class" "buffer"})))))))

(defn generate-output
  "Convert a validated graph into the HTML, CSS, and JS files that compose the website"
  [graph]
  (let [encodings (create-encodings (all-nodes graph)),
        homes (create-homes graph)]
    (clear-directory "output")
    (doseq
      [sub (all-nodes graph "/type/essay")]
      (let
        [filename (str "output/" (sub encodings) ".html")]
        (do
          (spit filename (generate-essay-segment graph encodings homes sub))
          (println "Generated" filename))))
    (spit "output/styles.css" (stylesheet))
    (if static-html-mode nil (spit "output/script.js" (script)))))
