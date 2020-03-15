(ns transcendentalism.constraint-v2-test
  (:require [clojure.test :refer :all]
            [transcendentalism.constraint-v2 :refer :all]))

(use 'transcendentalism.graph-v2)

(def test-schema {
  :types {
    "/person" {
      :super-type "/thing",
      :predicates {
        "/name" {
          :range-type :string,
          :required true,
          :constraints [
            (reify Constraint
              (validate [constraint graph triple]
                (let [name (get-obj triple)]
                  (if (> (count name) 20)
                      #{(str name " has more than 20 characters")}
                      #{}))))
          ],
        },
        "/age" {
          :range-type :time,
          :unique true,
          :required true,
        },
        "/location" {
          :range-type "/place",
          :required true,
          :unique true,
        },
        "/favorite_thing" {
          :range-type "/thing",
          :properties {
            "/order" {
              :range-type :number,
              :required true,
              :unique true,
            },
          },
          :meta-constraints [
            (reify Constraint
              (validate [constraint graph node]
                (let [fave-things (get-triples node "/favorite_thing"),
                      fave-ordinals (map #(get-properties % "/order") fave-things)]
                  (if (distinct? fave-ordinals)
                      #{}
                      #{(str fave-ordinals " are not distinct")}))))
          ],
        },
      },
      ; TODO - add type constraints and meta-constraints
    },
    "/place" {
      :predicates {
        "/coords" {
          :range-type nil,
          :required true,
          :unique true,
          :properties {
            "/lat" {
              :range-type :number,
              :required true,
              :unique true,
              :constraints [
                (reify Constraint
                  (validate [constraint graph property]
                    (let [val (get-val property)]
                      (if (or (< val -90) (> val 90))
                          #{"/lat must be in [-90, 90]"}
                          #{}))))
              ],
            },
            "/lng" {
              :range-type :number,
              :required true,
              :unique true,
              :constraints [
                (reify Constraint
                  (validate [constraint graph property]
                    (let [val (get-val property)]
                      (if (or (< val -180) (> val 180))
                          #{"/lng must be in [-180, 180]"}
                          #{}))))
              ],
            },
          },
        },
      },
    },
    "/thing" {
      :predicates {
        "/texture" {
          :range-type [:hard :soft],
          :required true,
          :unique true,
          :properties {
            "/kind_of_softness" {
              :range-type [:feathery :pillowy :fuzzy],
              :unique true,
              :exclusive #{"/kind_of_hardness"},
              :meta-constraints [
                (reify Constraint
                  (validate [constraint graph triple]
                    (if (= (get-obj triple) :hard)
                        #{}
                        #{"/kind_of_hardness requires that /texture be :hard"})))
              ],
            },
            "/kind_of_hardness" {
              :range-type [:pointy :rocky :steely],
              :unique true,
              :exclusive #{"/kind_of_softness"},
              :meta-constraints [
                (reify Constraint
                  (validate [constraint graph triple]
                    (if (= (get-obj triple) :soft)
                        #{}
                        #{"/kind_of_softness requires that /texture be :soft"})))
              ],
            },
          },
        },
      },
    },
  },
})

(def graph-constraints (create-graph-constraints test-schema))

(deftest empty-graph-test
  (testing "Validate empty graph"
    (is (= #{} (validate graph-constraints nil (create-graph (->G {} {})))))))
