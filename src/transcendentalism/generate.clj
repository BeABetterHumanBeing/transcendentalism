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

(defn- ul [attrs & contents] (xml-tag "ul" attrs (apply str contents)))

(defn- li [attrs contents] (xml-tag "li" attrs contents))

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
  [node]
  (div {"class" (dbg-able "poem")}
    (str/join "\n"
      (map #(p {"class" "poem-line"} %)
           (get-ordered-objs node "/item/poem/line")))))

(defn- generate-item-big-emoji
  [node]
  (div {"class" (dbg-able "emoji")}
    (unique-or-nil node "/item/big_emoji/emoji")))

(defn- generate-item-quote
  "Returns the HTML corresponding to a /type/item/quote"
  [node]
  (let [quote-text (unique-or-nil node "/item/quote/text"),
        author-or-nil (unique-or-nil node "/item/quote/author"),
        author (if (nil? author-or-nil) "Anonymous" author-or-nil)]
    (div {"class" (dbg-able "quote")}
      (p {} (str "\"" quote-text "\""))
      (p {"class" "author"} (str "-" author)))))

(defn- generate-item-image
  "Returns the HTML corresponding to a /type/item/image"
  [node]
  (div {"class" (dbg-able "")}
    (let [image-url-triple (unique-or-nil node "/item/image/url"),
          image-alt-text-triple (unique-or-nil node "/item/image/alt_text")]
      (img {"src" image-url-triple,
            "alt" image-alt-text-triple}))))

(defn- generate-q-and-a
  "Returns the HTML for a /type/item/q_and_a"
  [node renderer graph footnote-map]
  (let [q-block (unique-or-nil node "/item/q_and_a/question"),
        a-block (unique-or-nil node "/item/q_and_a/answer"),
        q (get-unique graph q-block "/segment/contains"),
        a (get-unique graph a-block "/segment/contains")]
    (div {"class" "q_and_a"}
      (div {"class" "q_and_a_header"} "Q:")
      (str "<i>" (renderer graph q footnote-map) "</i>")
      (div {"class" "q_and_a_header"} "A:")
      (renderer graph a footnote-map))))

(defn- generate-bullet-list
  "Returns the HTML for a /type/item/bullet_list"
  [node renderer graph footnote-map]
  (let [header-block-or-nil (unique-or-nil node "/item/bullet_list/header"),
        point-subs (map #(get-unique graph % "/segment/contains")
                        (get-ordered-objs node "/item/bullet_list/point"))]
    (div {}
      (if (nil? header-block-or-nil)
          ""
          (div {}
            (renderer graph
                      (get-unique graph header-block-or-nil "/segment/contains")
                      footnote-map)))
      (apply ul {}
        (into [] (map #(li {} (renderer graph % footnote-map)) point-subs))))))

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
  [node footnote-map]
  (let [text (unique-or-nil node "/item/inline/text"),
        tangent (unique-or-nil node "/item/inline/tangent")]
    (if (nil? tangent)
      (span {} text)
      (span {"class" "tangent",
             "onclick" (call-js "toggleFootnote"
                         (js-str (:id (footnote-map tangent))))}
        (str text " " (render-footnote-idx (:ancestry (footnote-map tangent))))))))

(defn- render-item
  "Renders the HTML for an item"
  ; TODO(gierl) Create Renderer protocol, reified over graph and footnote-map
  [graph item footnote-map]
  (letfn
    [(inner-render-item
       [graph item footnote-map]
       (let [node (get-node graph item),
             item-type (filter #(not (= % "/type/item")) (get-types node))]
         (case (first item-type)
           "/type/item/poem" (generate-item-poem node),
           "/type/item/big_emoji" (generate-item-big-emoji node),
           "/type/item/quote" (generate-item-quote node),
           "/type/item/image" (generate-item-image node),
           "/type/item/q_and_a" (generate-q-and-a node inner-render-item graph footnote-map),
           "/type/item/bullet_list" (generate-bullet-list node inner-render-item graph footnote-map),
           "/type/item/inline" (generate-inline-item node footnote-map),
           (assert false
             (str "ERROR - Type " (first item-type) " not supported")))))]
    (inner-render-item graph item footnote-map)))

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
  "Follows a sequence of inline segments, collecting their items"
  [graph sub]
  (loop [result [],
         inline-sub sub]
    (if (nil? inline-sub)
      result
      (let [content (get-unique graph inline-sub "/segment/contains")]
        (recur (conj result content)
               (get-unique graph inline-sub "/segment/flow/inline"))))))

(defn- collect-block-tangents
  "Follows a sequence of inline segments, collecting their tangents"
  [graph sub]
  (loop [result [],
         inline-sub sub]
    (if (nil? inline-sub)
      result
      (let [content (get-unique graph inline-sub "/segment/contains"),
            tangent (get-unique graph content "/item/inline/tangent")]
        (recur (if (nil? tangent)
                   result
                   (conj result tangent))
               (get-unique graph inline-sub "/segment/flow/inline"))))))

(defn- calculate-footnote-map
  "Returns a sub->{:ancestry :id} map of all footnotes under a given segment"
  [graph sub]
  (letfn
    [(inner-footnote-map [sub ancestry idx]
       (let [tangents (collect-block-tangents graph sub),
             next-block (get-unique graph sub "/segment/flow/block"),
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
    (div {"class" (if (= (count (:ancestry (footnote-map sub)))
                         1)
                      "topmost-footnote"
                      "footnote"),
          "id" (:id (footnote-map sub))}
      content)
    content))

(defn- maybe-add-footnote-anchor
  "If the given sub is a footnote, adds the anchor (e.g. [1-2-1])"
  [footnote-map sub]
  (if (contains? footnote-map sub)
      (span {"class" "footnote-anchor"}
        (span {"class" "footnote-chain",
               "style" (str "width:" (+ (* (count (:ancestry (footnote-map sub)))
                                           11)
                                        5) "px")})
        (render-footnote-idx (:ancestry (footnote-map sub))))
      ""))

(defn- generate-essay-contents
  [graph segment]
  (letfn
    [(generate-block-sequence [sub footnote-map]
       (let [block-content (collect-block-content graph sub),
             block-tangents (collect-block-tangents graph sub),
             next-block (get-unique graph sub "/segment/flow/block")]
         (maybe-wrap-footnote footnote-map sub
           (str/join "\n" [
             (div {"class" (dbg-able "block")}
               (maybe-add-footnote-anchor footnote-map sub)
               (apply str
                 (map #(render-item graph % footnote-map)
                      block-content))
               (str/join "\n"
                 (map #(generate-block-sequence % footnote-map)
                      block-tangents)))
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
                (generate-essay-contents
                  graph (get-unique graph sub "/essay/contains"))))
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
