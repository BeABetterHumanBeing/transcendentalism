(ns transcendentalism.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [transcendentalism.directive :refer :all]
            [transcendentalism.essay :refer :all]
            [transcendentalism.essays.consciousness :refer :all]
            [transcendentalism.essays.epistemology :refer :all]
            [transcendentalism.essays.intro :refer :all]
            [transcendentalism.essays.love :refer :all]
            [transcendentalism.essays.miscellaneous :refer :all]
            [transcendentalism.essays.morality :refer :all]
            [transcendentalism.essays.ontology :refer :all]
            [transcendentalism.essays.physics :refer :all]
            [transcendentalism.essays.politics :refer :all]
            [transcendentalism.essays.religion :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.glossary :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.graph-v3 :as g3]
            [transcendentalism.render :refer :all]
            [transcendentalism.schema :refer :all]
            [transcendentalism.server :refer :all]
            [transcendentalism.toolbox :refer :all])
  (:gen-class))

(def meta-directives
  [directive-label-menus])

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
  (clear-directory "resources/output")
  (let [all-renderers (get-all-renderers graph)]
    (let [css-filename "resources/output/styles.css"]
      (io/make-parents css-filename)
      (spit css-filename
            (apply str/join "\n"
                   [(filter #(not (empty? %)) (map render-css all-renderers))]))
      (spit "resources/output/script.js"
            (apply str/join "\n"
                   [(filter #(not (empty? %)) (map render-js all-renderers))])))))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (apply set-flags args)
  (let [graph-v1 (time-msg "Construct Graph" (collect-essays)),
        graph-v3 (time-msg "V1->V3" (graph-to-v3 graph-v1)),
        [v3-errors graph-final-v3] (time-msg "ValidateV3" (validate-graph-v3 graph-v3))]
    (if (empty? v3-errors)
      (let [flattened-graph (g3/flatten-graph graph-final-v3)]
        (do (prep-output flattened-graph)
            (launch-server flattened-graph)))
      (println "Graph fails validation!"))))
