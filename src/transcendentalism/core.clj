(ns transcendentalism.core)
(use
  'transcendentalism.generate
  'transcendentalism.graph
  'transcendentalism.schema)

; The monad
; This essay_segment serves as the default entry point into the graph.
(def monad
  [(->Triple :monad "/type/essay_segment" nil)
   (->Triple :monad "/essay/contains" :monad-contents)
   (->Triple :monad "/essay/title" "Transcendental Metaphysics")
   (->Triple :monad-contents "/type/item" nil)
   (->Triple :monad-contents "/type/item/ordered_set" nil)
   (->Triple :monad-contents "/item/order" :CHRONO)
   (->Triple :monad-contents "/item/contains" :monad-image)
   (->Triple :monad-contents "/item/contains" :monad-intro-text)
   (->Triple :monad-image "/type/item" nil)
   (->Triple :monad-image "/type/item/image" nil)
   (->Triple :monad-image "/item/image/url" "todo_monad_image_url.svg")
   (->Triple :monad-intro-text "/type/item" nil)
   (->Triple :monad-intro-text "/type/item/text" nil)
   (->Triple :monad-intro-text "/item/text/text" (clojure.string/join " "
    ["\"The Monad is the symbol of unity."
     "It is the godhead, the point from which all things originate,"
     "and the point to which all things return.\"\n"
     "-Daniel Gierl"]))
   ; TODO - Add /essay/flow/next to the full introduction.
   ; TODO - Add /essay/flow/see_also to the top-level menu of metaphysics essays.
   ])

(def graph (construct-graph (concat monad)))

(def schema (create-schema))

(defn -main
  "I don't do a whole lot."
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
