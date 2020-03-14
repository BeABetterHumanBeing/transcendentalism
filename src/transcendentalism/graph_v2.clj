(ns transcendentalism.graph-v2
  (:require [clojure.set :as set]))

(defrecord V [v])
(defrecord PV [p-vs])
(defrecord OPV [o pv])
(defrecord POPV [p-opvs])
(defrecord SPOPV [s popv])
(defrecord TSPOPV [t-spopvs])
(defrecord GTSPOPV [g tspopv])

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
  (get-name [graph])
  (get-types [graph])
  (get-nodes [graph type]))

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
  [gtspopv]
  (reify Graph
    (get-name [graph] (:g gtspopv))
    (get-types [graph] (keys (:tspopv gtspopv)))
    (get-nodes [graph type] (map create-node ((:tspopv gtspopv) type #{})))))

; TODO - will need functions to gracefully merge and prune trees
