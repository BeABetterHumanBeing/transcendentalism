(ns transcendentalism.core
  (:require [clojure.string :as str]))

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
  (let [graph (collect-essays)]
    (if (if (flag :enable-v2)
            (validate-graph-v2 schema-v2 (graph-to-v2 graph))
            (validate-graph schema graph))
      (generate-output graph)
      (println "Graph fails validation!"))))
