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
        type-root (reify TypeRoot
                    (get-type-name [root] "/range_type"))
        graph (write-path (create-graph-v3) #{:a} {}
                          {"/num" 7,
                           "/str" "blah",
                           "/time" t,
                           "/bool" false,
                           "/relation" :b,
                           "/enum" :bar}
                          "/relation" {"/type" :t}
                          "/type" type-root {"/type" :type}),
        read-num (first (read-path graph #{:a} "/num")),
        read-str (first (read-path graph #{:a} "/str")),
        read-time (first (read-path graph #{:a} "/time")),
        read-bool (first (read-path graph #{:a} "/bool")),
        read-relation (first (read-path graph #{:a} "/relation")),
        read-enum (first (read-path graph #{:a} "/enum"))]
    (testing "Test range constraint"
      (is (= [#{} graph]
             (check-constraint range-number graph read-num)))
      (is (= [#{"blah does not match range type :number"} graph]
             (check-constraint range-number graph read-str)))
      (is (= [#{(str t " does not match range type :number")} graph]
             (check-constraint range-number graph read-time)))
      (is (= [#{"false does not match range type :number"} graph]
             (check-constraint range-number graph read-bool)))
      (is (= [#{":b does not match range type :number"} graph]
             (check-constraint range-number graph read-relation)))
      (is (= [#{":bar does not match range type :number"} graph]
             (check-constraint range-number graph read-enum)))
      (is (= [#{"7 does not match range type :string"} graph]
             (check-constraint range-string graph read-num)))
      (is (= [#{} graph]
             (check-constraint range-string graph read-str)))
      (is (= [#{(str t " does not match range type :string")} graph]
             (check-constraint range-string graph read-time)))
      (is (= [#{"false does not match range type :string"} graph]
             (check-constraint range-string graph read-bool)))
      (is (= [#{":b does not match range type :string"} graph]
             (check-constraint range-string graph read-relation)))
      (is (= [#{":bar does not match range type :string"} graph]
             (check-constraint range-string graph read-enum)))
      (is (= [#{"7 does not match range type :time"} graph]
             (check-constraint range-time graph read-num)))
      (is (= [#{"blah does not match range type :time"} graph]
             (check-constraint range-time graph read-str)))
      (is (= [#{} graph]
             (check-constraint range-time graph read-time)))
      (is (= [#{"false does not match range type :time"} graph]
             (check-constraint range-time graph read-bool)))
      (is (= [#{":b does not match range type :time"} graph]
             (check-constraint range-time graph read-relation)))
      (is (= [#{":bar does not match range type :time"} graph]
             (check-constraint range-time graph read-enum)))
      (is (= [#{"7 does not match range type :bool"} graph]
             (check-constraint range-bool graph read-num)))
      (is (= [#{"blah does not match range type :bool"} graph]
             (check-constraint range-bool graph read-str)))
      (is (= [#{(str t " does not match range type :bool")} graph]
             (check-constraint range-bool graph read-time)))
      (is (= [#{} graph]
             (check-constraint range-bool graph read-bool)))
      (is (= [#{":b does not match range type :bool"} graph]
             (check-constraint range-bool graph read-relation)))
      (is (= [#{":bar does not match range type :bool"} graph]
             (check-constraint range-bool graph read-enum)))
      (is (= [#{"7 does not match range type /range_type"} graph]
             (check-constraint range-type graph read-num)))
      (is (= [#{"blah does not match range type /range_type"} graph]
             (check-constraint range-type graph read-str)))
      (is (= [#{(str t " does not match range type /range_type")} graph]
             (check-constraint range-type graph read-time)))
      (is (= [#{"false does not match range type /range_type"} graph]
             (check-constraint range-type graph read-bool)))
      (is (= [#{} graph]
             (check-constraint range-type graph read-relation)))
      (is (= [#{":bar does not match range type /range_type"} graph]
             (check-constraint range-type graph read-enum)))
      (is (= [#{"7 does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-num)))
      (is (= [#{"blah does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-str)))
      (is (= [#{(str t " does not match range type #{:bar :foo}")} graph]
             (check-constraint range-enum graph read-time)))
      (is (= [#{"false does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-bool)))
      (is (= [#{":b does not match range type #{:bar :foo}"} graph]
             (check-constraint range-enum graph read-relation)))
      (is (= [#{} graph]
             (check-constraint range-enum graph read-enum)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-num)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-str)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-time)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-bool)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-relation)))
      (is (= [#{} graph]
             (check-constraint range-nil graph read-enum))))))
