(ns transcendentalism.graph
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [transcendentalism.time :refer :all]))

; The graph is composed of triples. Each triple relates a subject to an object
; by means of a predicate.
(defrecord Triple [sub pred obj p-vs])

(defn index-by-sub
  "Indexes a collection of triples by their subjects"
  [triples]
  (reduce
    (fn [result triple]
      (assoc result (:sub triple) (conj (result (:sub triple)) triple)))
    {} triples))

; The Graph protocol is the interface through which a graph is accessed.
(defprotocol GraphV1
  (all-triples [graph] [graph sub] [graph sub pred]
    "Returns a collection of all the triples in the graph [for a given [sub and pred]]")
  (get-unique [graph sub pred]
    "Returns the obj of the unique triple on sub with pred, or nil if none exists")
  (get-time [graph sub]
    "Returns the time associated with the given sub, or nil if it has none"))

(defn construct-graph
  "Constructs a graph from a set of triples"
  [triples]
  (let [graph-data (index-by-sub triples)]
    (reify GraphV1
      (all-triples [graph]
        (flatten (map second (seq graph-data))))
      (all-triples [graph sub-or-pred]
        (if (string? sub-or-pred)
          (filter #(= (:pred %) sub-or-pred) (all-triples graph))
          (sub-or-pred graph-data)))
      (all-triples [graph sub pred]
        (filter #(= (:pred %) pred) (all-triples graph sub)))
      (get-unique [graph sub pred]
        (let [triples (all-triples graph sub pred)]
          (if (empty? triples) nil (:obj (first triples)))))
      (get-time [graph sub]
        (let [value (get-unique graph sub "/event/time")]
          (if (nil? value) nil (to-time value)))))))
