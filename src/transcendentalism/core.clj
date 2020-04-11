(ns transcendentalism.core
  (:require [clojure.string :as str]
            [ring.adapter.jetty :as ring]
            [transcendentalism.toolbox :refer :all]))

; TODO - change use to :require
(use 'transcendentalism.directive
     'transcendentalism.essay
     'transcendentalism.essays.consciousness
     'transcendentalism.essays.epistemology
     'transcendentalism.essays.intro
     'transcendentalism.essays.love
     'transcendentalism.essays.miscellaneous
     'transcendentalism.essays.morality
     'transcendentalism.essays.ontology
     'transcendentalism.essays.physics
     'transcendentalism.essays.politics
     'transcendentalism.essays.religion
     'transcendentalism.flags
     'transcendentalism.generate
     'transcendentalism.glossary
     'transcendentalism.graph
     'transcendentalism.schema)

(def meta-directives
  [directive-label-menus
   directive-see-also-inline-to-flow
   directive-dedup-cxns])

(defn collect-essays
  []
  (construct-graph
    (apply-directives
      meta-directives (glossary-essay) (intro-essays) (physics-essays)
      (ontology-essays) (epistemology-essays) (morality-essays) (religion-essays)
      (politics-essays) (consciousness-essays) (miscellaneous-essays)
      (love-essays))))

(defn render-sub-handler
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (apply set-flags args)
  (let [graph-v1 (time-msg "Construct Graph" (collect-essays)),
        graph-v3 (time-msg "V1->V3" (graph-to-v3 graph-v1)),
        [v3-errors graph-final-v3] (time-msg "ValidateV3" (validate-graph-v3 graph-v3)),
        graph-final-v1 (time-msg "V3->V1" (graph-to-v1 graph-final-v3))]
    (if (empty? v3-errors)
      (if (not (= 0 (flag :server)))
        (ring/run-jetty render-sub-handler {:port (flag :server)})
        (time-msg "Generate" (generate-output graph-final-v1)))
      (println "Graph fails validation!"))))
