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

(defprotocol Tablet
  (add-entry [tablet sub metadata]
             [tablet sub metadata graph]
    "Returns a new tablet with an added entry")
  (update-entry [tablet sub new-sub new-metadata]
    "Returns a new tablet with an updated entry")
  (remove-entry [tablet sub] "Returns a new tablet without the given entry")
  (merge-tablet [tablet other] [tablet other f]
    "Returns a new tablet with entries in the other tablet overwriting it. If f
     is provided, uses it to update merged metadata with (f other metadata).")
  (diff-tablet [tablet other]
    "Returns a new tablet without entries whose subs appear in other")
  (get-target-graph [tablet] "Returns the underlying graph")
  (get-metadata [tablet sub])
  (get-bindings [tablet] "Returns the bindings")
  (get-results [tablet] [tablet result f]
    "Returns the set of final results, discarding metadata. If given an f, uses
     it to reduce-kv into result over sub-metadata pairs."))

(defn create-read-write-tablet
  [data bindings graph]
  (reify
    Tablet
    (add-entry [tablet sub metadata]
      (add-entry tablet sub metadata graph))
    (add-entry [tablet sub metadata graph]
      (create-read-write-tablet (assoc data sub metadata) bindings graph))
    (update-entry [tablet sub new-sub new-metadata]
      (add-entry (remove-entry tablet sub) new-sub new-metadata))
    (remove-entry [tablet sub]
      (create-read-write-tablet (dissoc data sub) bindings graph))
    (merge-tablet [tablet other]
      (merge-tablet tablet other (fn [metadata] metadata)))
    (merge-tablet [tablet other f]
      (reduce
        (fn [result sub]
          (add-entry result sub
                            (f (get-metadata other sub))
                            (merge-graph graph (get-target-graph other))))
        tablet (read-ss other)))
    (diff-tablet [tablet other]
      (reduce
        (fn [result sub]
          (remove-entry result sub))
        tablet (read-ss other)))
    (get-target-graph [tablet] graph)
    (get-metadata [tablet sub] (data sub))
    (get-bindings [tablet] bindings)
    (get-results [tablet]
      (get-results tablet #{} (fn [result sub metadata] (conj result sub))))
    (get-results [tablet result f] (reduce-kv f result data))
    ReadGraph
    (read-ss [tablet] (into #{} (keys data)))
    (read-v [tablet sub] (read-v graph sub))
    (read-ps [tablet sub] (read-ps graph sub))
    (read-os [tablet sub pred] (read-os graph sub pred))
    WriteGraph
    (write-v [tablet sub val]
      (add-entry tablet sub
                        (data sub)
                        (write-v graph sub val)))
    (write-o [tablet sub pred obj]
      (add-entry tablet sub
                        (data sub)
                        (write-o graph sub pred obj)))))

(defprotocol GraphPath
  (follow-path [path tablet]
    "Advances the tablet along path, returning the new tablet"))

(defn follow-all
  [tablet & paths]
  (reduce
    (fn [result path]
      (follow-path path result))
    tablet paths))

(defn path-nil
  ([]
   (reify GraphPath
     (follow-path [path tablet] tablet)))
  ; If given an f, will call (f tablet sub metadata) to update the metadata on
  ; sub, or drop the entry if f returns nil.
  ([f]
   (reify GraphPath
     (follow-path [path tablet]
       (reduce
         (fn [result sub]
           (let [new-metadata (f tablet sub (get-metadata tablet sub))]
             (if (nil? new-metadata)
                 (remove-entry result sub)
                 (update-entry result sub sub new-metadata))))
         tablet (read-ss tablet))))))

(defn path-chain
  [& paths]
  (reify GraphPath
    (follow-path [path tablet]
      (reduce
        (fn [result path]
          (follow-path path result))
        tablet paths))))

(defn path-all
  ([paths]
   (reify GraphPath
     (follow-path [path tablet]
       ; Tablets' orders are reversed so that if a sub appears in multiple
       ; tablets, the final entry is the earliest branch of the OR.
       (let [new-tablets (reverse (map
                           (fn [path]
                             (follow-path path tablet))
                           paths))]
         (reduce
           (fn [result tablet]
             (merge-tablet result tablet))
           (create-read-write-tablet
            {} (get-bindings tablet) (get-target-graph tablet))
           new-tablets))))))

(defn read-self
  []
  (reify GraphPath
    (follow-path [path tablet]
      (reduce
        (fn [result sub]
          (cond
            (keyword? sub)
              (update-entry result sub (read-v tablet sub) (get-metadata tablet sub)),
            :else result))
        tablet (read-ss tablet)))))

(defn read-pred
  ([pred]
   (reify GraphPath
     (follow-path [path tablet]
       (reduce
         (fn [result sub]
           (reduce
             (fn [result obj]
               (add-entry result obj (get-metadata tablet sub)))
             (remove-entry result sub) (read-os tablet sub pred)))
         tablet (read-ss tablet))))))

(defn read-star
  ([inner-path]
   (reify GraphPath
     (follow-path [path tablet]
       (loop [final-result tablet,
              unprocessed final-result]
         (if (empty? (read-ss unprocessed))
             final-result
             (let [next-result (follow-path inner-path unprocessed)]
               (recur (merge-tablet final-result next-result)
                      (diff-tablet next-result final-result)))))))))

(defn write-val
  [val]
  (reify GraphPath
    (follow-path [path tablet]
      (reduce
        (fn [result sub]
          (write-v tablet
            sub (cond (fn? val) (val sub)
                      (keyword? val) (val (get-bindings tablet) val)
                      :else val)))
        tablet (read-ss tablet)))))

(defn write-obj
  [pred val]
  (reify GraphPath
    (follow-path [path tablet]
      (reduce
        (fn [result sub]
          (write-o tablet
            sub pred (cond (fn? val) (val sub)
                           (keyword? val) (val (get-bindings tablet) val)
                           :else val)))
        tablet (read-ss tablet)))))
