(ns transcendentalism.constraint-v3
  (:require [clojure.set :as set]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

(defprotocol TypeAspect
  (get-types [aspect]))

(defn create-type-aspect
  [graph sub]
  (reify TypeAspect
    (get-types [aspect]
      (cond
        (keyword? sub) (let [types (read-path graph #{sub}
                                     "/type"
                                     #{(p* "/supertype")
                                       "/cotype"} "/")]
                         (if (empty? types)
                             #{sub}
                             types))
        (string? sub) #{:string}
        (number? sub) #{:number}
        (instance? Boolean sub) #{:bool}
        (is-valid-time sub) #{:time}
        :else #{}))))

(defprotocol ConstraintV3
  (check-constraint [constraint graph checked-sub]
    "Checks whether checked-sub conforms to the given constraint. Returns
     [#{errors} graph]"))

(defn gen-sub [sub] (keyword (str (name sub) "-" (gen-key 3))))

(defn range-type-constraint
  [range-type]
  (reify ConstraintV3
    (check-constraint [constraint graph checked-sub]
      (if (or (nil? range-type)
              (let [types (get-types (create-type-aspect graph checked-sub))]
                (if (set? range-type)
                    (not (empty? (set/intersection range-type types)))
                    (contains? types range-type))))
          [#{} graph]
          [#{(str checked-sub " does not match range type " range-type)} graph]))))
