(ns transcendentalism.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [transcendentalism.amazon :as amzn]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.schema :refer :all]
            [transcendentalism.server :refer :all]
            [transcendentalism.toolbox :refer :all])
  (:gen-class))

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
        (time-msg "Downloading Resources" (amzn/sync-resources-down))))))

(defn graphlet-reader
  [data]
  (->Graphlet (:v data) (:p-os data)))

(def custom-readers {'transcendentalism.graph.Graphlet graphlet-reader})

(defn- read-graph
  []
  (if (flag :aws)
      (do (println "Serving Graph in the cloud is not supported")
          (create-graph))
      (create-graph (edn/read-string {:readers custom-readers}
                                     (slurp (flag :graph-file))))))

(defn -main
  "Reads a graph, validates it, and starts serving it"
  [& args]
  (apply set-flags args)
  (let [graph (time-msg "Loading Graph" (read-graph)),
        typed-graph (merge-graph graph component-graph),
        final-graph (flatten-graph (time-msg "Validate Graph"
                                             (validate typed-graph)))]
    (prep-output final-graph)
    (launch-server final-graph)))
