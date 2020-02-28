(ns transcendentalism.core)

(use 'transcendentalism.essay
     'transcendentalism.essays.consciousness
     'transcendentalism.essays.epistemology
     'transcendentalism.essays.intro
     'transcendentalism.essays.morality
     'transcendentalism.essays.ontology
     'transcendentalism.essays.physics
     'transcendentalism.essays.politics
     'transcendentalism.essays.religion
     'transcendentalism.generate
     'transcendentalism.graph
     'transcendentalism.schema)

(def meta-directives
  [directive-label-menus
   directive-see-also-inline-to-flow
   directive-dedup-cxns])

(def graph
  (construct-graph
    (apply-directives
      meta-directives intro-essays physics-essays ontology-essays
      epistemology-essays morality-essays religion-essays politics-essays
      consciousness-essays)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
