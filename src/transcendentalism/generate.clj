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

(defn- unique-or-nil
  "Returns the unique object of the given pred, or nil"
  [triples pred]
  (let [selected (filter #(= (:pred %) pred) triples)]
    (if (empty? selected)
      nil
      (:obj (first selected)))))

(defn- clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn- html [& contents] (xml-tag "html" {} (apply str contents)))

(defn- head [contents] (xml-tag "head" {} contents))

(defn- body [attrs & contents] (xml-tag "body" attrs (apply str contents)))

(defn- div [attrs & contents] (xml-tag "div" attrs (apply str contents)))

(defn- p [attrs contents] (xml-tag "p" attrs contents))

(defn- span [attrs & contents] (xml-tag "span" attrs (apply str contents)))

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

(defn- dbg-able
  "Adds the dbg class, if in debugging mode"
  [classes]
  (if debugging-mode
    (str "dbg " classes)
    classes))

(defn- generate-item-poem
  [triples]
  (div {"class" (dbg-able "poem")}
    (str/join "\n"
      (map
        (fn [line-triple]
          (p {"class" "poem-line"} (first (:obj line-triple))))
        (filter-and-order triples "/item/poem/line")))))

(defn- generate-item-big-emoji
  [triples]
  (div {"class" (dbg-able "emoji")}
    (unique-or-nil triples "/item/big_emoji/emoji")))

(defn- generate-item-quote
  "Returns the HTML corresponding to a /type/item/quote"
  [triples]
  (let [pred-triples (collect-triples-by-pred triples),
        quote-text (:obj (pred-triples "/item/quote/text")),
        author (:obj (pred-triples "/item/quote/author"
                                   [(->Triple nil nil "Anonymous")]))]
    (div {"class" (dbg-able "quote")}
      (p {} (str "\"" quote-text "\""))
      (p {"class" "author"} (str "-" author)))))

(defn- generate-item-image
  "Returns the HTML corresponding to a /type/item/image"
  [triples]
  (div {"class" (dbg-able "")}
    (let [triples-by-pred (collect-triples-by-pred triples),
          image-url-triple (triples-by-pred "/item/image/url"),
          image-alt-text-triple (triples-by-pred "/item/image/alt_text")]
      (img {"src" (:obj image-url-triple),
            "alt" (:obj image-alt-text-triple)}))))

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

(defn- sort-by-cxn-type
  "Sorts a group of cxns so that they go down, across, then up"
  [cxns]
  (let [cxns-by-type (group-by :type cxns)]
    (concat (cxns-by-type "down" [])
            (cxns-by-type "across" [])
            (cxns-by-type "up" []))))

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

(defn- render-footnote-idx
  [ancestry]
  (if (empty? ancestry)
    ""
    (str "[" (str/join "-" ancestry) "]")))

(defn- generate-inline-item
  [triples footnote-map]
  (let [text (unique-or-nil triples "/item/inline/text"),
        tangent (unique-or-nil triples "/item/inline/tangent")]
    (if (nil? tangent)
      (span {} text)
      (span {"class" "tangent",
             "onclick" (call-js "toggleFootnote"
                         (js-str (:id (footnote-map tangent))))}
        (str text " " (render-footnote-idx (:ancestry (footnote-map tangent))))))))

(defn- render-item
  "Renders the HTML for an item"
  [graph item footnote-map]
  (let [triples (all-triples graph item),
        item-type (filter #(and (str/starts-with? (:pred %) "/type")
                                (not (= (:pred %) "/type/item"))) triples)]
    (case (:pred (first item-type))
      "/type/item/poem" (generate-item-poem triples),
      "/type/item/big_emoji" (generate-item-big-emoji triples),
      "/type/item/quote" (generate-item-quote triples),
      "/type/item/image" (generate-item-image triples),
      "/type/item/inline" (generate-inline-item triples footnote-map),
      (assert false
        (str "ERROR - Type " (:pred (first item-type)) " not supported")))))

(defn- conj-vector-map
  "Merges a map into a map of vectors, conj-ing the new elements"
  [my-map my-new-elems]
  (reduce-kv
    (fn [result k v]
      (if (nil? v)
        result
        (assoc result k (conj (result k) v))))
    my-map my-new-elems))

(defn- collect-block-content
  "Follows a sequence of inline segments, collecting them and their related
   data"
  [graph sub]
  (loop [result {:contents [],
                 :tangents []},
         inline-sub sub]
    (if (nil? inline-sub)
      result
      (let [content (get-unique graph inline-sub "/segment/contains")]
        (recur (conj-vector-map
                 result {:contents content,
                         :tangents
                           (if (nil? content)
                               nil
                               (get-unique graph content "/item/inline/tangent"))})
               (get-unique graph inline-sub "/segment/flow/inline"))))))

(defn- calculate-footnote-map
  "Returns a sub->{:ancestry :id} map of all footnotes under a given segment"
  [graph sub]
  (letfn
    [(inner-footnote-map [sub ancestry idx]
       (let [block-content (collect-block-content graph sub),
             next-block (get-unique graph sub "/segment/flow/block"),
             tangents (:tangents block-content),
             new-tangents (reduce
               (fn [result i]
                 (assoc result
                   (get tangents i)
                   {:ancestry (conj ancestry (+ i idx)),
                    :id (gen-key 8)}))
               {} (range (count tangents)))]
         (apply merge
           new-tangents
           (if (nil? next-block)
             {}
             (inner-footnote-map next-block ancestry (+ idx (count tangents))))
           (map #(inner-footnote-map % (:ancestry (new-tangents %)) 1)
                tangents))))]
    (inner-footnote-map sub [] 1)))

(defn- maybe-wrap-footnote
  "If the given sub is a footnote, wraps the provided content"
  [footnote-map sub content]
  (if (contains? footnote-map sub)
    (div {"class" "footnote",
          "id" (:id (footnote-map sub))}
      content)
    content))

(defn- maybe-add-footnote-anchor
  "If the given sub is a footnote, adds the anchor (e.g. [1-2-1])"
  [footnote-map sub]
  (if (contains? footnote-map sub)
      (span {"class" "footnote-anchor"}
        (render-footnote-idx (:ancestry (footnote-map sub))) " ")
      ""))

(defn- generate-essay-contents
  [graph segment]
  (letfn
    [(generate-block-sequence [sub footnote-map]
       (let [block-content (collect-block-content graph sub),
             next-block (get-unique graph sub "/segment/flow/block")]
         (maybe-wrap-footnote footnote-map sub
           (str/join "\n" [
             (div {"class" (dbg-able "block")}
               (maybe-add-footnote-anchor footnote-map sub)
               (apply str
                 (map #(render-item graph % footnote-map)
                      (:contents block-content)))
               (str/join "\n"
                 (map
                   #(generate-block-sequence % footnote-map)
                   (:tangents block-content))))
             (if (nil? next-block)
               ""
               (generate-block-sequence next-block footnote-map))]))
         ))]
    (generate-block-sequence segment (calculate-footnote-map graph segment))))

(defn- generate-under-construction-splash
  "Returns a div that shows that the segment is under construction"
  []
  (div {"class" "construction-back"}
    (div {"class" "construction-front"}
      (h2 {} "UNDER CONSTRUCTION")
      (div {"class" "construction-separator"})
      (p {} "Connect with me if you want me to expedite its work"))))

(defn- generate-essay
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
                (let [segment-sub (get-unique graph sub "/essay/contains")]
                  (generate-essay-contents graph segment-sub))))
            (hr)
            (div {"id" (seg-id id "footer")}
              (let [cxns (sort-by-cxn-type (build-cxns graph encodings sub))]
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
          (spit filename (generate-essay graph encodings homes sub))
          (println "Generated" filename))))
    (spit "output/styles.css" (stylesheet))
    (if static-html-mode nil (spit "output/script.js" (script)))))
