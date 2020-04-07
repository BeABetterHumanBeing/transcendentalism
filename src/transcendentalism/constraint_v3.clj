(ns transcendentalism.constraint-v3
  (:require [clojure.set :as set]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defprotocol TypeRoot
  (get-constraint [root] "Returns the type's constraint")
  (is-abstract [root] "Whether this type is abstract"))

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
                                            "/cotype"})]
                         (if (empty? type-roots)
                             #{sub}
                             type-roots))
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

(defn accumulate-constraint
  [[errors graph] [new-errors new-graph]]
  [(set/union errors new-errors) new-graph])

(defn and-constraint
  [constraints]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (reduce
        (fn [result constraint]
          (accumulate-constraint result
                                 (check-constraint constraint
                                                   (second result) sub)))
        [#{} graph] (if (nil? constraints) [] constraints)))))

(defn multiply-pred-constraint
  "Returns a new constraint that results from applying the provided constraint
   to all objs with the given pred"
  [pred pred-constraint]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (reduce
        (fn [result obj]
          (accumulate-constraint result
                                 (check-constraint pred-constraint graph obj)))
        [#{} graph] (read-os graph sub pred)))))

(defn range-type-constraint
  [pred range-type]
  (multiply-pred-constraint pred
    (reify ConstraintV3
      (check-constraint [constraint graph obj]
        (if (nil? range-type)
          [#{} graph]
          [(let [types (get-types (create-type-aspect graph obj))]
             (if (if (set? range-type)
                     (not (empty? (set/intersection range-type types)))
                     (contains? types range-type))
                 #{}
                 #{(str obj " does not match range type " range-type)}))
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

(defn abstract-type-constraint
  []
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (let [types (get-types (create-type-aspect graph sub)),
            has-concrete-type (reduce
                                (fn [result type]
                                  (if result
                                      result
                                      (let [type-root (read-v graph type)]
                                        (if (satisfies? TypeRoot type-root)
                                            (not (is-abstract type-root))
                                            true))))
                                false types)]
        (if has-concrete-type
            [#{} graph]
            [#{(str sub " has no non-abstract type")} graph])))))

; TODO - Add order validation
(defn with-order
  [schema]
  (assoc schema
    :preds (assoc
      (schema :preds {})
      "/order" {
        :range-type :number,
        :required true,
        :unique true,
      })))

(defn schema-to-constraint
  "Creates a single constraint combining all the constraints of a given schema"
  [schema]
  (and-constraint
    (reduce-kv
      (fn [result k v]
        (case k
          :value-type (conj result (value-type-constraint v)),
          :mutually-exclusive (conj result (exclusive-pred-constraint v)),
          :abstract (if v
                        (conj result (abstract-type-constraint))
                        result),
          :preds (reduce-kv
                   (fn [result pred sub-schema]
                     (reduce-kv
                       (fn [result k v]
                         (case k
                           :range-type (conj result (range-type-constraint pred v)),
                           :required (if v
                                         (conj result (required-pred-constraint pred
                                                        (:default sub-schema nil)))
                                         result),
                           :unique (if v
                                       (conj result (unique-pred-constraint pred))
                                       result),
                           :excludes (conj result (exclusive-pred-constraint pred v)),
                           result))
                       (conj result
                             (multiply-pred-constraint
                               pred (schema-to-constraint sub-schema)))
                       sub-schema))
                   result v),
          result))
      [] schema)))

(defn build-type-graph
  "Returns a sub-graph implementing a given type"
  [graph sub supertypes schema]
  (write-path graph sub {}
              (reify TypeRoot
                (get-constraint [root] (schema-to-constraint schema))
                (is-abstract [root] (:abstract schema false)))
              (merge {"/type" :type}
                     (if (empty? supertypes)
                         {}
                         {"/supertype" supertypes}))))

(defn validate
  "Validates a graph, returning [#{errors} graph]"
  [graph]
  (reduce
    (fn [result sub]
      (accumulate-constraint
        result
        (check-constraint
          (and-constraint
            (reduce
              (fn [result type]
                (let [type-root (read-v graph type)]
                  (if (satisfies? TypeRoot type-root)
                      (conj result (get-constraint type-root))
                      result)))
              [] (get-types (create-type-aspect graph sub))))
          graph sub)))
    [#{} graph] (read-ss graph)))
