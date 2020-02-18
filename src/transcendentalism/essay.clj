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
  [sub title f]
  (let [t (create-essay-thread sub)]
    (flatten [
      (types schema sub "/essay")
      (->Triple sub "/essay/title" title)
      (initiate t)
      (f t)
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
  [sub f]
  (let [essay-thread (create-essay-thread sub)]
    (f essay-thread)))

(defn- item-sub
  [sub]
  (keyword (str (name sub) "-i")))

(defn poem-segment
  [t & lines]
  (let [sub (major-key t),
        item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/poem")
     (map
       (fn [i]
         (let [line (nth lines i)]
           (->Triple item-keyword "/item/poem/line" ^{:order i} [line])))
       (range (count lines)))]))

(defn image-segment
  [t url alt-text]
  (let [sub (major-key t),
        item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/image")
     (->Triple item-keyword "/item/image/url" url)
     (->Triple item-keyword "/item/image/alt_text" alt-text)]))

(defn quote-segment
  ([t quote] (quote-segment t quote nil))
  ([t quote author]
    (let [sub (major-key t),
          item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/quote")
       (->Triple item-keyword "/item/quote/text" quote)
       (if (nil? author)
         []
         (->Triple item-keyword "/item/quote/author" author))])))

(defn big-emoji-segment
  [t emoji]
  (let [sub (major-key t),
        item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/big_emoji")
     (->Triple item-keyword "/item/big_emoji/emoji" emoji)]))

(defn text-segment
  [sub & lines]
  (let [item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/inline")
     (->Triple item-keyword "/item/inline/text" (str/join " " lines))]))

(defn tangent-segment
  [t footnote-sub & lines]
  (let [k (minor-key t)]
    [(apply text-segment k lines)
     (->Triple (item-sub k) "/item/inline/tangent" footnote-sub)]))