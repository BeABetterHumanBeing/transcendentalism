(ns transcendentalism.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [transcendentalism.amazon :refer :all]
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
            [transcendentalism.glossary :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.schema :refer :all]
            [transcendentalism.server :refer :all]
            [transcendentalism.tablet-v2 :refer :all]
            [transcendentalism.toolbox :refer :all])
  (:gen-class))

(defn collect-graph
  []
  (reduce
    (fn [result tablet]
      (commit-txn tablet result))
    component-graph
    (flatten
      [(glossary-essay) (intro-essays) (physics-essays) (ontology-essays)
       (epistemology-essays) (morality-essays) (religion-essays)
       (politics-essays) (consciousness-essays) (miscellaneous-essays)
       (love-essays) (personal-essays) (randomness-essays)])))

(defn- prep-output
  "Collects all CSS and JS from the graph's renderers, and puts them in
   styles.css and script.js respectively"
  [graph]
  (let [dir "resources/output"]
    (clear-directory dir)
    (let [all-renderers (get-all-renderers graph),
          collect-css (fn [is-mobile]
                        (apply str/join "\n"
                          [(filter #(not (empty? %))
                                   (map #(render-css % is-mobile)
                                        all-renderers))]))]
      (io/make-parents (str dir "/tmp"))
      (spit (str dir "/styles.css") (collect-css false))
      (spit (str dir "/mobile_styles.css") (collect-css true))
      (spit (str dir "/script.js")
            (apply str/join "\n"
                   [(filter #(not (empty? %)) (map render-js all-renderers))]))
      (when (flag :aws)
        (time-msg "Downloading Resources" (sync-resources-down))))))

(defn -main
  "Validates the website's graph, and generates its files"
  [& args]
  (apply set-flags args)
  (if (flag :sync)
      (time-msg "Syncing" (sync-resources-up))
      (let [graph (time-msg "Construct Graph" (collect-graph)),
            flattened-graph (flatten-graph graph)]
        (prep-output flattened-graph)
        (launch-server flattened-graph))))
