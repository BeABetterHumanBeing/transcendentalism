(ns transcendentalism.constraint-test
  (:require [clojure.test :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.tablet :refer :all]
            [transcendentalism.time :refer :all]))

(defn check-constraint-raw-data
  [constraint graph sub]
  (let [[errors new-graph] (check-constraint constraint graph sub)]
    [errors (get-raw-data new-graph)]))

(deftest test-range-constraint
  (let [t (get-hours-ago 20),
        graph (write-path (build-component (create-graph) :t #{} {})
                          :a {}
                          {"/num" 7,
                           "/str" "blah",
                           "/time" t,
                           "/bool" false,
                           "/relation" :b,
                           "/enum" :bar}
                          "/relation" {"/type" :t})]
    (testing "Test range constraint"
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" :number) graph :a)))
      (is (= [#{"blah does not match range type :number"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" :number) graph :a)))
      (is (= [#{(str t " does not match range type :number")} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" :number) graph :a)))
      (is (= [#{"false does not match range type :number"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" :number) graph :a)))
      (is (= [#{":b does not match range type :number"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" :number) graph :a)))
      (is (= [#{":bar does not match range type :number"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" :number) graph :a)))
      (is (= [#{"7 does not match range type :string"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" :string) graph :a)))
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" :string) graph :a)))
      (is (= [#{(str t " does not match range type :string")} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" :string) graph :a)))
      (is (= [#{"false does not match range type :string"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" :string) graph :a)))
      (is (= [#{":b does not match range type :string"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" :string) graph :a)))
      (is (= [#{":bar does not match range type :string"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" :string) graph :a)))
      (is (= [#{"7 does not match range type :time"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" :time) graph :a)))
      (is (= [#{"blah does not match range type :time"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" :time) graph :a)))
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" :time) graph :a)))
      (is (= [#{"false does not match range type :time"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" :time) graph :a)))
      (is (= [#{":b does not match range type :time"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" :time) graph :a)))
      (is (= [#{":bar does not match range type :time"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" :time) graph :a)))
      (is (= [#{"7 does not match range type :bool"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" :bool) graph :a)))
      (is (= [#{"blah does not match range type :bool"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" :bool) graph :a)))
      (is (= [#{(str t " does not match range type :bool")} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" :bool) graph :a)))
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" :bool) graph :a)))
      (is (= [#{":b does not match range type :bool"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" :bool) graph :a)))
      (is (= [#{":bar does not match range type :bool"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" :bool) graph :a)))
      (is (= [#{"7 does not match range type :t"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" :t) graph :a)))
      (is (= [#{"blah does not match range type :t"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" :t) graph :a)))
      (is (= [#{(str t " does not match range type :t")} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" :t) graph :a)))
      (is (= [#{"false does not match range type :t"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" :t) graph :a)))
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" :t) graph :a)))
      (is (= [#{":bar does not match range type :t"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" :t) graph :a)))
      (is (= [#{"7 does not match range type #{:bar :foo}"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/num" #{:foo :bar}) graph :a)))
      (is (= [#{"blah does not match range type #{:bar :foo}"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/str" #{:foo :bar}) graph :a)))
      (is (= [#{(str t " does not match range type #{:bar :foo}")} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/time" #{:foo :bar}) graph :a)))
      (is (= [#{"false does not match range type #{:bar :foo}"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/bool" #{:foo :bar}) graph :a)))
      (is (= [#{":b does not match range type #{:bar :foo}"} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/relation" #{:foo :bar}) graph :a)))
      (is (= [#{} (get-raw-data graph)]
             (check-constraint-raw-data (range-type-constraint "/enum" #{:foo :bar}) graph :a))))))

(defn validate-raw-data
  [graph]
  (let [[errors new-graph] (validate graph)]
    [errors (get-raw-data new-graph)]))

(deftest test-required-constraint
  (let [req-no-default (required-pred-constraint "/foo"),
        req-w-default (required-pred-constraint "/bar" 5),
        noncompliant-graph (write-v (create-graph) :a 3),
        compliant-graph (write-path noncompliant-graph :a {} {"/foo" 2,
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
        zero-graph (write-v (create-graph) :a 3),
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
        foo-graph (write-o (create-graph) :a "/foo" 1),
        bar-graph (write-o (create-graph) :a "/bar" 2),
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
      (let [graph (write-path (create-graph) :a {}
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
      (let [graph (write-path (create-graph) :a {}
                              3 {"/foo" #{13 14}})]
        (is (= [#{"/foo is unique on :a, but multiple present"
                  "/baz is required on :a, but not present"}
                (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a))))
      (let [graph (write-path (create-graph) :a {}
                              3 {"/baz" 8,
                                 "/fip" 9})]
        (is (= [#{} (get-raw-data (write-o graph :a "/foo" 12))]
               (check-constraint-raw-data constraint graph :a)))))))

(deftest test-nested-schema-to-constraint
  (let [schema {
          :value-type :number,
          :preds {
            "/foo" {
              :value-type :range-type,
              :unique true,
              :preds {
                "/bar" {
                  :value-type :other-range-type,
                  :required true,
                  :preds {
                    "/baz" {
                      :range-type :string,
                      :required true,
                      :default "default",
                    },
                  },
                },
              },
            },
          },
        },
        constraint (schema-to-constraint schema),
        range-type-root (reify TypeRoot
                          (get-constraint [root] constraint)
                          (is-abstract [root] false)),
        other-range-type-root (reify TypeRoot
                                (get-constraint [root] nil)
                                (is-abstract [root] false)),
        base-graph (-> (create-graph)
                       (write-path :b {}
                                   {"/type" :range-type}
                                   "/type" range-type-root)
                       (write-path :c {}
                                   {"/type" :other-range-type}
                                   "/type" other-range-type-root))]
    (testing "Test nested constraint generated from schema"
      (let [graph (write-path base-graph :a1 {}
                              7 {"/foo" :a2}
                              "/foo" :b {"/bar" :a3}
                              "/bar" :c {"/baz" 100})]
        (is (= [#{"100 does not match range type :string"} (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a1))))
      (let [graph (write-path base-graph :a1 {}
                              7 {"/foo" :a2}
                              "/foo" :c {"/bar" :a3}
                              "/bar" :b)]
        (is (= [#{":c does not match value type :range-type"
                  ":b does not match value type :other-range-type"}
                (get-raw-data (write-o graph :a3 "/baz" "default"))]
               (check-constraint-raw-data constraint graph :a1))))
      (let [graph (write-path base-graph :a1 {}
                              "not a number" {"/foo" #{:a2 :a3}}
                              "/foo" :b)]
        (is (= [#{"not a number does not match value type :number"
                  "/foo is unique on :a1, but multiple present"
                  "/bar is required on :a2, but not present"
                  "/bar is required on :a3, but not present"
                  " does not match value type :range-type"}
                (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a1)))))))

(deftest test-ordered-schema
  (let [constraint (schema-to-constraint {
                       :preds {
                          "/point" {
                            :value-type :string,
                            :ordered true,
                          },
                        },
                     })]
    (testing "Test order validation"
      (let [graph (write-path (create-graph) :a {}
                              {"/point" :b1}
                              "/point" "My String")]
        (is (= [#{"/order is required on :b1, but not present"}
                (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a))))
      (let [graph (-> (create-graph)
                      (write-path :a {} {"/point" #{:b1 :b2}})
                      (write-path :b1 {} "One" {"/order" 2})
                      (write-path :b2 {} "Two" {"/order" 2}))]
        (is (= [#{":a /point has non-distinct ordinals (2 2)"}
                (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a))))
      (let [graph (-> (create-graph)
                      (write-path :a {} {"/point" #{:b1 :b2 :b3}})
                      (write-path :b1 {} "One" {"/order" 1})
                      (write-path :b2 {} "Two" {"/order" 2})
                      (write-path :b3 {} "Thr" {"/order" 3}))]
        (is (= [#{} (get-raw-data graph)]
               (check-constraint-raw-data constraint graph :a)))))))

(deftest test-schema-validation
  (let [type-graph
          (-> (create-graph)
              (build-component :building-type #{}
                               {
                                 :abstract true,
                                 :preds {
                                   "/size" {
                                     :range-type :number,
                                     :required true,
                                     :unique true,
                                   },
                                 },
                               })
              (build-component :fabrick-type #{:building-type}
                               {
                                 :value-type :string,
                                 :preds {
                                   "/input" {
                                     :required true,
                                     :unique true,
                                     :preds {
                                       "/iron" {
                                         :range-type :number,
                                         :required true,
                                         :unique true,
                                         :default 3,
                                       },
                                       "/coal" {
                                         :range-type :number,
                                         :required true,
                                         :unique true,
                                       },
                                     },
                                   },
                                   "/output" {
                                     :required true,
                                     :unique true,
                                     :preds {
                                       "/steel" {
                                         :range-type :number,
                                         :required true,
                                         :unique true,
                                       },
                                     },
                                   },
                                 },
                               })
              (build-component :slot-type #{}
                               {
                                 :value-type #{:water :land :rock},
                                 :preds {
                                   "/contains" {
                                     :range-type :building-type,
                                   },
                                 },
                               }))]
    (testing "Test schema validation"
      (let [graph (write-path type-graph :my-slot {}
                              :land {"/type" :slot-type,
                                     "/contains" :my-building}
                              "/contains" "A Building" {"/type" :building-type,
                                                        "/size" 100})]
        (is (= [#{":my-building has no non-abstract type"} {}]
               (validate-raw-data graph))))
      (let [graph (write-path type-graph :my-slot {}
                              :water {"/type" :slot-type,
                                      "/contains" :my-building}
                              "/contains" "A steelworks" {"/type" :fabrick-type,
                                                          "/input" :i,
                                                          "/output" :o}
                              ["/output" {"/steel" "three"}])]
        (is (= [#{"three does not match range type :number"
                  "/size is required on :my-building, but not present"
                  "/coal is required on :i, but not present"}
                (get-raw-data (write-o (create-graph) :i "/iron" 3))]
               (validate-raw-data graph))))
      (let [graph (write-path type-graph :my-slot {}
                              :water {"/type" :slot-type,
                                      "/contains" :my-building}
                              "/contains" "A steelworks" {"/type" :fabrick-type,
                                                          "/size" 100,
                                                          "/input" :i,
                                                          "/output" :o}
                              #{["/input" {"/iron" 3, "/coal" 2}]
                                ["/output" {"/steel" 3}]})]
        (is (= [#{} {}]
               (validate-raw-data graph)))))))
