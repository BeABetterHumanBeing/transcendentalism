(ns transcendentalism.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [transcendentalism.generate :as gen]
            [transcendentalism.graph-v3 :as g3]
            [transcendentalism.render :refer :all]
            [transcendentalism.server :refer :all]
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

(defn- prep-output
  "Collects all CSS and JS from the graph's renderers, and puts them in
   styles.css and script.js respectively"
  [graph]
  (gen/clear-directory "resources/output")
  (let [all-renderers (get-all-renderers graph)]
    (spit "resources/output/styles.css"
          (apply str/join "\n"
                 [(filter #(not (empty? %)) (map render-css all-renderers))]))
    (spit "resources/output/script.js"
          (apply str/join "\n"
                 [(filter #(not (empty? %)) (map render-js all-renderers))]))))

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
        (let [flattened-graph (g3/flatten-graph graph-final-v3)]
          (do (prep-output flattened-graph)
              (launch-server flattened-graph)))
        (time-msg "Generate" (gen/generate-output graph-final-v1)))
      (println "Graph fails validation!"))))
