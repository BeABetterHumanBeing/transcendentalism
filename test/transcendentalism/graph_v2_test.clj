(ns transcendentalism.graph-v2-test
  (:require [clojure.test :refer :all]
            [transcendentalism.graph-v2 :refer :all]))

(defn- nodes-to-spopv
  [nodes]
  (into #{} (map to-spopv nodes)))

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
          expected-a1 (->SPOPV :a1 (->POPV {"/type/a" #{}})),
          expected-a2 (->SPOPV :a2 (->POPV {"/type/a" #{}})),
          expected-b1 (->SPOPV :b1 (->POPV {"/type/b" #{}}))]
      (testing "Test empty graph"
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
