(ns transcendentalism.constraint-v3-test
  (:require [clojure.test :refer :all]
            [transcendentalism.constraint-v3 :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

(deftest test-range-constraint
  (let [t (get-hours-ago 20),
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
                          "/type" type-root {"/type" :type})]
    (testing "Test range constraint"
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/num" :number) graph :a)))
      (is (= [#{"blah does not match range type :number"} graph]
             (check-constraint (range-type-constraint "/str" :number) graph :a)))
      (is (= [#{(str t " does not match range type :number")} graph]
             (check-constraint (range-type-constraint "/time" :number) graph :a)))
      (is (= [#{"false does not match range type :number"} graph]
             (check-constraint (range-type-constraint "/bool" :number) graph :a)))
      (is (= [#{":b does not match range type :number"} graph]
             (check-constraint (range-type-constraint "/relation" :number) graph :a)))
      (is (= [#{":bar does not match range type :number"} graph]
             (check-constraint (range-type-constraint "/enum" :number) graph :a)))
      (is (= [#{"7 does not match range type :string"} graph]
             (check-constraint (range-type-constraint "/num" :string) graph :a)))
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/str" :string) graph :a)))
      (is (= [#{(str t " does not match range type :string")} graph]
             (check-constraint (range-type-constraint "/time" :string) graph :a)))
      (is (= [#{"false does not match range type :string"} graph]
             (check-constraint (range-type-constraint "/bool" :string) graph :a)))
      (is (= [#{":b does not match range type :string"} graph]
             (check-constraint (range-type-constraint "/relation" :string) graph :a)))
      (is (= [#{":bar does not match range type :string"} graph]
             (check-constraint (range-type-constraint "/enum" :string) graph :a)))
      (is (= [#{"7 does not match range type :time"} graph]
             (check-constraint (range-type-constraint "/num" :time) graph :a)))
      (is (= [#{"blah does not match range type :time"} graph]
             (check-constraint (range-type-constraint "/str" :time) graph :a)))
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/time" :time) graph :a)))
      (is (= [#{"false does not match range type :time"} graph]
             (check-constraint (range-type-constraint "/bool" :time) graph :a)))
      (is (= [#{":b does not match range type :time"} graph]
             (check-constraint (range-type-constraint "/relation" :time) graph :a)))
      (is (= [#{":bar does not match range type :time"} graph]
             (check-constraint (range-type-constraint "/enum" :time) graph :a)))
      (is (= [#{"7 does not match range type :bool"} graph]
             (check-constraint (range-type-constraint "/num" :bool) graph :a)))
      (is (= [#{"blah does not match range type :bool"} graph]
             (check-constraint (range-type-constraint "/str" :bool) graph :a)))
      (is (= [#{(str t " does not match range type :bool")} graph]
             (check-constraint (range-type-constraint "/time" :bool) graph :a)))
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/bool" :bool) graph :a)))
      (is (= [#{":b does not match range type :bool"} graph]
             (check-constraint (range-type-constraint "/relation" :bool) graph :a)))
      (is (= [#{":bar does not match range type :bool"} graph]
             (check-constraint (range-type-constraint "/enum" :bool) graph :a)))
      (is (= [#{"7 does not match range type /range_type"} graph]
             (check-constraint (range-type-constraint "/num" "/range_type") graph :a)))
      (is (= [#{"blah does not match range type /range_type"} graph]
             (check-constraint (range-type-constraint "/str" "/range_type") graph :a)))
      (is (= [#{(str t " does not match range type /range_type")} graph]
             (check-constraint (range-type-constraint "/time" "/range_type") graph :a)))
      (is (= [#{"false does not match range type /range_type"} graph]
             (check-constraint (range-type-constraint "/bool" "/range_type") graph :a)))
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/relation" "/range_type") graph :a)))
      (is (= [#{":bar does not match range type /range_type"} graph]
             (check-constraint (range-type-constraint "/enum" "/range_type") graph :a)))
      (is (= [#{"7 does not match range type #{:bar :foo}"} graph]
             (check-constraint (range-type-constraint "/num" #{:foo :bar}) graph :a)))
      (is (= [#{"blah does not match range type #{:bar :foo}"} graph]
             (check-constraint (range-type-constraint "/str" #{:foo :bar}) graph :a)))
      (is (= [#{(str t " does not match range type #{:bar :foo}")} graph]
             (check-constraint (range-type-constraint "/time" #{:foo :bar}) graph :a)))
      (is (= [#{"false does not match range type #{:bar :foo}"} graph]
             (check-constraint (range-type-constraint "/bool" #{:foo :bar}) graph :a)))
      (is (= [#{":b does not match range type #{:bar :foo}"} graph]
             (check-constraint (range-type-constraint "/relation" #{:foo :bar}) graph :a)))
      (is (= [#{} graph]
             (check-constraint (range-type-constraint "/enum" #{:foo :bar}) graph :a))))))

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

(deftest test-schema-to-constraint
  (let [schema {
          :value-type :number,
          :mutually-exclusive #{"/bar" "/baz"},
          :preds {
            "/foo" {
              :required true,
              :default 12,
              :unique true,
            },
            "/bar" {
              :unique true,
            },
            "/baz" {
              :range-type :number,
              :required true,
            },
            "/fip" {
              :excludes #{"/bar"},
            },
            "/bop" {
              :excludes #{"/baz"},
            },
          },
        },
        constraint (schema-to-constraint schema)]
    (testing "Test constraint generated from schema"
      (let [graph (write-path (create-graph-v3) #{:a} {}
                              "not a num" {"/bar" 7,
                                           "/baz" "not a number",
                                           "/fip" 9,
                                           "/bop" 10})]
        (is (= [#{"not a number does not match range type :number"
                  "#{\"/baz\" \"/bar\"} on :a are mutually exclusive"
                  "/bop on :a excludes #{\"/baz\"}"
                  "/fip on :a excludes #{\"/bar\"}"
                  "not a num does not match value type :number"}
                (get-raw-data (write-o graph :a "/foo" 12))]
               (check-constraint-raw-data constraint graph :a))))
      (let [graph (write-path (create-graph-v3) #{:a} {}
                              3 {"/foo" #{13 14}})]
        (is (= [#{"/foo is unique on :a, but multiple present"
                  "/baz is required on :a, but not present"}
                (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a))))
      (let [graph (write-path (create-graph-v3) #{:a} {}
                              3 {"/baz" 8,
                                 "/fip" 9})]
        (is (= [#{} (get-raw-data (write-o graph :a "/foo" 12))]
               (check-constraint-raw-data constraint graph :a)))))))
