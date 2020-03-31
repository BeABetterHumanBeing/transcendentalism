(ns transcendentalism.constraint-v3
  (:require [clojure.set :as set]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

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
  [range-type]
  (reify ConstraintV3
    (check-constraint [constraint graph sub]
      (if (or (nil? range-type)
              (let [types (get-types (create-type-aspect graph sub))]
                (if (set? range-type)
                    (not (empty? (set/intersection range-type types)))
                    (contains? types range-type))))
          [#{} graph]
          [#{(str sub " does not match range type " range-type)} graph]))))

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
