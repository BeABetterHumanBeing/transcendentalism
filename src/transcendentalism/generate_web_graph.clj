(ns transcendentalism.generate-web-graph
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.essay :refer :all]
            [transcendentalism.essays.consciousness :refer :all]
            [transcendentalism.essays.epistemology :refer :all]
            [transcendentalism.essays.intro :refer :all]
            [transcendentalism.essays.love :refer :all]
            [transcendentalism.essays.miscellaneous :refer :all]
            [transcendentalism.essays.morality :refer :all]
            [transcendentalism.essays.ontology :refer :all]
            [transcendentalism.essays.personal :refer :all]
            [transcendentalism.essays.physics :refer :all]
            [transcendentalism.essays.politics :refer :all]
            [transcendentalism.essays.randomness :refer :all]
            [transcendentalism.essays.religion :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.schema :refer :all]
            [transcendentalism.tablet-v2 :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defn collect-graph
  []
  (reduce
    (fn [result tablet]
      (merge-graph result (tablet-as-graph tablet)))
    (create-graph)
    (flatten
      [(glossary-essay) (intro-essays) (physics-essays) (ontology-essays)
       (epistemology-essays) (morality-essays) (religion-essays)
       (politics-essays) (consciousness-essays) (miscellaneous-essays)
       (love-essays) (personal-essays) (randomness-essays)
       ])))

(defn- write-graph
  [graph]
  (spit (flag :graph-file) (pr-str (get-raw-data graph)))
  (println "Saved graph to" (flag :graph-file)))

(defn -main
  "Generates the website's graph, and saves it to graph-file"
  [& args]
  (apply set-flags args)
  (let [graph (time-msg "Construct Graph" (collect-graph)),
        flattened-graph (flatten-graph graph)]
    (when (flag :validate)
          (time-msg "Validate Graph" (validate
                                       (merge-graph flattened-graph
                                                    component-graph))))
    (write-graph flattened-graph)))
