(ns transcendentalism.core
  (:require [clojure.string :as str]
            [transcendentalism.toolbox :refer :all]))

; TODO - change use to :require
(use 'transcendentalism.constraint-v2
     'transcendentalism.directive
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
     'transcendentalism.schema
     'transcendentalism.schema-v2
     'transcendentalism.schema-v3)

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

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (apply set-flags args)
  (let [graph-v1 (time-msg "Construct Graph" (collect-essays)),
        ; Translation must be done into v2 for apply directives.
        graph-v2 (time-msg "V1->V2" (graph-to-v2 graph-v1)),
        graph-v3 (time-msg "V1->V3" (graph-to-v3 graph-v1)),
        graph-final-v2 (time-msg "DirectV2" (direct-graph schema graph-v2)),
        ; But back to v1 for sxs validation comparison and rendering.
        [v3-errors graph-final-v3] (time-msg "ValidateV3" (validate-graph-v3 graph-v3))
        graph-final-v1 (time-msg "V3->V1" (graph-to-v1 graph-final-v3))]
    (if (and (empty? v3-errors)
             (time-msg "ValidateV2" (validate-graph-v2 schema graph-final-v2))
             (time-msg "ValidateV1" (validate-graph-v1 schema-v1 graph-final-v1)))
      (if (flag :enable-v2)
        (println "Skipping graph generation")
        (time-msg "Generate" (generate-output graph-final-v1)))
      (println "Graph fails validation!"))))
