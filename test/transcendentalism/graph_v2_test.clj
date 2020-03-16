(ns transcendentalism.graph-v2-test
  (:require [clojure.test :refer :all]
            [transcendentalism.graph-v2 :refer :all]))

(defn- nodes-to-spopv
  [nodes]
  (into #{} (map to-spopv nodes)))

(defn- triples-to-opv
  [triples]
  (into #{} (map to-opv triples)))

(defn- properties-to-v
  [properties]
  (into #{} (map to-v properties)))

(deftest empty-graph-test
  (let [graph-builder (create-graph-builder),
        graph (create-graph (get-built-graph graph-builder))]
    (testing "Test empty graph"
      (is (= #{} (get-all-types graph))))))

(deftest graph-nodes-test
  (let [graph-builder (create-graph-builder),
        type-a-builder (get-node-builder graph-builder "/a"),
        type-b-builder (get-node-builder graph-builder "/b")]
    (build-node type-a-builder :a1)
    (build-node type-a-builder :a2)
    (build-node type-b-builder :b1)
    (let [graph (create-graph (get-built-graph graph-builder)),
          expected-a1 (->SPOPV :a1 {"/type/a" #{nil}}),
          expected-a2 (->SPOPV :a2 {"/type/a" #{nil}}),
          expected-b1 (->SPOPV :b1 {"/type/b" #{nil}})]
      (testing "Test graph with only nodes"
        (is (= #{"/type/a", "/type/b"} (get-all-types graph)))
        (is (= expected-a1 (to-spopv (get-node graph :a1))))
        (is (= expected-a2 (to-spopv (get-node graph :a2))))
        (is (= expected-b1 (to-spopv (get-node graph :b1))))
        (is (= nil (get-node graph :c1)))
        (is (= #{expected-a1 expected-a2} (nodes-to-spopv (get-nodes graph "/type/a"))))
        (is (= #{expected-b1} (nodes-to-spopv (get-nodes graph "/type/b"))))
        (is (= #{} (get-nodes graph "/type/c")))
        (is (has-type? graph :a1 "/type/a"))
        (is (has-type? graph :a2 "/type/a"))
        (is (has-type? graph :b1 "/type/b"))
        (is (not (has-type? graph :a1 "/type/b")))
        (is (not (has-type? graph :b1 "/type/a")))))))

(deftest graph-triples-test
  (let [graph-builder (create-graph-builder),
        type-a-builder (get-node-builder graph-builder "/a"),
        pred-foo-builder (get-triple-builder type-a-builder "/foo"),
        pred-bar-builder (get-triple-builder type-a-builder "/bar")]
    (build-node type-a-builder :a1) ; Has no predicates.
    (build-triple pred-foo-builder "foo1")
    (build-node type-a-builder :a2) ; Has one predicate.
    (build-triple pred-foo-builder "foo2")
    (build-triple pred-bar-builder "bar1")
    (build-node type-a-builder :a3) ; Has three predicates.
    (let [graph (create-graph (get-built-graph graph-builder)),
          expected-foo1 (->OPV "foo1" {}),
          expected-foo2 (->OPV "foo2" {}),
          expected-bar1 (->OPV "bar1" {}),
          expected-a1 (->SPOPV :a1 {"/type/a" #{nil}}),
          expected-a2 (->SPOPV :a2 {"/type/a" #{nil},
                                    "/foo" #{expected-foo1}}),
          expected-a3 (->SPOPV :a3 {"/type/a" #{nil},
                                    "/foo" #{expected-foo1 expected-foo2},
                                    "/bar" #{expected-bar1}}),
          node-a1 (get-node graph :a1),
          node-a2 (get-node graph :a2),
          node-a3 (get-node graph :a3)]
      (testing "Test graph with nodes with triples"
        (is (= #{"/type/a"} (get-all-types graph)))
        (is (= expected-a1 (to-spopv node-a1)))
        (is (= expected-a2 (to-spopv node-a2)))
        (is (= expected-a3 (to-spopv node-a3)))
        (is (= #{expected-a1 expected-a2 expected-a3}
               (nodes-to-spopv (get-nodes graph "/type/a"))))
        (is (has-type? graph :a1 "/type/a"))
        (is (has-type? graph :a2 "/type/a"))
        (is (has-type? graph :a3 "/type/a"))
        (is (= :a1 (get-sub node-a1)))
        (is (= :a2 (get-sub node-a2)))
        (is (= :a3 (get-sub node-a3)))
        (is (= #{"/type/a"} (get-types node-a1)))
        (is (= #{"/type/a"} (get-types node-a2)))
        (is (= #{"/type/a"} (get-types node-a3)))
        (is (= #{"/type/a"} (get-preds node-a1)))
        (is (= #{"/type/a" "/foo"} (get-preds node-a2)))
        (is (= #{"/type/a" "/foo" "/bar"} (get-preds node-a3)))
        (is (= #{} (get-triples node-a1 "/foo")))
        (is (= #{} (get-triples node-a1 "/bar")))
        (is (= #{expected-foo1} (triples-to-opv (get-triples node-a2 "/foo"))))
        (is (= #{} (get-triples node-a2 "/bar")))
        (is (= #{expected-foo1 expected-foo2}
               (triples-to-opv (get-triples node-a3 "/foo"))))
        (is (= #{expected-bar1} (triples-to-opv (get-triples node-a3 "/bar"))))))))

(deftest graph-properties-test
  (let [graph-builder (create-graph-builder),
        type-a-builder (get-node-builder graph-builder "/a"),
        pred-foo-builder (get-triple-builder type-a-builder "/foo"),
        prop-do-builder (get-property-builder pred-foo-builder "/do"),
        prop-re-builder (get-property-builder pred-foo-builder "/re"),
        prop-mi-builder (get-property-builder pred-foo-builder "/mi")]
    (build-property prop-do-builder 111)
    (build-node type-a-builder :a1) ; Has no triples, and therefore no properties.
    (build-triple pred-foo-builder "foo1")
    (build-node type-a-builder :a2) ; Has one triple, with one property.
    (build-property prop-re-builder 222)
    (build-property prop-re-builder 333)
    (build-triple pred-foo-builder "foo2")
    (build-node type-a-builder :a3) ; Has one triple, with three properties.
    (build-property prop-mi-builder 444) ; No triple has this property.
    (let [graph (create-graph (get-built-graph graph-builder)),
          expected-do (->V 111),
          expected-re1 (->V 222),
          expected-re2 (->V 333),
          expected-foo1 (->OPV "foo1" {"/do" #{expected-do}}),
          expected-foo2 (->OPV "foo2" {"/do" #{expected-do},
                                       "/re" #{expected-re1 expected-re2}}),
          expected-a1 (->SPOPV :a1 {"/type/a" #{nil}}),
          expected-a2 (->SPOPV :a2 {"/type/a" #{nil},
                                    "/foo" #{expected-foo1}}),
          expected-a3 (->SPOPV :a3 {"/type/a" #{nil},
                                    "/foo" #{expected-foo1 expected-foo2}}),
          node-a1 (get-node graph :a1),
          node-a2 (get-node graph :a2),
          node-a3 (get-node graph :a3),
          triples-a2 (get-triples node-a2 "/foo"),
          triples-a3 (get-triples node-a3 "/foo")]
      (testing "Test graph with nodes with a triple with properties"
        (is (= expected-a1 (to-spopv node-a1)))
        (is (= expected-a2 (to-spopv node-a2)))
        (is (= expected-a3 (to-spopv node-a3)))
        (is (= #{"/type/a"} (get-preds node-a1)))
        (is (= #{"/type/a" "/foo"} (get-preds node-a2)))
        (is (= #{"/type/a" "/foo"} (get-preds node-a3)))
        (is (= #{} (get-triples node-a1 "/foo")))
        (is (= #{expected-foo1} (triples-to-opv triples-a2)))
        (is (= #{expected-foo1 expected-foo2} (triples-to-opv triples-a3))))
      (let [triple-a2-foo1 (first triples-a2),
            triple-a3-foo1 (first (filter #(= (get-obj %) "foo1") triples-a3)),
            triple-a3-foo2 (first (filter #(= (get-obj %) "foo2") triples-a3)),
            do-props-a2-foo1 (get-properties triple-a2-foo1 "/do"),
            re-props-a2-foo1 (get-properties triple-a2-foo1 "/re"),
            mi-props-a2-foo1 (get-properties triple-a2-foo1 "/mi"),
            do-props-a3-foo1 (get-properties triple-a3-foo1 "/do"),
            re-props-a3-foo1 (get-properties triple-a3-foo1 "/re"),
            mi-props-a3-foo1 (get-properties triple-a3-foo1 "/mi"),
            do-props-a3-foo2 (get-properties triple-a3-foo2 "/do"),
            re-props-a3-foo2 (get-properties triple-a3-foo2 "/re"),
            mi-props-a3-foo2 (get-properties triple-a3-foo2 "/mi")]
        (testing "Testing the triples themselves"
          (is (= "foo1" (get-obj triple-a2-foo1)))
          (is (= "foo1" (get-obj triple-a3-foo1)))
          (is (= "foo2" (get-obj triple-a3-foo2)))
          (is (= #{"/do"} (get-props triple-a2-foo1)))
          (is (= #{"/do"} (get-props triple-a3-foo1)))
          (is (= #{"/do" "/re"} (get-props triple-a3-foo2)))
          (is (= #{expected-do} (properties-to-v do-props-a2-foo1)))
          (is (= #{} re-props-a2-foo1))
          (is (= #{} mi-props-a2-foo1))
          (is (= #{expected-do} (properties-to-v do-props-a3-foo1)))
          (is (= #{} re-props-a3-foo1))
          (is (= #{} mi-props-a3-foo1))
          (is (= #{expected-do} (properties-to-v do-props-a3-foo2)))
          (is (= #{expected-re1 expected-re2} (properties-to-v re-props-a3-foo2)))
          (is (= #{} mi-props-a3-foo2)))
        (let [prop-a3-foo2-re1 (filter #(= (get-val %) 222) re-props-a3-foo2),
              prop-a3-foo2-re2 (filter #(= (get-val %) 333) re-props-a3-foo2)]
          (testing "Testing the properties"
            (is (= expected-do (to-v (first do-props-a2-foo1))))
            (is (= expected-do (to-v (first do-props-a3-foo1))))
            (is (= expected-do (to-v (first do-props-a3-foo2))))
            (is (= expected-re1 (to-v (first prop-a3-foo2-re1))))
            (is (= expected-re2 (to-v (first prop-a3-foo2-re2))))))))))
