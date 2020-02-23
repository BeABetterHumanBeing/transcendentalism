(ns transcendentalism.core)

(use 'transcendentalism.essay
     'transcendentalism.essays.intro
     'transcendentalism.generate
     'transcendentalism.graph
     'transcendentalism.schema)

(def meta-directives
  [directive-see-also
   directive-dedup-cxns])

(def graph
  (construct-graph
    (apply-directives
      meta-directives intro-essays)))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (if (validate-graph schema graph)
    (generate-output graph)
    (println "Graph fails validation!")))
