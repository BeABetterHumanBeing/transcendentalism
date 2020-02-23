(ns transcendentalism.core)

(use 'transcendentalism.essay
     'transcendentalism.essays.intro
     'transcendentalism.essays.physics
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
      meta-directives intro-essays physics-essays)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
