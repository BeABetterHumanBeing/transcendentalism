(ns transcendentalism.core
  (:require [clojure.string :as str]))

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
     'transcendentalism.schema-v2)

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
  (let [graph-v1 (collect-essays),
        ; Translation must be done into v2 for apply directives.
        graph-v2 (graph-to-v2 graph-v1),
        graph-final-v2 (direct-graph schema graph-v2),
        ; But back to v1 for sxs validation comparison and rendering.
        graph-final-v1 (graph-to-v1 graph-final-v2)]
    (if (and (validate-graph-v2 schema graph-final-v2)
             (validate-graph-v1 schema-v1 graph-final-v1))
      (if (flag :enable-v2)
        (println "Skipping graph generation")
        (generate-output graph-final-v1))
      (println "Graph fails validation!"))))
