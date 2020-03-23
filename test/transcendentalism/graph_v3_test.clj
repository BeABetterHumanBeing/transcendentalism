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
      (is (= nil (read-v graph :c)))
      (is (= #{"/foo" "/bar"} (read-ps graph :a)))
      (is (= #{} (read-ps graph :b)))
      (is (= #{} (read-ps graph :c)))
      (is (= #{"lol" "kek"} (read-os graph :a "/foo")))
      (is (= #{21} (read-os graph :a "/bar")))
      (is (= #{} (read-os graph :b "/foo")))
      (is (= #{} (read-os graph :c "/foo")))
      (is (= #{"lol" "kek" 21} (read-os graph :a #"/\w*")))
      (is (= #{} (read-os graph :a #"/\d*"))))))
