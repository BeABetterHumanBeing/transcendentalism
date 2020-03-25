(ns transcendentalism.graph-v3-test
  (:require [clojure.test :refer :all]
            [transcendentalism.graph-v3 :refer :all]))

(deftest graph-read-write-test
  (let [graph (-> (create-graph-v3 "test")
                  (write-v :a 7)
                  (write-v :b 8)
                  (write-o :a "/foo" "lol")
                  (write-o :a "/foo" "kek")
                  (write-o :a "/bar" 21))]
    (testing "Test reading and writing to the graph"
      (is (= "test" (get-graph-name graph)))
      (is (= #{:a :b} (read-ss graph)))
      (is (= 7 (read-v graph :a)))
      (is (= 8 (read-v graph :b)))
      (is (nil? (read-v graph :c)))
      (is (= #{"/foo" "/bar"} (read-ps graph :a)))
      (is (empty? (read-ps graph :b)))
      (is (empty? (read-ps graph :c)))
      (is (= #{"lol" "kek"} (read-os graph :a "/foo")))
      (is (= #{21} (read-os graph :a "/bar")))
      (is (empty? (read-os graph :b "/foo")))
      (is (empty? (read-os graph :c "/foo")))
      (is (= #{"lol" "kek" 21} (read-os graph :a #"/\w*")))
      (is (empty? (read-os graph :a #"/\d*"))))))

(deftest graph-overlay-test
  (let [base-graph (-> (create-graph-v3 "test-base")
                       (write-v :a 7)
                       (write-v :b 8)
                       (write-v :c 9)
                       (write-o :a "/foo" "lol")
                       (write-o :a "/bar" "kek")),
        graph (-> (create-graph-v3 "test-overlay" {} base-graph)
                  (write-v :a 10)
                  (write-o :a "/bar" "eke")
                  (write-o :b "/foo" "nom")
                  (write-o :d "/baz" "hah"))]
    (testing "Test reading and writing to an overlay graph"
      (is (= "test-overlay" (get-graph-name graph)))
      (is (= #{:a :b :c :d} (read-ss graph)))
      (is (= 10 (read-v graph :a)))
      (is (= 8 (read-v graph :b)))
      (is (= 9 (read-v graph :c)))
      (is (nil? (read-v graph :d)))
      (is (= #{"/foo" "/bar"} (read-ps graph :a)))
      (is (= #{"/foo"} (read-ps graph :b)))
      (is (empty? (read-ps graph :c)))
      (is (= #{"/baz"} (read-ps graph :d)))
      (is (= #{"lol"} (read-os graph :a "/foo")))
      (is (= #{"kek" "eke"} (read-os graph :a "/bar")))
      (is (= #{"nom"} (read-os graph :b "/foo")))
      (is (= #{"hah"} (read-os graph :d "/baz"))))))

(deftest graph-registry-test
  (let [graph-a (-> (create-graph-v3 "graph-a")
                    (write-v :a 1)),
        graph-b (-> (create-graph-v3 "graph-b")
                    (write-v :b 2)),
        base-registry (create-graph-registry
                        (graphs-to-registry-data graph-a graph-b) nil),
        init-registry (create-graph-registry base-registry),
        final-registry (-> init-registry
                         (update-graph "graph-a"
                           (-> (get-graph init-registry "graph-a")
                               (write-o :a "/foo" "lol")))
                         (update-graph "graph-b"
                           (-> (get-graph init-registry "graph-b")
                               (write-v :b 3))))]
    (testing "Test updating graphs through a registry"
      (is (= #{"graph-a" "graph-b"} (get-graph-names final-registry)))
      (let [graph (get-graph final-registry "graph-a")]
        (is (= 1 (read-v graph :a)))
        (is (= #{"/foo"} (read-ps graph :a))))
      (let [graph (get-graph final-registry "graph-b")]
        (is (= 3 (read-v graph :b)))))))

(deftest tablet-test
  (let [graph-a (-> (create-graph-v3 "graph-a")
                    (write-v :a1 7)
                    (write-v :a2 8)
                    (write-o :a1 "/foo" "blah")
                    (write-o :a2 "/foo" "yack")),
        graph-b (-> (create-graph-v3 "graph-b")
                    (write-v :b1 1)
                    (write-v :b2 2)
                    (write-o :b1 "/bar" "blue")
                    (write-o :b2 "/bar" "yellow")),
        registry (create-graph-registry {"graph-a" graph-a,
                                         "graph-b" graph-b}
                                        nil)
        tablet-a (-> (create-read-write-tablet {} {:c 100, :d 200} registry)
                     (add-entry :a1 "graph-a" {:my-meta "cat"})
                     (add-entry :a2 "graph-a" {:my-meta "dog"})
                     (write-v :a1 701)
                     (write-v :a2 801)),
        tablet-b (-> (create-read-write-tablet {} {:e 300} registry)
                     (add-entry :b1 "graph-b" {:my-meta "mouse"})
                     (add-entry :b2 "graph-b" {:my-meta "rat"})
                     (update-entry :b1 :b3 "graph-b" {:my-meta "cow"})
                     (write-o :b2 "/baz" "paper")
                     (write-o :b3 "/baz" "scissors")),
        tablet-ab (-> (merge-tablet tablet-a tablet-b
                                    (fn [metadata]
                                      (assoc metadata :merge-info true)))
                      (write-o :a1 "/snu" "hello")
                      (write-o :b2 "/snu" "hola"))]
    (testing "Test reading and writing to a tablet"
      (is (= (get-bindings tablet-a) (get-bindings tablet-ab)))
      (is (= #{:a1 :a2 :b2 :b3} (read-ss tablet-ab)))
      (is (= {:graph-name "graph-a" :my-meta "cat"} (get-metadata tablet-ab :a1)))
      (is (= {:graph-name "graph-a" :my-meta "dog"} (get-metadata tablet-ab :a2)))
      (is (= {:graph-name "graph-b" :merge-info true :my-meta "rat"}
             (get-metadata tablet-ab :b2)))
      (is (= {:graph-name "graph-b" :merge-info true :my-meta "cow"}
             (get-metadata tablet-ab :b3)))
      (is (= 701 (read-v tablet-ab :a1)))
      (is (= 801 (read-v tablet-ab :a2)))
      (is (= 2 (read-v tablet-ab :b2)))
      (is (nil? (read-v tablet-ab :b3)))
      (is (= #{"/foo" "/snu"} (read-ps tablet-ab :a1)))
      (is (= #{"/foo"} (read-ps tablet-ab :a2)))
      (is (= #{"/bar" "/baz" "/snu"} (read-ps tablet-ab :b2)))
      (is (= #{"/baz"} (read-ps tablet-ab :b3)))
      (is (= #{"blah"} (read-os tablet-ab :a1 "/foo")))
      (is (= #{"hello"} (read-os tablet-ab :a1 "/snu")))
      (is (= #{"yack"} (read-os tablet-ab :a2 "/foo")))
      (is (= #{"yellow"} (read-os tablet-ab :b2 "/bar")))
      (is (= #{"paper"} (read-os tablet-ab :b2 "/baz")))
      (is (= #{"hola"} (read-os tablet-ab :b2 "/snu")))
      (is (= #{"scissors"} (read-os tablet-ab :b3 "/baz"))))))
