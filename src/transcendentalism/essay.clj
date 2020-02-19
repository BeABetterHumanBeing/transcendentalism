(ns transcendentalism.essay
  (:require [clojure.string :as str]))

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

(defn poem
  [& lines]
  (fn [t]
    (let [sub (major-key t),
          item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/poem")
       (map
         (fn [i]
           (let [line (nth lines i)]
             (->Triple item-keyword "/item/poem/line" ^{:order i} [line])))
         (range (count lines)))])))

(defn image
  [url alt-text]
  (fn [t]
    (let [sub (major-key t),
          item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/image")
       (->Triple item-keyword "/item/image/url" url)
       (->Triple item-keyword "/item/image/alt_text" alt-text)])))

(defn quote*
  ([q] (quote* q nil))
  ([q author]
    (fn [t]
      (let [sub (major-key t),
            item-keyword (item-sub sub)]
        [(types schema sub "/segment")
         (->Triple sub "/segment/contains" item-keyword)
         (types schema item-keyword "/item/quote")
         (->Triple item-keyword "/item/quote/text" q)
         (if (nil? author)
           []
           (->Triple item-keyword "/item/quote/author" author))]))))

(defn big-emoji
  [emoji]
  (fn [t]
    (let [sub (major-key t),
          item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/big_emoji")
       (->Triple item-keyword "/item/big_emoji/emoji" emoji)])))

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

(defn q-and-a
  [q a]
  (fn [t]
    (let [sub (major-key t),
          item-keyword (item-sub sub),
          q-sub (sub-suffix sub "q"),
          a-sub (sub-suffix sub "a"),
          q-t (create-essay-thread q-sub),
          a-t (create-essay-thread a-sub)]
      [(q q-t)
       (a a-t)
       (types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/q_and_a")
       (->Triple item-keyword "/item/q_and_a/question" q-sub)
       (->Triple item-keyword "/item/q_and_a/answer" a-sub)])))

(defn bullet-list
  [& items]
  (fn [t]
    (map
      #(% t)
      (reduce
        (fn [result f]
          (concat result [push-block f]))
        [(first items)] (rest items)))))

