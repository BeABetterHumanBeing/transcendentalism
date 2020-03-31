(ns transcendentalism.constraint-v3
  (:require [clojure.set :as set]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defprotocol TypeRoot
  (get-type-name [root] "Returns the name of the type"))

(defprotocol TypeAspect
  (get-types [aspect] "Returns the set of all types"))

(defn create-type-aspect
  [graph sub]
  (reify TypeAspect
    (get-types [aspect]
      (cond
        (keyword? sub) (let [type-roots (read-path graph #{sub}
                                          "/type"
                                          #{(p* "/supertype")
                                            "/cotype"} "/"),
                             type-names (into #{} (map get-type-name type-roots))]
                         (if (empty? type-names)
                             #{sub}
                             type-names))
        (fn? sub) #{:fn}
        (string? sub) #{:string}
        (number? sub) #{:number}
        (instance? Boolean sub) #{:bool}
        (is-valid-time sub) #{:time}
        :else #{}))))

(defprotocol ConstraintV3
  (check-constraint [constraint graph checked-sub]
    "Checks whether checked-sub conforms to the given constraint. Returns
     [#{errors} graph]"))

(defn and-constraint
  [constraints]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (reduce
        (fn [result constraint]
          (let [[errors new-graph] (check-constraint constraint
                                                     (second result) sub)]
            [(set/union (first result) errors),
             (merge-graph (second result) new-graph)]))
        [#{} graph] constraints))))

(defn range-type-constraint
  [pred range-type]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (if (nil? range-type)
          [#{} graph]
          (let [objs (read-os graph sub pred)]
            [(reduce
               (fn [result obj]
                 (let [types (get-types (create-type-aspect graph obj))]
                   (if (if (set? range-type)
                           (not (empty? (set/intersection range-type types)))
                           (contains? types range-type))
                       result
                       (conj result
                             (str obj " does not match range type " range-type)))))
              #{} objs)
             graph])))))

(defn value-type-constraint
  [range-type]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (let [val (read-v graph sub)]
        (if (or (nil? range-type)
                (let [types (get-types (create-type-aspect graph val))]
                  (if (set? range-type)
                      (not (empty? (set/intersection range-type types)))
                      (contains? types range-type))))
            [#{} graph]
            [#{(str val " does not match value type " range-type)} graph])))))

(defn required-pred-constraint
  ([required-pred] (required-pred-constraint required-pred nil))
  ([required-pred default]
   (reify ConstraintV3
     (check-constraint [constraint graph sub]
       (let [objs (read-os graph sub required-pred)]
         (if (empty? objs)
             (if (nil? default)
                 [#{(str required-pred " is required on " sub ", but not present")}
                  graph]
                 [#{}
                  (write-o graph sub required-pred default)])
             [#{} graph]))))))

(defn unique-pred-constraint
  [unique-pred]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (let [objs (read-os graph sub unique-pred)]
        (if (> (count objs) 1)
            [#{(str unique-pred " is unique on " sub ", but multiple present")}
             graph]
            [#{} graph])))))

(defn exclusive-pred-constraint
  ([mutually-exclusive-preds]
   (reify ConstraintV3
     (check-constraint [constraint graph sub]
       (let [preds (read-ps graph sub),
             offenders (set/intersection preds mutually-exclusive-preds)]
         (if (> (count offenders) 1)
             [#{(str offenders " on " sub " are mutually exclusive")} graph]
             [#{} graph])))))
  ([excluding-pred excluded-preds]
   (reify ConstraintV3
     (check-constraint [constraint graph sub]
       (let [preds (read-ps graph sub)]
         (if (contains? preds excluding-pred)
             (let [offenders (set/intersection preds excluded-preds)]
               (if (empty? offenders)
                   [#{} graph]
                   [#{(str excluding-pred " on " sub " excludes " offenders)}
                    graph]))
             [#{} graph]))))))

(defn schema-to-constraint
  "Creates a single constraint combining all the constraints of a given schema"
  [schema]
  (and-constraint
    (reduce-kv
      (fn [result k v]
        (case k
          :value-type (conj result (value-type-constraint v)),
          :mutually-exclusive (conj result (exclusive-pred-constraint v)),
          :preds (reduce-all result result
                             [[pred sub-schema v]
                              [k v sub-schema]]
                   (case k
                     :range-type (conj result (range-type-constraint pred v)),
                     :required (conj result (required-pred-constraint pred
                                              (:default sub-schema nil))),
                     :unique (conj result (unique-pred-constraint pred)),
                     :excludes (conj result (exclusive-pred-constraint pred v)),
                     result)),
          result))
      [] schema)))

; TODO - Test
(defn validate
  "Validates a graph, returning [#{errors} graph]"
  [graph]
  (reduce
    (fn [result sub]
      (check-constraint
        (and-constraint
          (reduce
            (fn [result type]
              (if (satisfies? ConstraintV3 type)
                  (conj result type)
                  result))
            [] (get-types (create-type-aspect graph sub))))
        graph sub))
    [#{} graph] (read-ss graph)))
