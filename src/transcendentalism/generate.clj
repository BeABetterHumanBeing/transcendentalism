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

(defn div [attrs & contents] (xml-tag "div" attrs (apply str contents)))

(defn- p [attrs contents] (xml-tag "p" attrs contents))

(defn span [attrs & contents] (xml-tag "span" attrs (apply str contents)))

(defn- a [attrs contents] (xml-tag "a" attrs contents))

(defn- ul [attrs & contents] (xml-tag "ul" attrs (apply str contents)))

(defn- ol [attrs & contents] (xml-tag "ol" attrs (apply str contents)))

(defn- li [attrs contents] (xml-tag "li" attrs contents))

(defn- hr [] (str "<hr>"))

(defn- h1 [attrs contents] (xml-tag "h1" attrs contents))

(defn- h2 [attrs contents] (xml-tag "h2" attrs contents))

(defn- img [attrs] (xml-open "img" attrs))

(defn- button [attrs contents] (xml-tag "button" attrs contents))

(defn- input [attrs] (xml-open "input" attrs))

(defn- table [attrs contents] (xml-tag "table" attrs contents))

(defn- tr [attrs contents] (xml-tag "tr" attrs contents))

(defn- td [attrs contents] (xml-tag "td" attrs contents))

(defn- dbg-able
  "Adds the dbg class, if in debugging mode"
  [classes]
  (if debugging-mode
    (str "dbg " classes)
    classes))

(defrecord Cxn [encoded_obj name type])

(defn- build-cxns
  "Determines the connections available from a given sub"
  [graph encodings sub]
  (map
    (fn [triple]
      (let [pred (:pred triple)]
        (if (= pred "/essay/flow/random")
          (->Cxn (into #{} (map #(% encodings) (:obj triple))) "Random" "random")
          (let [obj (:obj triple),
                encoded_obj (obj encodings),
                title (get-unique graph obj "/essay/title")]
            (case (:pred triple)
              "/essay/flow/home" (->Cxn encoded_obj title "up")
              "/essay/flow/next" (->Cxn encoded_obj title "down")
              "/essay/flow/see_also" (->Cxn encoded_obj title "across")
              "/essay/flow/menu" (->Cxn encoded_obj (str "[" title " Menu]") "menu")
              (assert false (str "ERROR - Type " (:pred triple) " not supported")))))))
    (filter #(str/starts-with? (:pred %) "/essay/flow") (all-triples graph sub))))

(defn- sort-by-cxn-type
  "Sorts a group of cxns so that they go down, across, then up"
  [cxns]
  (let [cxns-by-type (group-by :type cxns)]
    (concat (cxns-by-type "down" [])
            (cxns-by-type "menu" [])
            (cxns-by-type "across" [])
            (cxns-by-type "up" [])
            (cxns-by-type "random" []))))

(defn- generate-link
  "Returns the HTML for a link in the footer"
  [encoded_id, cxn]
  (if (= (:type cxn) "random")
    (if static-html-mode
      "" ; Can't choose at random without JS.
      (button {"class" (str "link_segment " (:type cxn)),
               "onclick" (call-js "openRandomSegment"
                           (js-str encoded_id)
                           (js-array (map js-str (:encoded_obj cxn))))}
              "? Random"))
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
                name)))))

(defn- render-footnote-idx
  [ancestry]
  (if (empty? ancestry)
    ""
    (str "[" (str/join "-" ancestry) "]")))

(defprotocol Renderer
  (render-block [renderer block-sub] "Renders a /type/segment")
  (render-item [renderer node] "Renders a /type/item")
  (render-poem [renderer node] "Renders a /type/item/poem")
  (render-big-emoji [renderer node] "Renders a /type/item/big_emoji")
  (render-quote [renderer node] "Renders a /type/item/quote")
  (render-image [renderer node] "Renders a /type/item/image")
  (render-q-and-a [renderer node] "Renders a /type/item/q_and_a")
  (render-bullet-list [renderer node] "Renders a /type/item/bullet_list")
  (render-contact [renderer node] "Renders a /type/item/contact")
  (render-definition [renderer node] "Renders a /type/item/definition")
  (render-table [renderer node] "Renders a /type/item/table")
  (render-raw-html [renderer node] "Renders a /type/item/raw_html")
  (render-inline-item [renderer node] "Renders a /type/item/inline"))

(defn- create-renderer
  [graph encoded_id encodings footnote-map]
  (reify Renderer
    (render-block [renderer block-sub]
      (let [authors (all-triples graph block-sub "/segment/author"),
            contents
            (apply str
                   (map #(render-item renderer
                                      (get-node graph
                                        (get-unique graph % "/segment/contains")))
                        (transitive-closure
                          graph block-sub "/segment/flow/inline")))]
        (if (empty? authors)
            contents
            (div {"class" "authors-parent"}
              (div {"class" "authors"}
                (div {"class" "authors-chain"} "")
                (str/join ", " (map :obj authors)))
              contents))))
    (render-item [renderer node]
      (let [item-type (filter #(not (= % "/type/item")) (get-types node))]
        (case (first item-type)
          "/type/item/poem" (render-poem renderer node),
          "/type/item/big_emoji" (render-big-emoji renderer node),
          "/type/item/quote" (render-quote renderer node),
          "/type/item/image" (render-image renderer node),
          "/type/item/q_and_a" (render-q-and-a renderer node),
          "/type/item/bullet_list" (render-bullet-list renderer node),
          "/type/item/contact" (render-contact renderer node),
          "/type/item/definition" (render-definition renderer node),
          "/type/item/table" (render-table renderer node),
          "/type/item/raw_html" (render-raw-html renderer node),
          "/type/item/inline" (render-inline-item renderer node),
          (assert false
            (str "ERROR - Type " (first item-type) " not supported")))))
    (render-poem [renderer node]
      (div {"class" (dbg-able "poem")}
        (str/join "\n"
          (map #(p {"class" "poem-line"} %)
               (get-ordered-objs node "/item/poem/line")))))
    (render-big-emoji [renderer node]
      (div {"class" (dbg-able "emoji")}
        (unique-or-nil node "/item/big_emoji/emoji")))
    (render-quote [renderer node]
      (let [quote-text (unique-or-nil node "/item/quote/text"),
            author-or-nil (unique-or-nil node "/item/quote/author"),
            author (if (nil? author-or-nil) "Anonymous" author-or-nil)]
        (div {"class" (dbg-able "quote")}
          (p {} (str "\"" quote-text "\""))
          (p {"class" "author"} (str "-" author)))))
    (render-image [renderer node]
      (let [image-url-triple (unique-or-nil node "/item/image/url"),
            image-alt-text-triple (unique-or-nil node "/item/image/alt_text"),
            image-width (unique-or-nil node "/item/image/width"),
            image-height (unique-or-nil node "/item/image/height")]
        (img {"src" image-url-triple,
              "alt" image-alt-text-triple,
              "style" (str (if (nil? image-width) "" (str "width:" image-width "px"))
                           ";"
                           (if (nil? image-height) "" (str "height:" image-height "px")))})))
    (render-q-and-a [renderer node]
      (let [q-block (unique-or-nil node "/item/q_and_a/question"),
            a-block (unique-or-nil node "/item/q_and_a/answer")]
        (div {"class" "q_and_a"}
          (div {"class" "q_and_a_header"} "Q:")
          (str "<i>" (render-block renderer q-block) "</i>")
          (div {"class" "q_and_a_header"} "A:")
          (div {} (render-block renderer a-block)))))
    (render-bullet-list [renderer node]
      (let [header-block-or-nil (unique-or-nil node "/item/bullet_list/header"),
            point-blocks (get-ordered-objs node "/item/bullet_list/point"),
            is_ordered (unique-or-nil node "/item/bullet_list/is_ordered")]
        (div {}
          (if (nil? header-block-or-nil)
              ""
              (div {}
                (render-block
                  renderer
                  header-block-or-nil)))
          (apply
            (if (or (nil? is_ordered) (not is_ordered)) ul ol)
            {"class" "bullet_list"}
            (into [] (map #(li {} (render-block renderer %)) point-blocks))))))
    (render-contact [renderer node]
      (let [email-address (unique-or-nil node "/item/contact/email"),
            elem_id (gen-key 8)]
        (div {"class" "contact-container"}
          (div {"class" "contact-centered"}
            (input {"class" "contact",
                    "id" elem_id,
                    "type" "text",
                    "value" email-address,
                    "readonly" "readonly"})
            (div {"class" "contact-dash"})
            (div {"class" "contact-buttons"}
              (a {"href" "javascript:void(0);",
                  "onclick" (call-js "copyToClipboard" (js-str elem_id))}
                 "Copy")
              "/"
              (a {"href" (str "mailto:" email-address),
                  "target" "_top"} "Mail"))))))
    (render-definition [renderer node]
      (let [word (unique-or-nil node "/item/definition/word"),
            part-of-speech ({
              :noun "noun",
              :adjective "adj.",
              } (unique-or-nil node "/item/definition/part_of_speech")
              "unk"),
            definitions (get-ordered-objs node "/item/definition/definition")]
        (div {"class" "definition"}
          (span {} (str "<b>Definition</b> " word " ")
          (span {} (str "<i>(" part-of-speech ")</i>:"))
          (apply ul {"class" "bullet_list"}
            (str/join "\n"
              (map #(li {"class" "word-definition"} %) definitions)))))))
    (render-table [renderer node]
      (let [labels-n-cells
              (filter #(contains? #{"/item/table/label",
                                    "/item/table/cell"} (:pred %))
                      (get-triples node))
            row-max (apply max (map #(property % "/row" -1) labels-n-cells)),
            col-max (apply max (map #(property % "/column" -1) labels-n-cells)),
            triples-by-row (group-by #(property % "/row" -1) labels-n-cells)]
        (table {"class" "t"}
          (str/join "\n"
            (map (fn [row]
                   (let [triples-by-column (group-by #(property % "/column" -1)
                                                     (triples-by-row row []))]
                     (tr {}
                       (str/join "\n"
                         (map (fn [col]
                                (let [val (triples-by-column col nil),
                                      class (if (or (= col -1) (= row -1))
                                                "label"
                                                "cell")]
                                  (td {"class" class}
                                      (if (nil? val)
                                          ""
                                          (render-block renderer
                                                        (:obj (first val)))))))
                              (range -1 (inc col-max)))))))
                 (range -1 (inc row-max)))))))
    (render-raw-html [renderer node]
      (unique-or-nil node "/item/raw_html/contains"))
    (render-inline-item [renderer node]
      (let [text (unique-or-nil node "/item/inline/text"),
            tangent (unique-or-nil node "/item/inline/tangent"),
            see-also (unique-or-nil node "/item/inline/see_also"),
            link (unique-or-nil node "/item/inline/url")]
        (if (nil? tangent)
          (if (nil? see-also)
            (if (nil? link)
              (span {} text)
              (a {"href" link,
                  "target" "_blank"}
                (str text " &#11016")))
            (span {"class" "see-also",
                   "onclick" (call-js "seeAlsoSegment"
                               (js-str encoded_id)
                               (js-str (see-also encodings))
                               (js-str (get-unique graph see-also "/essay/title")))}
              (str text " &#8594")))
          (span {"class" "tangent",
                 "onclick" (call-js "toggleFootnote"
                             (js-str (:id (footnote-map tangent))))}
            (str text " "
              (render-footnote-idx (:ancestry (footnote-map tangent))))))))))

(defn- compare-by-priority
  "Returns a comparator that examines data in a tablet in a given order of
   priorities. Missing data is assumed to be 0."
  [tablet & priorities]
  (fn [a b]
    (let [a-data (tablet a),
          b-data (tablet b)]
      (loop [k (first priorities),
             etc (rest priorities)]
        (let [a-val (k a-data 0),
              b-val (k b-data 0)]
          (if (and (= a-val b-val) (not (empty? etc)))
            (recur (first etc) (rest etc))
            (< a-val b-val)))))))

(defn- collect-block-tangents
  "Follows a sequence of inline segments, collecting their tangents"
  [graph sub]
  (let [gq-result
        (gq
         graph
         (q-chain
           (q-kleene (fn [sub data i] (assoc data :inline i))
             (q-pred "/segment/flow/inline"))
           (q-pred "/segment/contains")
           (q-kleene (fn [sub data i] (assoc data :in-item i))
             (q-chain
               (q-or (q-pred "/item/q_and_a/question")
                     (q-pred "/item/q_and_a/answer")
                     (q-pred "/item/bullet_list/header")
                     (q-pred (fn [triple data]
                               (assoc data :order (property triple "/order" 0)))
                             "/item/bullet_list/point"))
               (q-kleene (fn [sub data i] (assoc data :in-item-inline i))
                 ; Assumes questions, answers, and points are single-blocked.
                 (q-pred "/segment/flow/inline"))
               (q-pred "/segment/contains")))
           (q-pred "/item/inline/tangent"))
         sub),
        sorted-gq-result
        (sort (compare-by-priority gq-result
                :inline :in-item :order :in-item-inline)
              (keys gq-result))]
    (into [] sorted-gq-result)))

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
                    :id (gen-key 8),
                    :root sub}))
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
  [graph encoded_id encodings segment]
  (letfn
    [(generate-block-sequence [sub footnote-map]
       (let [renderer (create-renderer graph encoded_id encodings footnote-map),
             next-block (get-unique graph sub "/segment/flow/block")]
         (maybe-wrap-footnote footnote-map sub
           (str/join "\n" [
             (div {"class" (dbg-able "block")}
               (maybe-add-footnote-anchor footnote-map sub)
               (render-block renderer sub)
               (str/join "\n"
                 (map #(generate-block-sequence % footnote-map)
                      (reduce-kv
                        (fn [result k v]
                          (if (= (:root v) sub)
                            (conj result k)
                            result))
                        [] footnote-map))))
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
        (div {"class" "segment",
              "id" id}
          (div "") ; Empty divs occupy first and last cells in grid.
          (div {}
            (let [labels (into #{}
                           (map :obj (all-triples graph sub "/essay/label"))),
                  title (get-unique graph sub "/essay/title")]
              (str/join "\n" [
                (h1 {"class" "header"}
                  (if (contains? labels :invisible)
                      (str "[" title "]")
                      title))
                (hr)
                (if (contains? labels :invisible)
                  ""
                  (str/join "\n" [
                    (if (contains? labels :under-construction)
                      (generate-under-construction-splash)
                      (generate-essay-contents
                        graph id encodings (get-unique graph sub "/essay/contains")))
                    (hr)]))]))
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
          (println
            "Generated"
            sub
            (str "\"" (unique-or-nil (get-node graph sub) "/essay/title") "\"")
            filename))))
    (spit "output/styles.css" (stylesheet))
    (if static-html-mode nil (spit "output/script.js" (script)))))
