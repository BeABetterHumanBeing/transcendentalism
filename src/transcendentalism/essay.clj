(ns transcendentalism.essay
  (:require
    [clojure.string :as str]))

(use 'transcendentalism.encoding
     'transcendentalism.graph
     'transcendentalism.html
     'transcendentalism.schema)

(defn sub-suffix
  [sub suffix]
  (keyword (str (name sub) "-" suffix)))

(defn item-sub [sub] (sub-suffix sub "i"))

(defprotocol Loom
  (add-triples [loom new-triples] "Adds some triples to the thread")
  (essay-triples [loom] "Returns the triples on the thread")
  (get-essay-sub [loom] "Returns the top-level essay sub")
  (fork-loom [loom new-sub]
    "Returns a new essay thread with the same essay sub")
  (initiate [loom]
    "Adds a sub as the initial segment in an essay thread")
  (push-block [loom] "Adds a new block")
  (push-inline [loom] "Adds a new inline to the current block")
  (major-key [loom] "Returns the current major key")
  (minor-key [loom] "returns the current minor key")
  (knot-root-menu [loom label title]
    "Marks a given essay as the root of some label")
  (knot-file-under [loom label] "Files the given essay under some label")
  (knot-footnote [loom virtual-sub fns])
  (knot-paragraph [loom fns])
  (knot-text [loom lines])
  (knot-tangent [loom virtual-sub lines])
  (knot-see-also [loom essay-sub lines])
  (knot-link [loom url lines])
  (knot-credit [loom whom f]
    "Adds /credit property to all /item/segments produced by some function")
  (knot-block-item [loom f]
    "Given a function that takes a sub and produces triples, adds the necessary
     triples to make it a block item")
  (knot-poem [loom lines])
  (knot-image [loom url alt-text width height])
  (knot-quote [loom q author])
  (knot-big-emoji [loom emoji])
  (knot-q-and-a [loom q a])
  (knot-list [loom is-ordered header-or-nil items])
  (knot-contact-email [loom email-address])
  (knot-thesis [loom line])
  (knot-matrix [loom rows columns contents])
  (knot-html-passthrough [loom html]
    "Takes some HTML, and passes it straight-through, effectively by-passing the
     schema layer. This should only be used for bespoke content.")
  (knot-definition [loom word part-of-speech definitions]))

(defn create-loom
  ([essay-sub]
    (let [t (create-loom essay-sub essay-sub)]
      (initiate t)
      t))
  ([essay-sub sub]
   (let [key-gen (create-key-gen sub),
         triples (atom [])]
     (reify Loom
       (add-triples [loom new-triples]
         (if (instance? transcendentalism.graph.Triple new-triples)
           (reset! triples (conj @triples new-triples))
           (reset! triples (concat @triples (flatten new-triples))))
         [])
       (essay-triples [loom] @triples)
       (get-essay-sub [loom] essay-sub)
       (fork-loom [loom new-sub]
         (create-loom essay-sub new-sub))
       (initiate [loom]
         (let [prev (prev-major-key key-gen),
               sub (push-major-key key-gen)]
           (add-triples loom
             (->Triple prev "/essay/contains" sub {}))))
       (push-block [loom]
         (let [prev (prev-major-key key-gen),
               sub (push-major-key key-gen)]
           (add-triples loom
             (->Triple prev "/segment/flow/block" sub {}))))
       (push-inline [loom]
         (let [prev (prev-minor-key key-gen),
               sub (push-minor-key key-gen)]
           (add-triples loom
             (->Triple prev "/segment/flow/inline" sub {}))))
       (major-key [loom] (prev-major-key key-gen))
       (minor-key [loom] (prev-minor-key key-gen))
       (knot-root-menu [loom label title]
         (add-triples loom
           (->Triple (get-essay-sub loom) "/essay/flow/menu" label {"/title" title})))
       (knot-file-under [loom label]
         (add-triples loom
           (->Triple (get-essay-sub loom) "/essay/flow/home" label {"/label" :menu})))
       (knot-footnote [loom virtual-sub fns]
         (let [sub (if (fn? virtual-sub) (virtual-sub loom) virtual-sub),
               loom (fork-loom loom sub)]
           (concat
             (flatten
               (into [] (map #(% loom)
                 (reduce
                   (fn [result f]
                     (if (contains? (meta f) :no-block)
                       (conj result f)
                       (concat result [push-block f])))
                   [(first fns)] (rest fns)))))
             (essay-triples loom))))
       (knot-paragraph [loom fns]
         (let [loom (fork-loom loom (major-key loom))]
           (concat
             (flatten
               (into []
                 (map #(% loom)
                   (reduce
                     (fn [result f]
                       (concat result [push-inline f]))
                     [(first fns)] (rest fns)))))
             (essay-triples loom))))
       (knot-text [loom lines]
         (let [sub (minor-key loom),
               item-keyword (item-sub sub),
               joined-lines (reduce
                              (fn [result line]
                                (if (or (= (count line) 0)
                                        (= (.charAt line 0) \<)
                                        (= (count result) 0)
                                        (= (.charAt result (dec (count result))) \>))
                                    (str result line)
                                    (str result " " line)))
                              (first lines) (rest lines))]
           (add-triples loom
             [(types schema sub "/segment")
              (->Triple sub "/segment/contains" item-keyword {})
              (types schema item-keyword "/item/inline")
              (->Triple item-keyword "/item/inline/text" joined-lines {})])))
       (knot-tangent [loom virtual-sub lines]
         (let [sub (if (fn? virtual-sub) (virtual-sub loom) virtual-sub),
               k (minor-key loom)]
           (add-triples loom
             [(knot-text loom lines)
              (->Triple (item-sub k) "/item/inline/tangent" sub {})])))
       (knot-see-also [loom essay-sub lines]
         (let [k (minor-key loom)]
           (add-triples loom
             [(knot-text loom lines)
              (->Triple (item-sub k) "/item/inline/see_also" essay-sub {})])))
       (knot-link [loom url lines]
         (let [k (minor-key loom)]
           (add-triples loom
             [(knot-text loom lines)
              (->Triple (item-sub k) "/item/inline/url" url {})])))
       (knot-credit [loom whom f]
         ; TODO - BUG - Since functions are no longer returning triples, they
         ; will not have the appropriate credit added. Will have to add "author"
         ; as the creator on the loom, so that one can change it, stitch away,
         ; and return to before.
         (let [triples (flatten (f loom)),
               subs (map :sub
                         (filter #(= (:pred %) "/type/segment") triples))]
           (add-triples loom
             (concat
               triples
               (map #(->Triple % "/segment/author" whom {}) subs)))))
       (knot-block-item [loom f]
         (let [block-sub (major-key loom),
               item-sub (item-sub block-sub)]
           (add-triples loom
             (concat [(types schema block-sub "/segment"),
                      (->Triple block-sub "/segment/contains" item-sub {})]
                     (f loom item-sub)))))
       (knot-poem [loom lines]
         (knot-block-item loom
           (fn [loom sub]
             [(types schema sub "/item/poem")
              (map
                (fn [i]
                  (let [line (nth lines i)]
                    (->Triple sub "/item/poem/line" line {"/order" i})))
                (range (count lines)))])))
       (knot-image [loom url alt-text width height]
         (knot-block-item loom
           (fn [loom sub]
             [(types schema sub "/item/image")
              (->Triple sub "/item/image/url" url {})
              (->Triple sub "/item/image/alt_text" alt-text {})
              (if (nil? width)
                []
                (->Triple sub "/item/image/width" width {}))
              (if (nil? height)
                []
                (->Triple sub "/item/image/height" height {}))])))
       (knot-quote [loom q author]
         (knot-block-item loom
           (fn [loom sub]
             [(types schema sub "/item/quote")
              (->Triple sub "/item/quote/text" q {})
              (if (nil? author)
                []
                (->Triple sub "/item/quote/author" author {}))])))
       (knot-big-emoji [loom emoji]
         (knot-block-item loom
           (fn [loom sub]
             [(types schema sub "/item/big_emoji")
              (->Triple sub "/item/big_emoji/emoji" emoji {})])))
       (knot-q-and-a [loom q a]
         (knot-block-item loom
           (fn [t sub]
             (let [q-sub (sub-suffix sub "q"),
                   a-sub (sub-suffix sub "a"),
                   q-loom (fork-loom t q-sub),
                   a-loom (fork-loom t a-sub)]
               [(q q-loom)
                (a a-loom)
                (types schema sub "/item/q_and_a")
                (->Triple sub "/item/q_and_a/question" q-sub {})
                (->Triple sub "/item/q_and_a/answer" a-sub {})
                (essay-triples q-loom)
                (essay-triples a-loom)]))))
       (knot-list [loom is-ordered header-or-nil items]
         (knot-block-item loom
           (fn [t sub]
             (let [item-subs (map #(sub-suffix sub (str "i" %))
                                  (range (count items))),
                   header-sub (sub-suffix sub "h")]
               (concat
                 (types schema sub "/item/bullet_list")
                 (if (nil? header-or-nil)
                   []
                   (let [header-loom (fork-loom t header-sub)]
                     [(header-or-nil header-loom)
                      (->Triple sub "/item/bullet_list/header" header-sub {})
                      (essay-triples header-loom)]))
                 [(->Triple sub "/item/bullet_list/is_ordered" is-ordered {})]
                 (map #(let [item-loom (fork-loom t (second %))]
                         (concat ((first %) item-loom)
                                 (essay-triples item-loom)))
                      (map vector items item-subs))
                 (map #(->Triple sub "/item/bullet_list/point" (first %)
                                 {"/order" (second %)})
                      (map vector item-subs (range (count item-subs)))))))))
       (knot-contact-email [loom email-address]
         (knot-block-item loom
           (fn [t sub]
             [(types schema sub "/item/contact"),
              (->Triple sub "/item/contact/email" email-address {})])))
       (knot-thesis [loom line]
         (knot-block-item loom
           (fn [t sub]
             [(types schema sub "/item/thesis"),
              (->Triple sub "/item/thesis/contains" line {})])))
       (knot-matrix [loom rows columns contents]
         (knot-block-item loom
           (fn [t sub]
             (let [index-1d (fn [items]
                              (reduce
                                (fn [result i]
                                  (conj result {:val (nth items i),
                                                :i i}))
                               [] (range (count items)))),
                   index-2d (fn [item-lists]
                              (reduce
                                (fn [result i]
                                  (reduce
                                    (fn [result j]
                                      (conj result {:val (nth (nth item-lists i) j),
                                                    :i i,
                                                    :j j}))
                                   result (range (count (nth item-lists i)))))
                               [] (range (count item-lists)))),
                   item (fn [val gen-triple]
                          (if (nil? val)
                            []
                            (let [t (fork-loom t (sub-suffix sub (gen-key 5)))]
                              (conj (if (string? val)
                                        (concat (knot-text t [val]) (essay-triples t))
                                        (concat (val t) (essay-triples t)))
                                    (gen-triple (minor-key t)))))),
                   rows* (filter #(not (nil? (:val %))) (index-1d rows)),
                   columns* (filter #(not (nil? (:val %))) (index-1d columns)),
                   contents* (filter #(not (nil? (:val %))) (index-2d contents))]
               [(types schema sub "/item/table"),
                (map #(item (:val %)
                            (fn [obj]
                              (->Triple sub "/item/table/label" obj {"/row" (:i %)})))
                     rows*),
                (map #(item (:val %)
                            (fn [obj]
                              (->Triple sub "/item/table/label" obj {"/column" (:i %)})))
                     columns*),
                (map #(item (:val %)
                            (fn [obj]
                              (->Triple sub "/item/table/cell" obj {"/row" (:i %),
                                                                    "/column" (:j %)})))
                     contents*)]))))
       (knot-html-passthrough [loom html]
         (knot-block-item loom
           (fn [t sub]
             [(types schema sub "/item/raw_html")
              (->Triple sub "/item/raw_html/contains" html {})])))
       (knot-definition [loom word part-of-speech definitions]
         (knot-block-item loom
           (fn [t sub]
             [(types schema sub "/item/definition"),
              (->Triple sub "/item/definition/word" word {}),
              (->Triple sub "/item/definition/part_of_speech" part-of-speech {}),
              (map
               (fn [i]
                 (->Triple sub "/item/definition/definition" (nth definitions i)
                           {"/order" i}))
               (range (count definitions)))])))))))

(defn f
  "Returns a virtual sub that produces footnote names"
  [num]
  (fn [t]
    (sub-suffix (get-essay-sub t) (str "f" num))))

(defn root-menu
  [label title]
  ^{:no-block true} (fn [t] (knot-root-menu t label title)))

(defn file-under
  [label]
  ^{:no-block true} (fn [t] (knot-file-under t label)))

(defn essay
  [sub title & fns]
  (let [t (create-loom sub)]
    (concat 
      (flatten [
        (types schema sub "/essay")
        (->Triple sub "/essay/title" title {})
        (into [] (map #(% t)
          (reduce
            (fn [result f]
              (if (contains? (meta f) :no-block)
                (conj result f)
                (concat result [push-block f])))
            [(first fns)] (rest fns))))
      ])
      (essay-triples t))))

(defn essay-series
  "Adds triples connecting a series of essay segments. The first segment will
   be used as the home for the rest of the series."
  [subs]
  (let [home (first subs)]
    (concat
      (into [] (map #(->Triple % "/essay/flow/home" home {}) (rest subs)))
      (into [] (map #(->Triple
                      (get subs %) "/essay/flow/next" (get subs (inc %)) {})
                    (range (dec (count subs))))))))

(defn footnote
  [virtual-sub & fns]
  ^{:no-block true} (fn [t] (knot-footnote t virtual-sub fns)))

(defn poem
  [& lines]
  (fn [t] (knot-poem t lines)))

(defn image
  ([url alt-text] (image url alt-text nil nil))
  ([url alt-text width height]
    (fn [t] (knot-image t url alt-text width height))))

(defn quote*
  ([q] (quote* q nil))
  ([q author] (fn [t] (knot-quote t q author))))

(defn big-emoji
  [emoji]
  (fn [t] (knot-big-emoji t emoji)))

(defn paragraph
  [& fns]
  (fn [t] (knot-paragraph t fns)))

(defn text
  [& lines]
  (fn [t] (knot-text t lines)))

(defn tangent
  [virtual-sub & lines]
  (fn [t] (knot-tangent t virtual-sub lines)))

(defn see-also
  [essay-sub & lines]
  (fn [t] (knot-see-also t essay-sub lines)))

(defn link
  [url & lines]
  (fn [t] (knot-link t url lines)))

(defn ex
  "Include an example"
  [& lines]
  (str (span {"class" "ex"} "Ex:") " " (i (str "\"" (str/join " " lines) "\""))))

(defn heading [line] (h2 {"class" "header"} line))

; A very commonly-used particle.
(def dot (text "."))

(def tm
  (i "Transcendental Metaphysics"))

(defn m
  "Makes some text monospaced"
  [& lines]
  (span {"class" "mono"} (str/join " " lines)))

(defn q-and-a
  [q a]
  (fn [t] (knot-q-and-a t q a)))

(defn bullet-list
  [header-or-nil & items]
  (fn [t] (knot-list t false header-or-nil items)))

(defn numbered-list
  [header-or-nil & items]
  (fn [t] (knot-list t true header-or-nil items)))

(defn contact-email
  [email-address]
  (fn [t] (knot-contact-email t email-address)))

(defn thesis
  [line]
  (fn [t] (knot-thesis t line)))

(defn matrix
  [rows columns contents]
  (fn [t] (knot-matrix t rows columns contents)))

(defn credit
  ([f] (credit "Anonymous" f))
  ([whom f] (fn [t] (knot-credit t whom f))))

(defn html-passthrough
  [html]
  (fn [t] (knot-html-passthrough t html)))
