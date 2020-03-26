(ns transcendentalism.graph-v3-test
  (:require [clojure.test :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.toolbox :refer :all]))

(deftest graph-read-write-test
  (let [graph (-> (create-graph-v3)
                  (write-v :a 7)
                  (write-v :b 8)
                  (write-o :a "/foo" "lol")
                  (write-o :a "/foo" "kek")
                  (write-o :a "/bar" 21))]
    (testing "Test reading and writing to the graph"
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
  (let [base-graph (-> (create-graph-v3)
                       (write-v :a 7)
                       (write-v :b 8)
                       (write-v :c 9)
                       (write-o :a "/foo" "lol")
                       (write-o :a "/bar" "kek")),
        graph (-> (create-graph-v3 {} base-graph)
                  (write-v :a 10)
                  (write-o :a "/bar" "eke")
                  (write-o :b "/foo" "nom")
                  (write-o :d "/baz" "hah"))]
    (testing "Test reading and writing to an overlay graph"
      ; (is (= "test-overlay" (get-graph-name graph)))
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

(deftest tablet-test
  (let [graph (-> (create-graph-v3)
                  (write-v :a1 7)
                  (write-v :a2 8)
                  (write-o :a1 "/foo" "blah")
                  (write-o :a2 "/foo" "yack")
                  (write-v :b1 1)
                  (write-v :b2 2)
                  (write-o :b1 "/bar" "blue")
                  (write-o :b2 "/bar" "yellow")),
        tablet-a (-> (create-read-write-tablet
                       {} {:c 100, :d 200} (create-graph-v3 {} graph))
                     (add-entry :a1 {:my-meta "cat"})
                     (add-entry :a2 {:my-meta "dog"})
                     (write-v :a1 701)
                     (write-v :a2 801)),
        tablet-b (-> (create-read-write-tablet
                       {} {:e 300} (create-graph-v3 {} graph))
                     (add-entry :b1 {:my-meta "mouse"})
                     (add-entry :b2 {:my-meta "rat"})
                     (update-entry :b1 :b3 {:my-meta "cow"})
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
      (is (= {:my-meta "cat"} (get-metadata tablet-ab :a1)))
      (is (= {:my-meta "dog"} (get-metadata tablet-ab :a2)))
      (is (= {:merge-info true :my-meta "rat"} (get-metadata tablet-ab :b2)))
      (is (= {:merge-info true :my-meta "cow"} (get-metadata tablet-ab :b3)))
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

(deftest graph-path-test
  (let [graph (create-graph-v3),
        ret-1 (fn [_] 1), ; Some closure
        ret-a3 (fn [_] :a3), ; A virtual sub
        tablet (follow-all (create-read-write-tablet {:a1 {}}
                                              {:binding-1 10, :binding-2 20}
                                              graph)
                           (write-obj "/foo" :a2) (read-pred "/foo")
                           (write-val ret-1)
                           (write-obj "/foo" ret-a3) (read-pred "/foo")
                           (write-val 2)
                           (write-obj "/baz" 9)
                           (write-obj "/foo" :a1) (read-pred "/foo")
                           (write-obj "/baz" :b1) (read-pred "/baz")
                           (write-val :binding-1)
                           (write-obj "/bar" :binding-2)
                           (write-obj "/bar" 4)),
        read-tablet (create-read-write-tablet {:a1 {}} {}
                                              (get-target-graph tablet))]
    (testing "Test graph path components"
      (is (= #{:a2}
             (get-results (follow-all read-tablet
                                      (read-pred "/foo")))))
      (is (= #{1}
             (get-results (follow-all read-tablet
                                      (read-pred "/foo")
                                      (read-self)))))
      (is (= #{20 4}
             (get-results (follow-all read-tablet
                                      (path-chain
                                        (read-pred "/baz")
                                        (read-pred "/bar"))))))
      (is (= #{9 10}
             (get-results (follow-all read-tablet
                                      (path-chain
                                        (path-all
                                          (path-chain
                                            (read-pred "/foo")
                                            (read-pred "/foo"))
                                          (path-nil))
                                        (read-pred "/baz")
                                        (read-self)
                                        )))))
      (is (= #{:b1 9}
             (get-results (follow-all read-tablet
                                      (path-chain
                                        (read-star
                                          (read-pred "/foo"))
                                        (read-pred "/baz"))))))
      (is (= {:a3 {:o :a2},
              4 {:o :b1}}
             (get-results (follow-all read-tablet
                                      (path-chain
                                        (read-pred #".*")
                                        (path-nil
                                          (fn [tablet sub metadata]
                                            (assoc metadata :o sub)))
                                        (read-pred #".*")
                                        (path-nil
                                          (fn [tablet sub metadata]
                                            (if (and (number? sub) (> sub 10))
                                                nil
                                                metadata)))))
                          (fn [data] data)))))))

(deftest simplified-graph-path-test
  (let [footnoter (fn [sub] (keyword (str (name sub) "-f"))),
        graph (write-path (create-graph-v3) #{:a1} {:no-val "X"}
                [:no-val {"/foo" :a2,
                          "/bar" :a5}
                 #{["/foo" :no-val {"/f" footnoter,
                                    "/foo" :a3}
                    #{["/f" 2]
                      ["/foo" "A" {"/foo" :a4,
                                   "/bar" :a6}
                       #{["/foo" "B" {"/f" footnoter,
                                      "/bar" :a7}
                          #{["/f" 5]
                            ["/bar" :no-val {"/f" footnoter}
                             "/f" 6]}]
                         ["/bar" "C" {"/f" footnoter,
                                      "/bar" :a9}
                          #{["/f" 3]
                            ["/bar" :no-val {"/bar" :a10}
                             "/bar" "D" {"/f" footnoter}
                             "/f" 4]}]}]}]
                   ["/bar" :no-val {"/bar" :a8}
                    "/bar" :no-val {"/f" footnoter,
                                    "/epsilon" #{:a2 :a9}}
                    "/f" 1]}])]
    (testing "Test using simplified graph paths, and metadata-based reads"
      (is (= [1 2 3 4 5 6]
             (get-results (follow-all
                            (create-read-write-tablet {:a1 {}} {} graph)
                            (build-path
                              [(p* ["/foo" (p! (fn [tablet sub metadata]
                                                 (assoc metadata :foo
                                                   (inc (metadata :foo 0)))))])
                               (p* ["/bar" (p! (fn [tablet sub metadata]
                                                 (assoc metadata :bar
                                                   (inc (metadata :bar 0)))))])
                               "/f" "/"
                              ]))
                          (fn [data]
                            (sort (compare-by-priority data :foo :bar)
                                  (keys data))))))
      (is (= #{"A" "B" "C" "D"}
             (read-path graph #{:a1}
               [#{"/foo" "/bar"}
                #{"/foo" "/bar"}
                #{"/epsilon" ""}
                #{"/foo" "/bar"}
                "/"]))))))