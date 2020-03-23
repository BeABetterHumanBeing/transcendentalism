(ns transcendentalism.graph-v3
  (:require [clojure.set :as set]))

; A graph is a map from :sub to Graphlet. v is some value (possibly nil), and
; p-os is a map from predicate to set of :objs that have that predicate.
(defrecord Graphlet [v p-os])

(defprotocol ReadGraph
  (read-ss [graph] "Returns the set of all subs")
  (read-v [graph sub] "Returns the value associated with sub, or nil")
  (read-ps [graph sub] "Returns the set of all predicates on the sub")
  (read-os [graph sub pred]
    "Returns the set of all objects associated with sub by pred. If regex, all
     preds that match."))

(defprotocol WriteGraph
  (write-v [graph sub val]
    "Returns a new graph where sub's value is val, creating sub if it doesn't
     already exist")
  (write-o [graph sub pred obj]
    "Returns a new graph where sub is associated with obj by pred, creating sub
     if it doesn't already exist"))

(defprotocol GraphV3
  (get-graphlet [graph sub] "Returns the graphlet for the sub, or nil")
  (get-raw-data [graph] "Returns the raw map underlying the graph")
  (merge-graph [graph other]
    "Returns a new graph created from merging another graph into this one"))

(defn create-graph-v3
  ([] (create-graph-v3 {}))
  ([raw-data] (create-graph-v3 raw-data nil))
  ([raw-data base-graph]
   (reify
     GraphV3
     (get-graphlet [graph sub]
       (let [graphlet (raw-data sub)]
         (if (and (nil? graphlet) (not (nil? base-graph)))
             (get-graphlet base-graph sub)
             graphlet)))
     (get-raw-data [graph] raw-data)
     (merge-graph [graph other]
       (create-graph-v3 (merge raw-data (get-raw-data other)) base-graph))
     ReadGraph
     (read-ss [graph]
       (let [my-subs (into #{} (keys raw-data))]
         (if (nil? base-graph)
             my-subs
             (set/union my-subs (read-ss base-graph)))))
     (read-v [graph sub] (:v (get-graphlet graph sub)))
     (read-ps [graph sub] (into #{} (keys (:p-os (get-graphlet graph sub) {}))))
     (read-os [graph sub pred]
       (if (instance? java.util.regex.Pattern pred)
           (reduce-kv
             (fn [result p os]
               (if (nil? (re-matches pred p))
                   result
                   (into result os)))
             #{} (:p-os (get-graphlet graph sub) {}))
           ((:p-os (get-graphlet graph sub) {}) pred #{})))
     WriteGraph
     (write-v [graph sub val]
       (create-graph-v3
         (assoc raw-data sub (->Graphlet val (:p-os (get-graphlet graph sub) {})))
         base-graph))
     (write-o [graph sub pred obj]
       (let [p-os (:p-os (get-graphlet graph sub) {})]
         (create-graph-v3
           (assoc raw-data sub
             (->Graphlet (:v (get-graphlet graph sub))
                         (assoc p-os pred
                           (conj (p-os pred #{}) obj))))
           base-graph))))))
