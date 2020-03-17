(ns transcendentalism.constraint-v2-test
  (:require [clojure.set :as set]
            [clojure.test :refer :all]
            [transcendentalism.constraint-v2 :refer :all]))

(use 'transcendentalism.graph-v2
     'transcendentalism.time)

(def test-schema {
  :types {
    "/type/person" {
      :super-type "/type/thing",
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
        "/parent" {
          :range-type "/type/person"
          :unique true,
          :exclusive #{"/child"},
        },
        "/child" {
          :range-type "/type/person",
          :unique true,
          :exclusive #{"/parent"},
        },
        "/birthday" {
          :range-type :time,
          :unique true,
        },
        "/location" {
          :range-type "/type/place",
          :required true,
          :unique true,
        },
        "/favorite_thing" {
          :range-type "/type/thing",
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
                      fave-orders (map #(first (get-properties % "/order")) fave-things),
                      fave-ordinals (into [] (map get-val fave-orders))]
                  (if (or (empty? fave-ordinals)
                          (apply distinct? fave-ordinals))
                      #{}
                      #{(str "/orders " fave-ordinals " are not distinct")}))))
          ],
        },
      },
      :constraints [
        (reify Constraint
          (validate [constraint graph node]
            ; If you're birthday was over twenty years ago, you cannot like
            ; hard things (totally contrived, but it's for testing purposes).
            (let [birthday (first (get-triples node "/birthday"))]
              (if (or (nil? birthday)
                      (> (hours-ago (to-time (get-obj birthday))) -175320))
                  #{}
                  (let [fave-things (map get-obj (get-triples node "/favorite_thing")),
                        texture-triples
                          (apply set/union
                            (map #(get-triples (get-node graph %) "/texture") fave-things)),
                        hard-textures (filter #(= (get-obj %) :hard) texture-triples)]
                    (if (empty? hard-textures)
                        #{}
                        #{"People over 20 can't like hard things"}))))))
      ],
      :meta-constraints [
        (reify Constraint
          (validate [constraint _ graph]
            ; The graph composed of 'parents' must be acyclic.
            (let [people (into #{} (map get-sub (get-nodes graph "/type/person"))),
                  person-parent (reduce
                                  (fn [result person]
                                    (assoc result
                                      person
                                      (map get-obj (get-triples (get-node graph person) "/parent"))))
                                  {} people)]
              (loop [prev #{},
                     curr (into #{} (keys person-parent))]
                (if (empty? curr)
                    #{}
                    (if (= prev curr)
                        #{(str "Found parentage cycle with " prev)}
                        (recur curr
                               (reduce
                                 (fn [result person]
                                   (set/union result (into #{} (person-parent person))))
                                 #{} curr))))))))
      ],
    },
    "/type/place" {
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
    "/type/thing" {
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
                    (let [kind-of-softness (get-properties triple
                                                           "/kind_of_softness")]
                      (if (and (not (empty? kind-of-softness))
                               (= (get-obj triple) :hard))
                          #{"/kind_of_softness requires that /texture be :soft"}
                          #{}))))
              ],
            },
            "/kind_of_hardness" {
              :range-type [:pointy :rocky :steely],
              :unique true,
              :exclusive #{"/kind_of_softness"},
              :meta-constraints [
                (reify Constraint
                  (validate [constraint graph triple]
                    (let [kind-of-hardness (get-properties triple
                                                           "/kind_of_hardness")]
                      (if (and (not (empty? kind-of-hardness))
                               (= (get-obj triple) :soft))
                          #{"/kind_of_hardness requires that /texture be :hard"}
                          #{}))))
              ],
            },
          },
        },
      },
    },
  },
  :constraints [
    (let [allowed-types #{"/type/person" "/type/place" "/type/thing"}]
      (reify Constraint
        (validate [constraint _ graph]
          (reduce
            (fn [result type]
              (if (contains? allowed-types type)
                  result
                  (conj result (str type " is not an allowed type"))))
            #{} (get-all-types graph)))))
  ],
})

(def graph-constraints (create-graph-constraints test-schema))

(defn do-validate
  [node-builder]
  (validate graph-constraints nil
            (create-graph (get-built-graph (create-graph-builder) node-builder))))

(deftest simple-graph-test
  (let [node-builder (create-node-builder)]
    (testing "Validate empty graph"
      (is (= #{} (do-validate node-builder))))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/not_a_pred" "blah" {})
      (build-node node-builder "/not_a_type" :a1 triple-builder))
    (testing "Validate non-existent type"
      (is (= #{"/type/not_a_type is not an allowed type"}
             (do-validate node-builder))))))

(deftest required-triple-test
  (let [node-builder (create-node-builder)]
    (build-node node-builder "/person" :ms-peacock {}) ; Requires triples
    (build-node node-builder "/place" :library {}) ; Requires triples
    (build-node node-builder "/thing" :candlestick {}) ; Requires triples
    (testing "Validate required triples"
      (is (= #{"/name is required" "/texture is required" "/coords is required"
               "/location is required"}
             (do-validate node-builder))))))

(deftest unique-triple-required-property-test
  (let [node-builder (create-node-builder)]
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Ms Peacock" {})
      (build-triple triple-builder "/name" "Jacqueline" {})
      (build-triple triple-builder "/parent" :ms-peacock {})
      (build-triple triple-builder "/parent" :ms-peacock {})
      (build-triple triple-builder "/birthday" (get-hours-ago 20) {})
      (build-triple triple-builder "/birthday" (get-hours-ago 200) {}) ; Violates uniqueness
      (build-triple triple-builder "/location" :library {})
      (build-triple triple-builder "/location" :ballroom {}) ; Violates uniqueness, range-type
      (build-triple triple-builder "/favorite_thing" :candlestick {})
      (build-triple triple-builder "/favorite_thing" :rope {}) ; Violates uniqueness, range-type
      (build-node node-builder "/person" :ms-peacock triple-builder))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/coords" "coords-A" {})
      (build-triple triple-builder "/coords" "coords-B" {}) ; Violates uniqueness
      (build-node node-builder "/place" :library triple-builder))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/texture" :hard {})
      (build-triple triple-builder "/texture" :soft {})
      (build-node node-builder "/thing" :candlestick triple-builder))
    (testing "Validate required triples"
      (is (= #{"/birthday is unique, but found 2"
               "/texture is unique, but found 2"
               "/coords is unique, but found 2"
               "/location is unique, but found 2"
               ":rope does not match range type /type/thing"
               ":ballroom does not match range type /type/place"
               "/lng is required" "/order is required" "/lat is required"}
             (do-validate node-builder))))))

(deftest unique-property-and-range-type-test
  (let [node-builder (create-node-builder)]
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" 0x2323 {}) ; Not a string
      (build-triple triple-builder "/parent" :ms-peacock {})
      (build-triple triple-builder "/birthday" "200 hours ago" {}) ; Not a time
      (build-triple triple-builder "/location" :candlestick {}) ; Not a place
      (let [property-builder (create-property-builder)]
        (build-property property-builder "/order" "not-a-number")
        (build-property property-builder "/order" "not-another-number") ; Violates uniqueness
        (build-triple triple-builder "/location" :library property-builder)) ; Not a thing
      (build-node node-builder "/person" :ms-peacock triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/lat" :not-a-number)
      (build-property property-builder "/lat" :not-another-number) ; Violates uniqueness
      (build-property property-builder "/lng" "not-a-number")
      (build-property property-builder "/lng" "not-another-number") ; Violates uniqueness
      (build-triple triple-builder "/coords" "coords-A" property-builder)
      (build-node node-builder "/place" :library triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/kind_of_hardness" :stony) ; Not a valid enum value
      (build-property property-builder "/kind_of_hardness" :stonier) ; Violates uniqueness
      (build-triple triple-builder "/texture" :hard-and-soft property-builder) ; Not a valid enum value
      (build-node node-builder "/thing" :candlestick triple-builder))
    (testing "Validate unique predicates, and range types"
      (is (= #{"/location is unique, but found 2"
               "/lat is unique, but found 2"
               "/lng is unique, but found 2"
               "/kind_of_hardness is unique, but found 2"
               ":not-a-number does not match range type :number"
               ":not-another-number does not match range type :number"
               "not-a-number does not match range type :number"
               "not-another-number does not match range type :number"
               ":hard-and-soft does not match range type [:hard :soft]"
               ":candlestick does not match range type /type/place"
               ":stony does not match range type [:pointy :rocky :steely]"
               "8995 does not match range type :string"
               ":stonier does not match range type [:pointy :rocky :steely]"
               "200 hours ago does not match range type :time"}
             (do-validate node-builder))))))

(deftest exclusivity-and-range-test
  (let [node-builder (create-node-builder)]
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Ms Peacock with a loooooooooong name" {})
      (build-triple triple-builder "/location" :library {})
      (build-node node-builder "/person" :ms-white triple-builder)
      (build-node node-builder "/person" :mr-green triple-builder)
      (build-triple triple-builder "/parent" :ms-white {})
      (build-triple triple-builder "/child" :mr-green {})
      (build-node node-builder "/person" :ms-peacock triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/lat" 100)
      (build-property property-builder "/lng" 200)
      (build-triple triple-builder "/coords" "coords-A" property-builder)
      (build-node node-builder "/place" :library triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/kind_of_hardness" :rocky)
      (build-property property-builder "/kind_of_softness" :feathery)
      (build-triple triple-builder "/texture" :hard property-builder)
      (build-node node-builder "/thing" :candlestick triple-builder))
    (testing "Validate exclusive properties and triples, and custom range checks"
      (is (= #{"Ms Peacock with a loooooooooong name has more than 20 characters"
               "/lng must be in [-180, 180]"
               "/lat must be in [-90, 90]"
               "/child excludes #{\"/parent\"}, but found #{\"/parent\"}"
               "/parent excludes #{\"/child\"}, but found #{\"/child\"}"
               "/kind_of_hardness excludes #{\"/kind_of_softness\"}, but found #{\"/kind_of_softness\"}"
              "/kind_of_softness excludes #{\"/kind_of_hardness\"}, but found #{\"/kind_of_hardness\"}"}
             (do-validate node-builder))))))

(deftest custom-constraints-test
  (let [node-builder (create-node-builder)]
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Ms Peacock" {})
      (build-triple triple-builder "/birthday" (get-hours-ago 200000) {}) ; Over 20 years
      (let [property-builder (create-property-builder)]
        (build-property property-builder "/order" 1)
        (build-triple triple-builder "/favorite_thing" :candlestick property-builder))
      (let [property-builder (create-property-builder)]
        (build-property property-builder "/order" 1) ; Violates ordering.
        (build-triple triple-builder "/favorite_thing" :rope property-builder))
      (build-triple triple-builder "/location" :library {})
      (build-node node-builder "/person" :ms-peacock triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/kind_of_hardness" :steely)
      (build-triple triple-builder "/texture" :soft property-builder) ; Texture mis-match
      (build-node node-builder "/thing" :rope triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/kind_of_softness" :feathery)
      (build-triple triple-builder "/texture" :hard property-builder) ; Texture mis-match
      (build-node node-builder "/thing" :candlestick triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/lat" 45)
      (build-property property-builder "/lng" 90)
      (build-triple triple-builder "/coords" "coords-A" property-builder)
      (build-node node-builder "/place" :library triple-builder))
    (testing "Validate custom [meta-]constraints"
      (is (= #{"/kind_of_softness requires that /texture be :soft"
               "/kind_of_hardness requires that /texture be :hard"
               "People over 20 can't like hard things"
               "/orders [1 1] are not distinct"}
             (do-validate node-builder))))))

(deftest whole-graph-custom-constraints-test
  (let [node-builder (create-node-builder)]
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Ms Peacock" {})
      (build-triple triple-builder "/location" :library {})
      (build-triple triple-builder "/parent" :mr-green {})
      (build-node node-builder "/person" :ms-peacock triple-builder))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Mr Green" {})
      (build-triple triple-builder "/location" :library {})
      (build-triple triple-builder "/parent" :ms-white {})
      (build-node node-builder "/person" :mr-green triple-builder))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/name" "Ms White" {})
      (build-triple triple-builder "/location" :library {})
      (build-triple triple-builder "/parent" :ms-peacock {})
      (build-node node-builder "/person" :ms-white triple-builder))
    (let [triple-builder (create-triple-builder),
          property-builder (create-property-builder)]
        (build-property property-builder "/lat" 45)
        (build-property property-builder "/lng" 90)
        (build-triple triple-builder "/coords" "coords-A" property-builder)
        (build-node node-builder "/place" :library triple-builder))
    (testing "Validate custom whole-graph constraints"
      (is (= #{"Found parentage cycle with #{:mr-green :ms-peacock :ms-white}"}
             (do-validate node-builder))))))
