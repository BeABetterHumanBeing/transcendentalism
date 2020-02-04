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
    (->Triple :monad-contents "/item/contains" ^{:order 2} [:monad-intro-text])
    (types :monad-image "/item/image")
    (->Triple :monad-image "/item/image/url" (svg-to-image "monad" 800 800 'svg-monad))
    (types :monad-intro-text "/item/text")
    (->Triple :monad-intro-text "/item/text/text" (clojure.string/join " "
      ["\"The Monad is the symbol of unity."
      "It is the godhead, the point from which all things originate,"
      "and the point to which all things return.\"\n"
      "-Daniel Gierl"]))
    ; TODO - Add /essay/flow/next to the full introduction.
    ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
   ]))

(def graph (construct-graph (concat monad)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
