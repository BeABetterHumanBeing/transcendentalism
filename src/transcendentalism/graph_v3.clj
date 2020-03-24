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
  (get-graph-name [graph])
  (get-graphlet [graph sub] "Returns the graphlet for the sub, or nil")
  (get-raw-data [graph] "Returns the raw map underlying the graph")
  (merge-graph [graph other]
    "Returns a new graph created from merging another graph into this one"))

(defn create-graph-v3
  ([name] (create-graph-v3 name {}))
  ([name raw-data] (create-graph-v3 name raw-data nil))
  ([name raw-data base-graph]
   (reify
     GraphV3
     (get-graph-name [graph] name)
     (get-graphlet [graph sub]
       (let [graphlet (raw-data sub)]
         (if (and (nil? graphlet) (not (nil? base-graph)))
             (get-graphlet base-graph sub)
             graphlet)))
     (get-raw-data [graph] raw-data)
     (merge-graph [graph other]
       (create-graph-v3 name (merge raw-data (get-raw-data other)) base-graph))
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
         name
         (assoc raw-data sub (->Graphlet val (:p-os (get-graphlet graph sub) {})))
         base-graph))
     (write-o [graph sub pred obj]
       (let [p-os (:p-os (get-graphlet graph sub) {})]
         (create-graph-v3
           name
           (assoc raw-data sub
             (->Graphlet (:v (get-graphlet graph sub))
                         (assoc p-os pred
                           (conj (p-os pred #{}) obj))))
           base-graph))))))

(defprotocol Tablet
  (add-entry [tablet sub graph metadata]
    "Returns a new tablet with an added entry")
  (update-entry [tablet sub new-sub new-graph new-metadata]
    "Returns a new tablet with an updated entry")
  (remove-entry [tablet sub] "Returns a new tablet without the given entry")
  (merge-tablet [tablet other] [tablet other f]
    "Returns a new tablet with entries in the other tablet overwriting it. If f
     is provided, uses it to update merged metadata with (f other metadata).")
  (get-graph [tablet sub] "Returns the underlying graph the sub is a part of")
  (get-metadata [tablet sub])
  (get-bindings [tablet] "Returns the bindings"))

(defn create-read-write-tablet
  [data bindings]
  (reify
    Tablet
    (add-entry [tablet sub graph metadata]
      (create-read-write-tablet (assoc data sub [graph metadata]) bindings))
    (update-entry [tablet sub new-sub new-graph new-metadata]
      (add-entry
        (remove-entry tablet sub)
        new-sub new-graph new-metadata))
    (remove-entry [tablet sub]
      (create-read-write-tablet (dissoc data sub) bindings))
    (merge-tablet [tablet other]
      (merge-tablet tablet other (fn [_ metadata] metadata)))
    (merge-tablet [tablet other f]
      (reduce
        (fn [result sub]
          (add-entry result
            sub (get-graph other sub) (f other (get-metadata other sub))))
        tablet (read-ss other)))
    (get-graph [tablet sub] (first (data sub [(create-graph-v3 "temp")])))
    (get-metadata [tablet sub] (second (data sub [nil {}])))
    (get-bindings [tablet] bindings)
    ReadGraph
    (read-ss [tablet] (into #{} (keys data)))
    (read-v [tablet sub] (read-v (get-graph tablet sub) sub))
    (read-ps [tablet sub] (read-ps (get-graph tablet sub) sub))
    (read-os [tablet sub pred] (read-os (get-graph tablet sub) sub pred))
    WriteGraph
    (write-v [tablet sub val]
      (add-entry tablet
        sub (write-v (get-graph tablet sub) sub val) (get-metadata tablet sub)))
    (write-o [tablet sub pred obj]
      (add-entry tablet sub
                        (write-o (get-graph tablet sub) sub pred obj)
                        (get-metadata tablet sub)))))
