(ns transcendentalism.graph-v2
  (:require [clojure.set :as set]))

(defrecord V [v])
(defrecord PV [p-vs])
(defrecord OPV [o pv])
(defrecord POPV [p-opvs])
(defrecord SPOPV [s popv])
(defrecord G [s-popvs t-ss])

(defprotocol Property
  (get-val [property]))
(defprotocol Triple
  (get-obj [triple])
  (get-props [triple])
  (get-properties [triple prop]))
(defprotocol Node
  (get-sub [node])
  (get-preds [node])
  (get-triples [node pred]))
(defprotocol Graph
  (get-types [graph])
  (get-node [graph node])
  (get-nodes [graph type])
  (has-type? [graph node type]))

(defn create-property
  [v]
  (reify Property
    (get-val [property] (:v v))))

(defn create-triple
  [opv]
  (reify Triple
    (get-obj [triple] (:o opv))
    (get-props [triple] (keys (:pv opv)))
    (get-properties [triple prop] (map create-property ((:pv opv) prop #{})))))

(defn create-node
  [spopv]
  (reify Node
    (get-sub [node] (:s spopv))
    (get-preds [node] (keys (:popv spopv)))
    (get-triples [node pred] (map create-triple ((:popv spopv) pred #{})))))

(defn create-graph
  [g]
  (reify Graph
    (get-types [graph] (keys (:t-ss g)))
    (get-node [graph node] (create-node (->SPOPV node ((:s-popvs g) node))))
    (get-nodes [graph type] (map #(create-node (->SPOPV % ((:s-popvs g) %)))
                                 ((:t-ss g) type #{})))
    (has-type? [graph node type] (contains? ((:t-ss g) type #{}) node))))

; TODO - will need functions to gracefully merge and prune trees
