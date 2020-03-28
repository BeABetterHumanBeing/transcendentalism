(ns transcendentalism.constraint-v3
  (:require [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

(defprotocol ConstraintV3
  (check-constraint [constraint graph checked-sub constraint-sub]
    "Checks whether checked-sub conforms to the particulars of constraint-sub.
     Returns [#{errors} new-graph]"))

(defn gen-sub [sub] (keyword (str (name sub) "-" (gen-key 3))))

(defn get-types
  [graph sub]
  (read-path graph #{sub}
     "/type"
     #{(p* "/supertype")
       "/cotype"} "/"))

; TODO - consider moving range-type to being a pred on constraint-sub. Would
; enable changing range-type-constraint from defn to def.
(defn range-type-constraint
  [range-type]
  (reify ConstraintV3
    (check-constraint [constraint graph checked-sub constraint-sub]
      (if (or (nil? range-type)
              (and (= range-type :string)
                   (string? checked-sub))
              (and (= range-type :number)
                   (number? checked-sub))
              (and (= range-type :bool)
                   (instance? Boolean checked-sub))
              (and (= range-type :time)
                   (is-valid-time checked-sub))
              (and (string? range-type)
                   (contains? (get-types graph checked-sub) range-type))
              (and (vector? range-type)
                   (not (nil? (some #(= checked-sub %) range-type)))))
          [#{} graph]
          [#{(str checked-sub " does not match range type " range-type)} graph]))))
