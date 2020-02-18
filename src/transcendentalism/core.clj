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
  (push-block [essay-thread sub] "Adds a new block")
  (push-inline [essay-thread sub] "Adds a new inline to the current block"))

(defn- create-essay-thread
  [essay-sub]
  (let [key-gen (create-key-gen essay-sub)]
    (reify EssayThread
      (initiate [essay-thread sub]
        (let [prev (prev-major-key key-gen)]
          (push-major-key key-gen sub)
          (->Triple prev "/essay/contains" sub)))
      (push-block [essay-thread sub]
        (let [prev (prev-major-key key-gen)]
          (push-major-key key-gen sub)
          (->Triple prev "/segment/flow/block" sub)))
      (push-inline [essay-thread sub]
        (let [prev (prev-minor-key key-gen)]
          (push-minor-key key-gen sub)
          (->Triple prev "/segment/flow/inline" sub))))))

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

(defn- footnote
  [sub f]
  (let [essay-thread (create-essay-thread sub)]
    (f essay-thread)))

(defn- item-sub
  [sub]
  (keyword (str (name sub) "-i")))

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
  (let [item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/image")
     (->Triple item-keyword "/item/image/url" url)
     (->Triple item-keyword "/item/image/alt_text" alt-text)]))

(defn- quote-segment
  ([sub quote] (quote-segment sub quote nil))
  ([sub quote author]
    (let [item-keyword (item-sub sub)]
      [(types schema sub "/segment")
       (->Triple sub "/segment/contains" item-keyword)
       (types schema item-keyword "/item/quote")
       (->Triple item-keyword "/item/quote/text" quote)
       (if (nil? author)
         []
         (->Triple item-keyword "/item/quote/author" author))])))

(defn- big-emoji-segment
  [sub emoji]
  (let [item-keyword (item-sub sub)]
    [(types schema sub "/segment")
     (->Triple sub "/segment/contains" item-keyword)
     (types schema item-keyword "/item/big_emoji")
     (->Triple item-keyword "/item/big_emoji/emoji" emoji)]))

(defn- text-segment
  [sub & lines]
  (let [item-keyword (item-sub sub)]
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
    (push-inline t :tangent-1)
    (text-segment :tangent-1 "from scratch")
    (->Triple (item-sub :tangent-1) "/item/inline/tangent" :footnote-1)
    (push-inline t :welcome-2-1)
    (text-segment :welcome-2-1
      "and, as a backend engineer, this was a recipe for, ummm, how shall we"
      "say, *curious* frontend design choices.")

    (footnote :footnote-1 (fn [t] [
      (text-segment :footnote-1
        "In clojure, no less. I used it as an opportunity to teach myself"
        "the language. There is no learning quite like doing.")
    ]))

    (push-block t :welcome-3)
    (text-segment :welcome-3
      "The whole site is structured as a big, tangled graph. What you're"
      "reading here is as close to a proper 'beginning' as it gets, and there"
      "is nothing out there that resembles an 'end'. My intention is that"
      "wandering through these pages will be an experience not unlike wandering"
      "through a garden maze; getting lost is half the fun, and there is all"
      "kinds of")
    (push-inline t :tangent-2)
    (text-segment :tangent-2 "treasure")
    (->Triple (item-sub :tangent-2) "/item/inline/tangent" :footnote-2)
    (push-inline t :welcome-3-1)
    (text-segment :welcome-3-1 "hidden away for you to find.")

    (footnote :footnote-2 (fn [t] [
      (text-segment :footnote-2
        "As an aside, I have tried to make the URLs somewhat stable so that they"
        "can be shared and saved, but I can only guarantee a modicum of stability"
        "in a shifting sea of ideas.")
    ]))

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
      "I do have a diary, but it's not on this website. If you read")
    (push-inline t :tangent-3)
    (text-segment :tangent-3 "enough of this")
    (->Triple (item-sub :tangent-3) "/item/inline/tangent" :footnote-3)
    (push-inline t :prefer-socks-1)
    (text-segment :prefer-socks-1", you will surely get a great sense of who I am, but it's not"
      "*really* supposed to be about me. Given this opportunity to toot my own"
      "horn, I'd much rather be known as \"the guy who went home from the"
      "hospital in yellow socks\" than anything else.")

    (footnote :footnote-3 (fn [t] [
      (text-segment :footnote-3
        "I tell a lot of stories, so it won't be that hard, if you can put up"
        "with my")
      (push-inline t :sub-tangent)
      (text-segment :sub-tangent
        "rambling")
      (->Triple (item-sub :sub-tangent) "/item/inline/tangent" :sub-footnote)
      (push-inline t :footnote-3-1)
      (text-segment :footnote-3-1 ".")

      (footnote :sub-footnote (fn [t] [
        (text-segment :sub-footnote
          "I'm partial to tangents, and tangents that go on tangents. The deep"
          "end of the pool is")
        (push-inline t :sub-sub-tangent)
        (text-segment :sub-sub-tangent "deeper")
        (->Triple (item-sub :sub-sub-tangent) "/item/inline/tangent" :sub-sub-footnote)
        (push-inline t :sub-footnote-1)
        (text-segment :sub-footnote-1 "than you might expect.")

        (footnote :sub-sub-footnote (fn [t] [
          (text-segment :sub-sub-footnote "Hahahaha")
          (->Triple (item-sub :sub-sub-footnote) "/item/inline/tangent" :sub-sub-sub-footnote)

          (footnote :sub-sub-sub-footnote (fn [t] [
            (text-segment :sub-sub-sub-footnote
              "It's worth noting that my tangents generally have a purpose."
              "This one happens to be for testing purposes, to allow me to"
              "check that deeply-nested tangents render correctly.")
          ]))
        ]))
      ]))

      (push-block t :adjectives)
      (text-segment :adjectives
        "To reward your patience so far, here's some more about me: back in"
        "2010, I chose three adjectives that I thought described myself, and"
        "were moreover things that I liked about me. They were: interesting,"
        "enthusiastic, and")
      (push-inline t :adjectives-tangent)
      (text-segment :adjectives-tangent "lovable")
      (->Triple (item-sub :adjectives-tangent) "/item/inline/tangent" :adjectives-footnote)
      (push-inline t :adjectives-1)
      (text-segment :adjectives-1
        ". Later on, in 2014, I expanded this list again, adding brave,"
        "compassionate, and just. It has been an excellent exercise in living"
        "a virtuous life, and I'd generally recommend that you try it, if it"
        "seems to be your cup of tea. But be warned: you should always expect"
        "to be tested over your principles; such badges are not won without"
        "work.")

      (footnote :adjectives-footnote (fn [t] [
        (text-segment :adjectives-footnote
          "I have since upgraded it to 'loving', to emphasize that it's more"
          "about what you give than what you receive.")
      ]))
    ]))
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
