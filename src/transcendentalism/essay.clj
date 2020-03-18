(ns transcendentalism.essay
  (:require
    [clojure.string :as str]))

(use 'transcendentalism.encoding
     'transcendentalism.glossary
     'transcendentalism.graph
     'transcendentalism.html
     'transcendentalism.loom
     'transcendentalism.schema)

(defn item-sub [sub] (sub-suffix sub "i"))

(defmacro with-fork
  "bingings => [name sub ...]

   Creates and binds loom forks for the evaluation of a body, returning the
   accumulated triples back to the parent at the end of evaluation."
  [loom bindings & body]
  (cond
    (= (count bindings) 0) `(do ~@body)
    (symbol? (bindings 0)) `(let [~(bindings 0) (fork-loom ~loom ~(bindings 1))]
                              (with-fork ~loom ~(subvec bindings 2) ~@body)
                              (add-triples ~loom (essay-triples ~(bindings 0))))
    :else (throw (IllegalArgumentException.
                   "with-fork only allows Symbols in bindings"))))

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
         nil)
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
       (knot-essay [loom sub title fns]
         (doall (map #(% loom)
           (reduce
             (fn [result f]
               (if (contains? (meta f) :no-block)
                 (conj result f)
                 (concat result [push-block f])))
             [(first fns)] (rest fns))))
         (add-triples loom [
           (types schema-v1 sub "/essay")
           (->Triple sub "/essay/title" title {})]))
       (knot-root-menu [loom label title]
         (add-triples loom
           (->Triple (get-essay-sub loom) "/essay/flow/menu" label {"/title" title})))
       (knot-file-under [loom label]
         (add-triples loom
           (->Triple (get-essay-sub loom) "/essay/flow/home" label {"/label" :menu})))
       (knot-add-home [loom sub]
         (add-triples loom
           (->Triple (get-essay-sub loom) "/essay/flow/home" sub {})))
       (knot-footnote [loom virtual-sub fns]
         (add-triples loom
           (let [sub (if (fn? virtual-sub) (virtual-sub loom) virtual-sub)]
             (with-fork loom [loom-fork sub]
               (doall (map #(% loom-fork)
                 (reduce
                   (fn [result f]
                     (if (contains? (meta f) :no-block)
                       (conj result f)
                       (concat result [push-block f])))
                   [(first fns)] (rest fns))))))))
       (knot-paragraph [loom fns]
         (add-triples loom
           (with-fork loom [loom-fork (major-key loom)]
             (doall
               (map #(% loom-fork)
                 (reduce
                   (fn [result f]
                     (concat result [push-inline f]))
                   [(first fns)] (rest fns)))))))
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
             [(types schema-v1 sub "/segment")
              (->Triple sub "/segment/contains" item-keyword {})
              (types schema-v1 item-keyword "/item/inline")
              (->Triple item-keyword "/item/inline/text" joined-lines {})])))
       (knot-tangent [loom virtual-sub lines]
         (let [sub (if (fn? virtual-sub) (virtual-sub loom) virtual-sub),
               k (minor-key loom)]
           (knot-text loom lines)
           (add-triples loom
             (->Triple (item-sub k) "/item/inline/tangent" sub {}))))
       (knot-see-also [loom essay-sub lines]
         (let [k (minor-key loom)]
           (knot-text loom lines)
           (add-triples loom
             (->Triple (item-sub k) "/item/inline/see_also" essay-sub {}))))
       (knot-link [loom url lines]
         (let [k (minor-key loom)]
           (knot-text loom lines)
           (add-triples loom
             (->Triple (item-sub k) "/item/inline/url" url {}))))
       (knot-inline-definition [loom word word-as-written]
         (let [word-data (glossary word),
               sub (glossary-sub word),
               k (minor-key loom)]
           (knot-text loom [word-as-written])
           (add-triples loom
             (->Triple (item-sub k) "/item/inline/definition" sub {}))))
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
             (concat [(types schema-v1 block-sub "/segment"),
                      (->Triple block-sub "/segment/contains" item-sub {})]
                     (f item-sub)))))
       (knot-poem [loom lines]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/poem")
              (map
                (fn [i]
                  (let [line (nth lines i)]
                    (->Triple sub "/item/poem/line" line {"/order" i})))
                (range (count lines)))])))
       (knot-image [loom url alt-text width height]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/image")
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
           (fn [sub]
             [(types schema-v1 sub "/item/quote")
              (->Triple sub "/item/quote/text" q {})
              (if (nil? author)
                []
                (->Triple sub "/item/quote/author" author {}))])))
       (knot-big-emoji [loom emoji]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/big_emoji")
              (->Triple sub "/item/big_emoji/emoji" emoji {})])))
       (knot-q-and-a [loom q a]
         (knot-block-item loom
           (fn [sub]
             (let [q-sub (sub-suffix sub "q"),
                   a-sub (sub-suffix sub "a")],
               (with-fork loom [q-loom q-sub,
                                a-loom a-sub]
                 (q q-loom)
                 (a a-loom)
                 (add-triples loom
                   [(types schema-v1 sub "/item/q_and_a")
                    (->Triple sub "/item/q_and_a/question" q-sub {})
                    (->Triple sub "/item/q_and_a/answer" a-sub {})]))))))
       (knot-list [loom is-ordered header-or-nil items]
         (knot-block-item loom
           (fn [sub]
             (let [item-subs (map #(sub-suffix sub (str "i" %))
                                  (range (count items))),
                   header-sub (sub-suffix sub "h")]
               (doall (map #(with-fork loom [item-loom (second %)]
                              ((first %) item-loom))
                           (map vector items item-subs)))
               (concat
                 (types schema-v1 sub "/item/bullet_list")
                 (if (nil? header-or-nil)
                   []
                   (with-fork loom [header-loom header-sub]
                     (header-or-nil header-loom)
                     (add-triples header-loom
                       (->Triple sub "/item/bullet_list/header" header-sub {}))))
                 [(->Triple sub "/item/bullet_list/is_ordered" is-ordered {})]
                 (map #(->Triple sub "/item/bullet_list/point" (first %)
                                 {"/order" (second %)})
                      (map vector item-subs (range (count item-subs)))))))))
       (knot-contact-email [loom email-address]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/contact"),
              (->Triple sub "/item/contact/email" email-address {})])))
       (knot-thesis [loom line]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/thesis"),
              (->Triple sub "/item/thesis/contains" line {})])))
       (knot-matrix [loom rows columns contents]
         (knot-block-item loom
           (fn [sub]
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
                            (with-fork loom [item-loom (sub-suffix sub (gen-key 5))]
                              (if (string? val)
                                  (knot-text item-loom [val])
                                  (val item-loom))
                              (add-triples loom (gen-triple (minor-key item-loom)))))),
                   rows* (filter #(not (nil? (:val %))) (index-1d rows)),
                   columns* (filter #(not (nil? (:val %))) (index-1d columns)),
                   contents* (filter #(not (nil? (:val %))) (index-2d contents))]
               (doall (map
                 #(item (:val %)
                        (fn [obj]
                          (->Triple sub "/item/table/label" obj {"/row" (:i %)})))
                 rows*)),
               (doall (map
                 #(item (:val %)
                        (fn [obj]
                          (->Triple sub "/item/table/label" obj {"/column" (:i %)})))
                 columns*)),
               (doall (map
                 #(item (:val %)
                        (fn [obj]
                          (->Triple sub "/item/table/cell" obj {"/row" (:i %),
                                                                "/column" (:j %)})))
                 contents*))
               (types schema-v1 sub "/item/table")))))
       (knot-html-passthrough [loom html]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/raw_html")
              (->Triple sub "/item/raw_html/contains" html {})])))
       (knot-definition [loom word part-of-speech definitions]
         (knot-block-item loom
           (fn [sub]
             [(types schema-v1 sub "/item/definition"),
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
    (knot-essay t sub title fns)
    (essay-triples t)))

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

(defn add-home
  [sub]
  ^{:no-block true} (fn [t] (knot-add-home t sub)))

(defn glossary-essay
  []
  (essay :glossary "Glossary"
    (text "TODO - block-define all words alphabetically here")

    ^{:no-block true} (fn [t]
    (reduce-kv
      (fn [result k v]
        (concat result
                ((footnote (glossary-sub k)
                   (apply definition k (:pos v) (:defs v))) t)))
      [] glossary))
    (add-home :monad)
    ))
