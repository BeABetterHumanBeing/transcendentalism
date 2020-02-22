(ns transcendentalism.essay
  (:require
    [clojure.set :as set]
    [clojure.string :as str]))

(use 'transcendentalism.encoding
     'transcendentalism.graph
     'transcendentalism.schema)

(defprotocol EssayThread
  (initiate [essay-thread] [essay-thread sub]
    "Adds a sub as the initial segment in an essay thread")
  (push-block [essay-thread] [essay-thread sub] "Adds a new block")
  (push-inline [essay-thread] [essay-thread sub]
    "Adds a new inline to the current block")
  (major-key [essay-thread] "Returns the current major key")
  (minor-key [essay-thread] "returns the current minor key"))

(defn- create-essay-thread
  [essay-sub]
  (let [key-gen (create-key-gen essay-sub)]
    (reify EssayThread
      (initiate [essay-thread]
        (let [prev (prev-major-key key-gen),
              sub (push-major-key key-gen)]
          (->Triple prev "/essay/contains" sub)))
      (initiate [essay-thread sub]
        (let [prev (prev-major-key key-gen)]
          (push-major-key key-gen sub)
          (->Triple prev "/essay/contains" sub)))
      (push-block [essay-thread]
        (let [prev (prev-major-key key-gen),
              sub (push-major-key key-gen)]
          (->Triple prev "/segment/flow/block" sub)))
      (push-block [essay-thread sub]
        (let [prev (prev-major-key key-gen)]
          (push-major-key key-gen sub)
          (->Triple prev "/segment/flow/block" sub)))
      (push-inline [essay-thread]
        (let [prev (prev-minor-key key-gen),
              sub (push-minor-key key-gen)]
          (->Triple prev "/segment/flow/inline" sub)))
      (push-inline [essay-thread sub]
        (let [prev (prev-minor-key key-gen)]
          (push-minor-key key-gen sub)
          (->Triple prev "/segment/flow/inline" sub)))
      (major-key [essay-thread] (prev-major-key key-gen))
      (minor-key [essay-thread] (prev-minor-key key-gen)))))

(defn apply-directives
  "Processes collections of triples, applying any directives found therein"
  [& colls]
  (let [groups (group-by #(instance? transcendentalism.graph.Triple %) (flatten colls)),
        triples (groups true),
        directives (groups false)]
    (reduce
      (fn [result directive] (directive result))
      triples directives)))

(defn directive-under-construction
  "Returns a directive that labels the given subs as under construction"
  [& sub]
  (let [subs (into #{} sub)]
    (fn [triples]
      (concat
        (filter #(or (not (contains? subs (:sub %)))
                     (not (str/starts-with? (:pred %) "/essay/flow"))
                     (= (:pred %) "/essay/flow/home")) triples)
        (into [] (map #(->Triple % "/essay/label" :under-construction) subs))
        (into [] (map #(->Triple % "/essay/flow/see_also" :connections) subs))))))

(defn directive-see-also
  "For every /item/inline/see_also, adds an equivalent /essay/flow/see_also"
  [triples]
  (let [graph (construct-graph triples),
        essay-subs (map :sub (filter #(= (:pred %) "/type/essay") triples))]
    (reduce
      (fn [result essay-sub]
        (let [see_alsos
              ((q-chain
                 (q-pred "/essay/contains")
                 gq-segment-to-item
                 gq-item-to-item
                 ; Because the see_also link may be buried in a tangent...
                 (q-kleene
                   (q-chain
                     (q-pred "/item/inline/tangent")
                     gq-segment-to-item
                     gq-item-to-item))
                 (q-pred "/item/inline/see_also")
                 )
               graph #{essay-sub})]
          (concat result
            (into []
              (map #(->Triple essay-sub "/essay/flow/see_also" %)
                   see_alsos)))))
      triples essay-subs)))

(defn directive-dedup-cxns
  "De-dupes /essay/flow triples that have the same sub and obj"
  [triples]
  (let [cxns (filter #(str/starts-with? (:pred %) "/essay/flow") triples),
        sub-to-cxns (index-by-sub cxns),
        dupes
        (into #{}
          (reduce-kv
            (fn [result sub cxns]
              (let [obj-to-cxns (index-by-obj cxns)]
                (set/union
                  result
                  (reduce-kv
                    (fn [result obj cxns]
                      (if (> (count cxns) 1)
                        (let [cxn-preds (vec (map :pred cxns)),
                              highest-priority-pred
                              (if (contains? cxn-preds "/essay/flow/home")
                                "/essay/flow/home"
                                "/essay/flow/next")]
                          (set/union
                            result
                            (filter #(not (= (:pred %) highest-priority-pred))
                                    cxns)))
                        result))
                    #{} obj-to-cxns))))
            #{} sub-to-cxns))]
    (filter #(not (contains? dupes %)) triples)))

(defn essay
  [sub title & fns]
  (let [t (create-essay-thread sub)]
    (flatten [
      (types schema sub "/essay")
      (->Triple sub "/essay/title" title)
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
      (into [] (map #(->Triple % "/essay/flow/home" home) (rest subs)))
      (into [] (map #(->Triple
                      (get subs %) "/essay/flow/next" (get subs (inc %)))
                    (range (dec (count subs))))))))

(defn footnote
  [sub & fns]
  ^{:no-block true} (fn [t]
    (let [t (create-essay-thread sub)]
      (map #(% t)
        (reduce
          (fn [result f]
            (if (contains? (meta f) :no-block)
              (conj result f)
              (concat result [push-block f])))
          [(first fns)] (rest fns))))))

(defn- sub-suffix
  [sub suffix]
  (keyword (str (name sub) "-" suffix)))

(defn- item-sub [sub] (sub-suffix sub "i"))

(defn- block-item
  "Given a function that takes a sub and produces triples, adds the necessary
   triples to make it a block item"
  [f]
  (fn [t]
    (let [block-sub (major-key t),
          item-sub (item-sub block-sub)]
      (concat [(types schema block-sub "/segment"),
               (->Triple block-sub "/segment/contains" item-sub)]
              (f item-sub)))))

(defn poem
  [& lines]
  (block-item
    (fn [sub]
      [(types schema sub "/item/poem")
       (map
         (fn [i]
           (let [line (nth lines i)]
             (->Triple sub "/item/poem/line" ^{:order i} [line])))
         (range (count lines)))])))

(defn image
  [url alt-text]
  (block-item
    (fn [sub]
      [(types schema sub "/item/image")
       (->Triple sub "/item/image/url" url)
       (->Triple sub "/item/image/alt_text" alt-text)])))

(defn quote*
  ([q] (quote* q nil))
  ([q author]
    (block-item
      (fn [sub]
        [(types schema sub "/item/quote")
         (->Triple sub "/item/quote/text" q)
         (if (nil? author)
           []
           (->Triple sub "/item/quote/author" author))]))))

(defn big-emoji
  [emoji]
  (block-item
    (fn [sub]
      [(types schema sub "/item/big_emoji")
       (->Triple sub "/item/big_emoji/emoji" emoji)])))

(defn paragraph
  [& fns]
  (fn [t]
    (let [t (create-essay-thread (major-key t))]
      (map #(% t)
        (reduce
          (fn [result f]
            (concat result [push-inline f]))
          [(first fns)] (rest fns))))))

(defn text
  [& lines]
  (fn [t]
    (let [sub (minor-key t),
          item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/inline")
       (->Triple item-keyword "/item/inline/text" (str/join " " lines))])))

(defn tangent
  [footnote-sub & lines]
  (fn [t]
    (let [k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/tangent" footnote-sub)])))

(defn see-also
  [essay-sub & lines]
  (fn [t]
    (let [k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/see_also" essay-sub)])))

(defn link
  [url & lines]
  (fn [t]
    (let [k (minor-key t)]
      [((apply text lines) t)
       (->Triple (item-sub k) "/item/inline/url" url)])))

(defn q-and-a
  [q a]
  (block-item
    (fn [sub]
      (let [q-sub (sub-suffix sub "q"),
            a-sub (sub-suffix sub "a")]
        [(q (create-essay-thread q-sub))
         (a (create-essay-thread a-sub))
         (types schema sub "/item/q_and_a")
         (->Triple sub "/item/q_and_a/question" q-sub)
         (->Triple sub "/item/q_and_a/answer" a-sub)]))))

(defn bullet-list
  [header-or-nil & items]
  (block-item
    (fn [sub]
      (let [item-subs (map #(sub-suffix sub (str "i" %))
                           (range (count items))),
            header-sub (sub-suffix sub "h")]
        (concat
          (types schema sub "/item/bullet_list")
          (if (nil? header-or-nil)
            []
            [(header-or-nil (create-essay-thread header-sub))
             (->Triple sub "/item/bullet_list/header" header-sub)])
          (map #((first %) (create-essay-thread (second %)))
               (map vector items item-subs))
          (map #(->Triple sub
                          "/item/bullet_list/point"
                          ^{:order (second %)} [(first %)])
               (map vector item-subs (range (count item-subs)))))))))

(defn contact-email
  [email-address]
  (block-item
    (fn [sub]
      [(types schema sub "/item/contact"),
       (->Triple sub "/item/contact/email" email-address)])))
