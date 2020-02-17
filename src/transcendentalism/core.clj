(ns transcendentalism.core
  (:require [clojure.string :as str]))

(use
  'transcendentalism.encoding
  'transcendentalism.generate
  'transcendentalism.graph
  'transcendentalism.schema
  'transcendentalism.svg)

(defprotocol EssayThread
  (initiate [essay-thread sub] "Adds a sub as the initial segment in an essay thread")
  (push-block [essay-thread sub] "Adds a new block"))

(defn- create-essay-thread
  [essay-sub]
  (let [key-gen (create-key-gen essay-sub)]
    (reify EssayThread
      (initiate [essay-thread sub]
        (let [prev (prev-key key-gen)]
          (push-key key-gen sub)
          (->Triple prev "/essay/contains" sub)))
      (push-block [essay-thread sub]
        (let [prev (prev-key key-gen)]
          (push-key key-gen sub)
          (->Triple prev "/segment/flow/block" sub))))))

(defn- apply-directives
  "Processes collections of triples, applying any directives found therein"
  [& colls]
  (let [groups (group-by #(instance? transcendentalism.graph.Triple %) (flatten colls)),
        triples (groups true),
        directives (groups false)]
    (reduce
      (fn [result directive] (directive result))
      triples directives)))

(defn- directive-under-construction
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

(defn- essay
  [sub title f]
  (let [essay-thread (create-essay-thread sub)]
    (flatten [
      (types schema sub "/essay")
      (->Triple sub "/essay/title" title)
      (f essay-thread)
    ])))

(defn- essay-series
  "Adds triples connecting a series of essay segments. The first segment will
   be used as the home for the rest of the series."
  [subs]
  (let [home (first subs)]
    (concat
      (into [] (map #(->Triple % "/essay/flow/home" home) (rest subs)))
      (into [] (map #(->Triple
                      (get subs %) "/essay/flow/next" (get subs (inc %)))
                    (range (dec (count subs))))))))

(defn- decoration?
  "Returns the decoration of a sentence fragment"
  [fragment]
  (if (string? fragment)
    :string
    (fragment :decoration)))

(defn- get-fragment
  "Returns the sentence fragment itself"
  [fragments]
  (if (string? (first fragments))
    (str/join " " fragments)
    ((first fragments) :content)))

(defn- get-footnote
  "Returns the footnote to which the fragment is linked, or nil"
  [fragments]
  (if (string? (first fragments))
    nil
    ((first fragments) :footnote)))

(defn- tangent
  "Decorates a sentence fragment, associating it with a footnote"
  [footnote-sub & fragment]
  {
    ; Tangents should never have the same decoration as their immediate neighbor.
    :decoration (rand-int 1000000),
    :content (str/join " " fragment),
    :footnote footnote-sub,
  })

(defn- text
  "Converts a collection of [potentially decorated] sentence fragments into
   equivalent triples"
  [sub & fragments]
  (let [texts (partition-by #(decoration? %) fragments)]
    (concat
      (types schema sub "/item/text")
      (map
        (fn [i]
          (let [piece (nth texts i),
                footnote (get-footnote piece),
                metadata (if (nil? footnote)
                           {:order i}
                           {:order i,
                            :footnote footnote})]
            (->Triple sub "/item/text/text"
              (with-meta [(get-fragment piece)] metadata))))
        (range (count texts))))))

(defn- footnote
  "Creates text along with an accompanying footnote marker"
  [sub footnote-sub & fragments]
  (conj
    (apply text footnote-sub fragments)
    (->Triple sub "/item/footnote" footnote-sub)))

(defn- poem-segment
  [sub & lines]
  (let [item-keyword (keyword (str (name sub) "-i"))]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/poem")
     (map
       (fn [i]
         (let [line (nth lines i)]
           (->Triple item-keyword "/item/poem/line" ^{:order i} [line])))
       (range (count lines)))]))

(defn- image-segment
  [sub url alt-text]
  (let [item-keyword (keyword (str (name sub) "-i"))]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/image")
     (->Triple item-keyword "/item/image/url" url)
     (->Triple item-keyword "/item/image/alt_text" alt-text)]))

(defn- quote-segment
  ([sub quote] (quote-segment sub quote nil))
  ([sub quote author]
    (let [item-keyword (keyword (str (name sub) "-i"))]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/quote")
       (->Triple item-keyword "/item/quote/text" quote)
       (if (nil? author)
         []
         (->Triple item-keyword "/item/quote/author" author))])))

(defn- big-emoji-segment
  [sub emoji]
  (let [item-keyword (keyword (str (name sub) "-i"))]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/big_emoji")
     (->Triple item-keyword "/item/big_emoji/emoji" emoji)]))

(defn- text-segment
  [sub & lines]
  (let [item-keyword (keyword (str (name sub) "-i"))]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/inline")
     (->Triple item-keyword "/item/inline/text" (str/join " " lines))]))

(def intro-essay-sequence
  [(essay-series [:monad :welcome :i-am-dan :connections :apologies])
   (directive-under-construction :connections :apologies)])

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  (essay :monad "Transcendental Metaphysics" (fn [t] [
    (initiate t :monad-image)
    (image-segment :monad-image
      (svg-to-image "monad" 800 800 'svg-monad)
      "Animation of the star flower, with changes cascading inwards to a central point")

    (push-block t :monad-intro-quote)
    (quote-segment :monad-intro-quote
      (clojure.string/join " "
        ["The Monad is the symbol of unity."
         "It is the godhead, the point from which all things originate,"
         "and the point to which all things return."])
      "Daniel Gierl")

    ; The monad is the only segment whose home is reflexive.
    (->Triple :monad "/essay/flow/home" :monad)
    ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
  ])))

; About
; This sequence of essay_segments say a little about myself, this website, and
; what I hope to do with it.
(def welcome
  (essay :welcome "Welcome" (fn [t] [
    (initiate t :wave-emoji)
    (big-emoji-segment :wave-emoji "&#x1f44b")

    (push-block t :welcome-1)
    (text-segment :welcome-1
      "Hi there! I'm Daniel Gierl, and I'd like to welcome you to my personal"
      "website, Transcendental Metaphysics! I use this space to explore"
      "questions of philosophy, religion, politics, you name it. It is my"
      "sincere hope that you leave feeling enriched by the experience, and that"
      "the time you spend here is time well spent.")

    (push-block t :welcome-2)
    (text-segment :welcome-2
      "I apologize in advance for any issues you may encounter with the"
      "unorthodox structure of the site; I've been using it as a playground"
      "for some of the more experimental ideas I've been toying with. I wrote the"
      "whole thing")
    ; (->Triple :welcome-2 "/segment/flow/inline" :tangent-1)
    ; (text-segment :tangent-1 "from scratch") ; tangent to footnote-1
    ; (->Triple :tangent-1 "/segment/flow/inline" :welcome-2-1)
    ; (text-segment :welcome-2-1
    ;   "and, as a backend engineer, this was a recipe for, ummm, how shall we"
    ;   "say, *curious* frontend design choices.")

    ; (footnote :welcome :footnote-1
    ;   "In clojure, no less. I used it as an opportunity to teach myself"
    ;   "the language. There is no learning quite like doing.")

    (push-block t :welcome-3)
    (text-segment :welcome-3
      "The whole site is structured as a big, tangled graph. What you're"
      "reading here is as close to a proper 'beginning' as it gets, and there"
      "is nothing out there that resembles an 'end'. My intention is that"
      "wandering through these pages will be an experience not unlike wandering"
      "through a garden maze; getting lost is half the fun, and there is all"
      "kinds of")
    ;   (tangent :footnote-2 "treasure")
    ;   "hidden away for you to find.")
    ; (footnote :welcome :footnote-2
    ;   "As an aside, I have tried to make the URLs somewhat stable so that they"
    ;   "can be shared and saved, but I can only guarantee a modicum of stability"
    ;   "in a shifting sea of ideas.")

    (push-block t :welcome-4)
    (poem-segment :welcome-4
      "May you find that which you search for"
      "May your bridges meet you halfway"
      "May you never lose yourself in darkness"
      "And may the light of God shine brightly on your soul")

    (push-block t :praying-hands-emoji)
    (big-emoji-segment :praying-hands-emoji "&#x1f44b")
  ])))

(def i-am-dan
  (essay :i-am-dan "I Am Dan" (fn [t] [
    (initiate t :yellow-socks)
    (text-segment :yellow-socks
      "My name's Daniel Gierl. I was a plump, healthy baby with a full head of"
      "hair, and I went home from the hospital wearing little yellow socks.")

    ; ; TODO yellow socks (preferably a photo)

    (push-block t :salient-details)
    (text-segment :salient-details
      "In life, we often overlook the smaller, sentimental details. A person is"
      "readily reduced to their age and occupation, their parentage, their"
      "claim to fame... There's nothing wrong with this; it happens for a"
      "reason. If you're only going to remember one thing about a person, it"
      "makes sense to remember just their most salient trait. Sometimes, the"
      "salient trait isn't even what they would think is noteworthy: it's not"
      "uncommon in the annals of history to find a person whose existence is"
      "remembered solely because of the consequences of some rash action or"
      "small act of kindness. The passions, trials, and triumphs that they"
      "occupied their lives with are forgotten on account of being a bit too"
      "mundane, a bit too undocumented.")

    (push-block t :prefer-socks)
    (text-segment :prefer-socks
      "I do have a diary, but it's not on this website. If you read enough of"
      "this, you will surely get a great sense of who I am, but it's not"
      "*really* supposed to be about me. Given this opportunity to toot my own"
      "horn, I'd much rather be known as \"the guy who went home from the"
      "hospital in yellow socks\" than anything else.")
    ])))

(def connections
  (essay :connections "Connections" (fn [t] [
    (initiate t :todo-connections)
    (text-segment :todo-connections "TODO connections")
    ; TODO(gierl) contact information, respective responsibilities
  ])))

(def apologies
  (essay :apologies "Apologies" (fn [t] [
    (initiate t :todo-apologies)
    (text-segment :todo-apologies "TODO apologies")
    ; TODO(gierl) apologize
  ])))

(def graph
  (construct-graph
    (apply-directives
      intro-essay-sequence monad welcome i-am-dan connections apologies)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
