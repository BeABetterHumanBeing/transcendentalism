(ns transcendentalism.core)
(use
  'transcendentalism.generate
  'transcendentalism.graph
  'transcendentalism.schema
  'transcendentalism.svg)

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  (flatten [
    (types :monad "/essay_segment")
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
    (->Triple :monad "/essay/flow/next" :welcome)
    ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
   ]))

; About
; This sequence of essay_segments say a little about myself, this website, and
; what I hope to do with it.
(def about
  (flatten [
    ; Welcome
    (types :welcome "/essay_segment")
    (->Triple :welcome "/essay/contains" :welcome-contents)
    (->Triple :welcome "/essay/title" "Welcome")
    (types :welcome-contents "/item/ordered_set")
    ; TODO(gierl) apologize for shitty website, explain purpose of site
    (->Triple :welcome "/essay/flow/home" :monad)
    (->Triple :welcome "/essay/flow/next" :i-am-dan)

    ; I Am Dan
    (types :i-am-dan "/essay_segment")
    (->Triple :i-am-dan "/essay/contains" :i-am-dan-contents)
    (->Triple :i-am-dan "/essay/title" "I Am Dan")
    (types :i-am-dan-contents "/item/ordered_set")
    ; TODO(gierl) brief history
    (->Triple :i-am-dan "/essay/flow/home" :monad)
    (->Triple :i-am-dan "/essay/flow/next" :connections)

    ; Connections
    (types :connections "/essay_segment")
    (->Triple :connections "/essay/contains" :connections-contents)
    (->Triple :connections "/essay/title" "Connections")
    (types :connections-contents "/item/ordered_set")
    ; TODO(gierl) contact information, respective responsibilities
    (->Triple :connections "/essay/flow/home" :monad)
    (->Triple :connections "/essay/flow/next" :apologies)

    ; Apologies
    (types :apologies "/essay_segment")
    (->Triple :apologies "/essay/contains" :apologies-contents)
    (->Triple :apologies "/essay/title" "Apologies")
    (types :apologies-contents "/item/ordered_set")
    ; TODO(gierl) apologize
    (->Triple :apologies "/essay/flow/home" :monad)
  ]))

(def graph (construct-graph (concat monad about)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
