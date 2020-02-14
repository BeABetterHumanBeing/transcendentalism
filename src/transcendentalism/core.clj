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
    (types :monad "/essay")
    (->Triple :monad "/essay/contains" :monad-contents)
    (->Triple :monad "/essay/title" "Transcendental Metaphysics")
    (types :monad-contents "/item/ordered_set")
    (->Triple :monad-contents "/item/contains" ^{:order 1} [:monad-image])
    (->Triple :monad-contents "/item/contains" ^{:order 2} [:monad-intro-quote])
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
    (directive-under-construction :welcome :i-am-dan :connections :apologies)

    ; Welcome
    (types :welcome "/essay")
    (->Triple :welcome "/essay/contains" :welcome-contents)
    (->Triple :welcome "/essay/title" "Welcome")
    (types :welcome-contents "/item/ordered_set")
    ; TODO(gierl) apologize for shitty website, explain purpose of site

    ; I Am Dan
    (types :i-am-dan "/essay")
    (->Triple :i-am-dan "/essay/contains" :i-am-dan-contents)
    (->Triple :i-am-dan "/essay/title" "I Am Dan")
    (types :i-am-dan-contents "/item/ordered_set")
    ; TODO(gierl) brief history

    ; Connections
    (types :connections "/essay")
    (->Triple :connections "/essay/contains" :connections-contents)
    (->Triple :connections "/essay/title" "Connections")
    (types :connections-contents "/item/ordered_set")
    ; TODO(gierl) contact information, respective responsibilities

    ; Apologies
    (types :apologies "/essay")
    (->Triple :apologies "/essay/contains" :apologies-contents)
    (->Triple :apologies "/essay/title" "Apologies")
    (types :apologies-contents "/item/ordered_set")
    ; TODO(gierl) apologize
  ]))

(def graph (construct-graph (apply-directives monad about)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
