(ns transcendentalism.core
  (:require [clojure.string :as str]))

(use
  'transcendentalism.generate
  'transcendentalism.graph
  'transcendentalism.schema
  'transcendentalism.svg)

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
  "Adds triples to create an essay"
  [sub title content-subs]
  (let [contents-sub (keyword (str (name sub) "-contents"))]
    [(types sub "/essay")
     (->Triple sub "/essay/contains" contents-sub)
     (->Triple sub "/essay/title" title)
     (types contents-sub "/item/ordered_set")
     (map #(->Triple contents-sub
                     "/item/contains"
                     ^{:order %} [(get content-subs %)])
          (range (count content-subs)))]))

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

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  (flatten [
    (essay :monad "Transcendental Metaphysics" [:monad-image :monad-intro-quote])
    (types :monad-image "/item/image")
    (->Triple :monad-image "/item/image/url" (svg-to-image "monad" 800 800 'svg-monad))
    (->Triple :monad-image "/item/image/alt_text"
      "Animation of the star flower, with changes cascading inwards to a central point")
    (types :monad-intro-quote "/item/quote")
    (->Triple :monad-intro-quote "/item/quote/text"
      (clojure.string/join " "
        ["The Monad is the symbol of unity."
        "It is the godhead, the point from which all things originate,"
        "and the point to which all things return."]))
    (->Triple :monad-intro-quote "/item/quote/author" "Daniel Gierl")
    ; The monad is the only segment whose home is reflexive.
    (->Triple :monad "/essay/flow/home" :monad)
    ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
   ]))

; About
; This sequence of essay_segments say a little about myself, this website, and
; what I hope to do with it.
(def about
  (flatten [
    (essay-series [:monad :welcome :i-am-dan :connections :apologies])
    (directive-under-construction :i-am-dan :connections :apologies)

    ; Welcome
    (essay :welcome "Welcome" [
      :wave-emoji :welcome-1 :welcome-2 :footnote-1 :welcome-3 :footnote-2
      :welcome-4 :praying-hands-emoji])

    (types :wave-emoji "/item/big_emoji")
    (->Triple :wave-emoji "/item/big_emoji/emoji" "&#x1f44b")

    (types :welcome-1 "/item/text")
    (->Triple :welcome-1 "/item/text/text"
      (str/join " " [
        "Hi there! I'm Daniel Gierl, and I'd like to welcome you to my personal"
        "website, Transcendental Metaphysics! I use this space to explore"
        "questions of philosophy, religion, politics, you name it. It is my"
        "sincere hope that you leave feeling enriched by the experience, and"
        "that the time you spend here is time well spent."
      ]))

    (types :welcome-2 "/item/text")
    (->Triple :welcome-2 "/item/text/text"
      ^{:order 0}
      [(str/join " " [
        "I apologize in advance for any issues you may encounter with the"
        "unorthodox structure of the site; I've been using it as a playground"
        "for some of the more experimental ideas I've been toying with. I wrote the"
        "whole thing"
      ])])
    (->Triple :welcome-2 "/item/text/text"
      ^{:order 1}
      ["from scratch [1],"])
    (->Triple :welcome-2 "/item/text/text"
      ^{:order 2}
      [(str/join " " [
        "and as a backend engineer, this was a recipe for, ummm, how shall we"
        "say, *curious* frontend design choices."
      ])])

    (types :footnote-1 "/item/text")
    (->Triple :footnote-1 "/item/text/text"
      (str/join " " [
        "[1] In clojure, no less. I used it as an opportunity to teach myself"
        "the language. There is no learning quite like doing."
      ]))

    (types :welcome-3 "/item/text")
    (->Triple :welcome-3 "/item/text/text"
      ^{:order 0}
      [(str/join " " [
        "The whole site is structured as a big, tangled graph. What you're"
        "reading here is as close to a proper 'beginning' as it gets, and there"
        "is nothing out there that resembles an 'end'. My intention is that"
        "wandering through these pages will be an experience not unlike wandering"
        "through a garden maze; getting lost is half the fun, and there are all"
        "kinds of"
      ])])
    (->Triple :welcome-3 "/item/text/text"
      ^{:order 1}
      ["treasure [2]"])
    (->Triple :welcome-3 "/item/text/text"
      ^{:order 2}
      ["hidden away for you to find."])

    (types :footnote-2 "/item/text")
    (->Triple :footnote-2 "/item/text/text"
      (str/join " " [
        "[2] As an aside, I have tried to make the URLs somewhat stable so that"
        "they can be shared and saved, but I can only guarantee a modicum of"
        "stability in a shigfting sea of ideas."
      ]))

    (types :welcome-4 "/item/text")
    (->Triple :welcome-4 "/item/text/text"
      (str/join "\n" [
        "May you find that which you search for."
        "May your bridges meet you halfway."
        "May you never lose yourself in darkness."
        "And may the light of God shiny brightly on your soul."
      ]))

    (types :praying-hands-emoji "/item/big_emoji")
    (->Triple :praying-hands-emoji "/item/big_emoji/emoji" "&#x1f64f")

    ; I Am Dan
    (essay :i-am-dan "I Am Dan" [])
    ; TODO(gierl) brief history

    ; Connections
    (essay :connections "Connections" [])
    ; TODO(gierl) contact information, respective responsibilities

    ; Apologies
    (essay :apologies "Apologies" [])
    ; TODO(gierl) apologize
  ]))

(def graph (construct-graph (apply-directives monad about)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
