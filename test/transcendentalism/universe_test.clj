(ns transcendentalism.universe-test
  (:require [clojure.test :refer :all]
            [transcendentalism.cljc.universe :refer :all]))

(deftest primitive-type-test
  (testing "Testing primitive types"
    (is (= :number (value-type -57)))
    (is (= :string (value-type "foo")))
    (is (= :boolean (value-type false)))
    (is (= :keyword (value-type :moo)))
    (is (= :portal (value-type (->Portal "file/path" :star-name))))))
