(ns transcendentalism.graph-v3-test
  (:require [clojure.test :refer :all]
            [transcendentalism.graph-v3 :refer :all]))

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
