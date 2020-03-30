(ns transcendentalism.constraint-v3-test
  (:require [clojure.test :refer :all]
            [transcendentalism.constraint-v3 :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

(deftest test-range-constraint
  (let [range-number (range-type-constraint :number),
        range-string (range-type-constraint :string),
        range-time (range-type-constraint :time),
        range-bool (range-type-constraint :bool),
        range-type (range-type-constraint "/range_type"),
        range-enum (range-type-constraint #{:foo :bar}),
        range-nil (range-type-constraint nil),
        t (get-hours-ago 20),
        graph (write-path (create-graph-v3) #{:a} {}
                          {"/num" 7,
                           "/str" "blah",
                           "/time" t,
                           "/bool" false,
                           "/relation" :b,
                           "/enum" :bar}
                          "/relation" {"/type" :t}
                          "/type" (fn [_] "/range_type") {"/type" :type}
                          "/type" (fn [_] "/type") ),
        read-num (first (read-path graph #{:a} "/num")),
        read-str (first (read-path graph #{:a} "/str")),
        read-time (first (read-path graph #{:a} "/time")),
        read-bool (first (read-path graph #{:a} "/bool")),
        read-relation (first (read-path graph #{:a} "/relation")),
        read-enum (first (read-path graph #{:a} "/enum"))]
    (testing "Test range constraint"
      (is (= [#{} graph]
             (check-constraint range-number graph read-num nil)))
      (is (= [#{"blah does not match range type :number"} graph]
             (check-constraint range-number graph read-str nil)))
      (is (= [#{(str t " does not match range type :number")} graph]
             (check-constraint range-number graph read-time nil)))
      (is (= [#{"false does not match range type :number"} graph]
             (check-constraint range-number graph read-bool nil)))
      (is (= [#{":b does not match range type :number"} graph]
             (check-constraint range-number graph read-relation nil)))
      (is (= [#{":bar does not match range type :number"} graph]
             (check-constraint range-number graph read-enum nil)))
      (is (= [#{"7 does not match range type :string"} graph]
             (check-constraint range-string graph read-num nil)))
      (is (= [#{} graph]
             (check-constraint range-string graph read-str nil)))
      (is (= [#{(str t " does not match range type :string")} graph]
             (check-constraint range-string graph read-time nil)))
      (is (= [#{"false does not match range type :string"} graph]
             (check-constraint range-string graph read-bool nil)))
      (is (= [#{":b does not match range type :string"} graph]
             (check-constraint range-string graph read-relation nil)))
      (is (= [#{":bar does not match range type :string"} graph]
             (check-constraint range-string graph read-enum nil)))
      (is (= [#{"7 does not match range type :time"} graph]
             (check-constraint range-time graph read-num nil)))
      (is (= [#{"blah does not match range type :time"} graph]
             (check-constraint range-time graph read-str nil)))
      (is (= [#{} graph]
             (check-constraint range-time graph read-time nil)))
      (is (= [#{"false does not match range type :time"} graph]
             (check-constraint range-time graph read-bool nil)))
      (is (= [#{":b does not match range type :time"} graph]
             (check-constraint range-time graph read-relation nil)))
      (is (= [#{":bar does not match range type :time"} graph]
             (check-constraint range-time graph read-enum nil)))
      (is (= [#{"7 does not match range type :bool"} graph]
             (check-constraint range-bool graph read-num nil)))
      (is (= [#{"blah does not match range type :bool"} graph]
             (check-constraint range-bool graph read-str nil)))
      (is (= [#{(str t " does not match range type :bool")} graph]
             (check-constraint range-bool graph read-time nil)))
      (is (= [#{} graph]
             (check-constraint range-bool graph read-bool nil)))
      (is (= [#{":b does not match range type :bool"} graph]
             (check-constraint range-bool graph read-relation nil)))
      (is (= [#{":bar does not match range type :bool"} graph]
             (check-constraint range-bool graph read-enum nil)))
      (is (= [#{"7 does not match range type /range_type"} graph]
             (check-constraint range-type graph read-num nil)))
      (is (= [#{"blah does not match range type /range_type"} graph]
             (check-constraint range-type graph read-str nil)))
      (is (= [#{(str t " does not match range type /range_type")} graph]
             (check-constraint range-type graph read-time nil)))
      (is (= [#{"false does not match range type /range_type"} graph]
             (check-constraint range-type graph read-bool nil)))
      (is (= [#{} graph]
             (check-constraint range-type graph read-relation nil)))
      (is (= [#{":bar does not match range type /range_type"} graph]
             (check-constraint range-type graph read-enum nil)))
      (is (= [#{"7 does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-num nil)))
      (is (= [#{"blah does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-str nil)))
      (is (= [#{(str t " does not match range type #{:bar :foo}")} graph]
             (check-constraint range-enum graph read-time nil)))
      (is (= [#{"false does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-bool nil)))
      (is (= [#{":b does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-relation nil)))
      (is (= [#{} graph]
             (check-constraint range-enum graph read-enum nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-num nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-str nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-time nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-bool nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-relation nil)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-enum nil))))))
