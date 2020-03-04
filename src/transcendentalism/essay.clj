(ns transcendentalism.essay
  (:require
    [clojure.string :as str]))

(use 'transcendentalism.encoding
     'transcendentalism.graph
     'transcendentalism.html
     'transcendentalism.schema)

(defprotocol EssayThread
  (get-essay-sub [essay-thread] "Returns the top-level essay sub")
  (fork-essay-thread [essay-thread new-sub]
    "Returns a new essay thread with the same essay sub")
  (initiate [essay-thread] [essay-thread sub]
    "Adds a sub as the initial segment in an essay thread")
  (push-block [essay-thread] [essay-thread sub] "Adds a new block")
  (push-inline [essay-thread] [essay-thread sub]
    "Adds a new inline to the current block")
  (major-key [essay-thread] "Returns the current major key")
  (minor-key [essay-thread] "returns the current minor key"))

(defn create-essay-thread
  ([essay-sub] (create-essay-thread essay-sub essay-sub))
  ([essay-sub sub]
   (let [key-gen (create-key-gen sub)]
     (reify EssayThread
       (get-essay-sub [essay-thread] essay-sub)
       (fork-essay-thread [essay-thread new-sub]
         (create-essay-thread essay-sub new-sub))
       (initiate [essay-thread]
         (let [prev (prev-major-key key-gen),
               sub (push-major-key key-gen)]
           (->Triple prev "/essay/contains" sub {})))
       (initiate [essay-thread sub]
         (let [prev (prev-major-key key-gen)]
           (push-major-key key-gen sub)
           (->Triple prev "/essay/contains" sub {})))
       (push-block [essay-thread]
         (let [prev (prev-major-key key-gen),
               sub (push-major-key key-gen)]
           (->Triple prev "/segment/flow/block" sub {})))
       (push-block [essay-thread sub]
         (let [prev (prev-major-key key-gen)]
           (push-major-key key-gen sub)
           (->Triple prev "/segment/flow/block" sub {})))
       (push-inline [essay-thread]
         (let [prev (prev-minor-key key-gen),
               sub (push-minor-key key-gen)]
           (->Triple prev "/segment/flow/inline" sub {})))
       (push-inline [essay-thread sub]
         (let [prev (prev-minor-key key-gen)]
           (push-minor-key key-gen sub)
           (->Triple prev "/segment/flow/inline" sub {})))
       (major-key [essay-thread] (prev-major-key key-gen))
       (minor-key [essay-thread] (prev-minor-key key-gen))))))

(defn sub-suffix
  [sub suffix]
  (keyword (str (name sub) "-" suffix)))

(defn f
  "Returns a virtual sub that produces footnote names"
  [num]
  (fn [t]
    (sub-suffix (get-essay-sub t) (str "f" num))))

(defn root-menu
  "Marks a given essay as the root of some label"
  [label title]
  ^{:no-block true}
  (fn [t]
    (->Triple (get-essay-sub t) "/essay/flow/menu" label {"/title" title})))

(defn file-under
  "Files the given essay under some label"
  [label]
  ^{:no-block true}
  (fn [t] (->Triple (get-essay-sub t) "/essay/flow/home" label {"/label" :menu})))

(defn essay
  [sub title & fns]
  (let [t (create-essay-thread sub)]
    (flatten [
      (types schema sub "/essay")
      (->Triple sub "/essay/title" title {})
      (initiate t)
      (map #(% t)
        (reduce
          (fn [result f]
            (if (contains? (meta f) :no-block)
              (conj result f)
              (concat result [push-block f])))
          [(first fns)] (rest fns)))
    ])))

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
  ^{:no-block true} (fn [t]
    (let [sub (if (fn? virtual-sub) (virtual-sub t) virtual-sub),
          t (fork-essay-thread t sub)]
      (map #(% t)
        (reduce
          (fn [result f]
            (if (contains? (meta f) :no-block)
              (conj result f)
              (concat result [push-block f])))
          [(first fns)] (rest fns))))))

(defn item-sub [sub] (sub-suffix sub "i"))

(defn block-item
  "Given a function that takes a sub and produces triples, adds the necessary
   triples to make it a block item"
  [f]
  (fn [t]
    (let [block-sub (major-key t),
          item-sub (item-sub block-sub)]
      (concat [(types schema block-sub "/segment"),
               (->Triple block-sub "/segment/contains" item-sub {})]
              (f t item-sub)))))

(defn poem
  [& lines]
  (block-item
    (fn [t sub]
      [(types schema sub "/item/poem")
       (map
         (fn [i]
           (let [line (nth lines i)]
             (->Triple sub "/item/poem/line" line {"/order" i})))
         (range (count lines)))])))

(defn image
  ([url alt-text] (image url alt-text nil nil))
  ([url alt-text width height]
   (block-item
     (fn [t sub]
       [(types schema sub "/item/image")
        (->Triple sub "/item/image/url" url {})
        (->Triple sub "/item/image/alt_text" alt-text {})
        (if (nil? width)
          []
          (->Triple sub "/item/image/width" width {}))
        (if (nil? height)
          []
          (->Triple sub "/item/image/height" height {}))]))))

(defn quote*
  ([q] (quote* q nil))
  ([q author]
    (block-item
      (fn [t sub]
        [(types schema sub "/item/quote")
         (->Triple sub "/item/quote/text" q {})
         (if (nil? author)
           []
           (->Triple sub "/item/quote/author" author {}))]))))

(defn big-emoji
  [emoji]
  (block-item
    (fn [t sub]
      [(types schema sub "/item/big_emoji")
       (->Triple sub "/item/big_emoji/emoji" emoji {})])))

(defn paragraph
  [& fns]
  (fn [t]
    (let [t (fork-essay-thread t (major-key t))]
      (map #(% t)
        (reduce
          (fn [result f]
            (concat result [push-inline f]))
          [(first fns)] (rest fns))))))

(defn text
  [& lines]
  (fn [t]
    (let [sub (minor-key t),
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
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword {})
       (types schema item-keyword "/item/inline")
       (->Triple item-keyword "/item/inline/text" joined-lines {})])))

(defn tangent
  [virtual-sub & lines]
  (fn [t]
    (let [sub (if (fn? virtual-sub) (virtual-sub t) virtual-sub),
          k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/tangent" sub {})])))

(defn see-also
  [essay-sub & lines]
  (fn [t]
    (let [k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/see_also" essay-sub {})])))

(defn link
  [url & lines]
  (fn [t]
    (let [k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/url" url {})])))

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
  (block-item
    (fn [t sub]
      (let [q-sub (sub-suffix sub "q"),
            a-sub (sub-suffix sub "a")]
        [(q (fork-essay-thread t q-sub))
         (a (fork-essay-thread t a-sub))
         (types schema sub "/item/q_and_a")
         (->Triple sub "/item/q_and_a/question" q-sub {})
         (->Triple sub "/item/q_and_a/answer" a-sub {})]))))

(defn- inner-bullet-list
  [t sub is-ordered header-or-nil & items]
  (let [item-subs (map #(sub-suffix sub (str "i" %))
                       (range (count items))),
        header-sub (sub-suffix sub "h")]
    (concat
      (types schema sub "/item/bullet_list")
      (if (nil? header-or-nil)
        []
        [(header-or-nil (fork-essay-thread t header-sub))
         (->Triple sub "/item/bullet_list/header" header-sub {})])
      (if is-ordered
        [(->Triple sub "/item/bullet_list/is_ordered" true {})]
        [])
      (map #((first %) (fork-essay-thread t (second %)))
           (map vector items item-subs))
      (map #(->Triple sub "/item/bullet_list/point" (first %)
                      {"/order" (second %)})
           (map vector item-subs (range (count item-subs)))))))

(defn bullet-list
  [header-or-nil & items]
  (block-item #(apply inner-bullet-list %1 %2 false header-or-nil items)))

(defn numbered-list
  [header-or-nil & items]
  (block-item #(apply inner-bullet-list %1 %2 true header-or-nil items)))

(defn contact-email
  [email-address]
  (block-item
    (fn [t sub]
      [(types schema sub "/item/contact"),
       (->Triple sub "/item/contact/email" email-address {})])))

(defn matrix
  [rows columns contents]
  (block-item
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
                     (let [t (fork-essay-thread t (sub-suffix sub (gen-key 5)))]
                       (conj (if (string? val)
                                 ((text val) t)
                                 (val t))
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

(defn credit
  "Adds /credit property to all /item/segments produced by some function"
  ([f] (credit "Anonymous" f))
  ([whom f]
    (fn [t]
      (let [triples (flatten (f t)),
            subs (map :sub
                      (filter #(= (:pred %) "/type/segment") triples))]
        (concat
          triples
          (map #(->Triple % "/segment/author" whom {}) subs))))))

(defn html-passthrough
  "Takes some HTML, and passes it straight-through, effectively by-passing the
   schema layer. This should only be used for bespoke content."
   [html]
   (block-item
    (fn [t sub]
      [(types schema sub "/item/raw_html")
       (->Triple sub "/item/raw_html/contains" html {})])))
