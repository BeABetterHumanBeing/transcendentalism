(ns transcendentalism.garden
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [transcendentalism.flags :refer :all]
            [transcendentalism.graph :refer :all]))

; I like to think of the graphs as being flowers. The garden is where they can
; all be found.

(defn- graphlet-reader
  [data]
  (->Graphlet (:v data) (:p-os data)))
(def custom-readers {'transcendentalism.graph.Graphlet graphlet-reader})

(defn- name-to-file
  [name]
  (str "graphs/" name ".clj"))

(defn read-flower
  [name]
  (create-graph (edn/read-string {:readers custom-readers}
                                 (slurp (name-to-file name)))))

(defn write-flower
  [graph name]
  (let [file (name-to-file name)]
    (io/make-parents file)
    (spit file (pr-str (get-raw-data graph)))
    (println "Saved graph to" file)))
