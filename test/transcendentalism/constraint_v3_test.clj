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

(defn check-constraint-raw-data
  [constraint graph sub]
  (let [[errors new-graph] (check-constraint constraint graph sub)]
    [errors (get-raw-data new-graph)]))

(deftest test-required-constraint
  (let [req-no-default (required-pred-constraint "/foo"),
        req-w-default (required-pred-constraint "/bar" 5),
        noncompliant-graph (write-v (create-graph-v3) :a 3),
        compliant-graph (write-path noncompliant-graph #{:a} {} {"/foo" 2,
                                                                 "/bar" 3}),
        fixed-graph (write-o noncompliant-graph :a "/bar" 5)]
    (testing "Test required constraint"
      (is (= [#{} compliant-graph]
             (check-constraint req-no-default compliant-graph :a)))
      (is (= [#{} compliant-graph]
             (check-constraint req-w-default compliant-graph :a)))
      (is (= [#{"/foo is required on :a, but not present"} noncompliant-graph]
             (check-constraint req-no-default noncompliant-graph :a)))
      (is (= [#{} (get-raw-data fixed-graph)]
             (check-constraint-raw-data req-w-default noncompliant-graph :a))))))

(deftest test-unique-constraint
  (let [unique (unique-pred-constraint "/foo"),
        zero-graph (write-v (create-graph-v3) :a 3),
        one-graph (write-o zero-graph :a "/foo" 1),
        multi-graph (write-o one-graph :a "/foo" 2)]
    (testing "Test unique constraint"
      (is (= [#{} zero-graph]
             (check-constraint unique zero-graph :a)))
      (is (= [#{} one-graph]
             (check-constraint unique one-graph :a)))
      (is (= [#{"/foo is unique on :a, but multiple present"} multi-graph]
             (check-constraint unique multi-graph :a))))))

(deftest test-excluding-constraint
  (let [mutually-exclusive (exclusive-pred-constraint #{"/foo" "/bar"}),
        singly-exclusive (exclusive-pred-constraint "/foo" #{"/bar"}),
        foo-graph (write-o (create-graph-v3) :a "/foo" 1),
        bar-graph (write-o (create-graph-v3) :a "/bar" 2),
        foo-bar-graph (write-o foo-graph :a "/bar" 3)]
    (testing "Test exclusive constraint"
      (is (= [#{} foo-graph]
             (check-constraint mutually-exclusive foo-graph :a)))
      (is (= [#{} bar-graph]
             (check-constraint mutually-exclusive bar-graph :a)))
      (is (= [#{"#{\"/foo\" \"/bar\"} on :a are mutually exclusive"}
              foo-bar-graph]
             (check-constraint mutually-exclusive foo-bar-graph :a)))
      (is (= [#{} foo-graph]
             (check-constraint singly-exclusive foo-graph :a)))
      (is (= [#{} bar-graph]
             (check-constraint singly-exclusive bar-graph :a)))
      (is (= [#{"/foo on :a excludes #{\"/bar\"}"} foo-bar-graph]
             (check-constraint singly-exclusive foo-bar-graph :a))))))
