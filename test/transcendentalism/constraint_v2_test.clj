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
        },
        "/birthday" {
          :range-type :time,
          :unique true,
          :required true,
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
                      fave-ordinals (map #(get-properties % "/order") fave-things)]
                  (if (distinct? fave-ordinals)
                      #{}
                      #{(str fave-ordinals " are not distinct")}))))
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
                      (< (hours-ago (to-time (get-obj birthday))) 175320))
                  #{}
                  (let [fave-things (get-triples node "/favorite_thing"),
                        texture-triples (map #(get-triples % "/texture") fave-things),
                        hard-textures (filter #(= (get-obj %) :hard) texture-triples)]
                    (if (empty? hard-textures)
                        #{}
                        #{"People over 20 can't like hard things"}))))))
      ],
      :meta-constraints [
        (reify Constraint
          (validate [constraint _ graph]
            ; The graph composed of 'parents' must be acyclic.
            (let [people (get-nodes graph "/type/person"),
                  person-parent (reduce
                                  (fn [result person]
                                    (assoc result
                                      (get-sub person)
                                      (map :get-sub (get-triples person "/parent"))))
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
                      (if (or (empty? kind-of-softness)
                              (= (get-obj triple) :hard))
                          #{}
                          #{"/kind_of_softness requires that /texture be :soft"}))))
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
                      (if (or (empty? kind-of-hardness)
                              (= (get-obj triple) :soft))
                          #{}
                          #{"/kind_of_hardness requires that /texture be :hard"}))))
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
  [graph-builder]
  (validate graph-constraints nil
            (create-graph (get-built-graph graph-builder))))

(deftest simple-graph-test
  (let [graph-builder (create-graph-builder)]
    (testing "Validate empty graph"
      (is (= #{} (do-validate graph-builder))))
    (let [node-builder (get-node-builder graph-builder "/not_a_type"),
          triple-builder (get-triple-builder node-builder "/not_a_pred")]
      (build-triple triple-builder "blah")
      (build-node node-builder :a1))
    (testing "Validate non-existent type"
      (is (= #{"/type/not_a_type is not an allowed type"}
             (do-validate graph-builder))))))

(deftest required-triple-test
  (let [graph-builder (create-graph-builder),
        person-builder (get-node-builder graph-builder "/person"),
        place-builder (get-node-builder graph-builder "/place"),
        thing-builder (get-node-builder graph-builder "/thing")]
    (build-node person-builder :ms-peacock)
    (build-node place-builder :library)
    (build-node thing-builder :candlestick)
    (testing "Validate required triples"
      (is (= #{"/name is required" "/texture is required" "/coords is required"
               "/birthday is required" "/location is required"}
             (do-validate graph-builder))))))

(deftest unique-triple-required-property-test
  (let [graph-builder (create-graph-builder),
        person-builder (get-node-builder graph-builder "/person"),
        name-builder (get-triple-builder person-builder "/name"),
        parent-builder (get-triple-builder person-builder "/parent"),
        birthday-builder (get-triple-builder person-builder "/birthday"),
        location-builder (get-triple-builder person-builder "/location"),
        favorite-thing-builder (get-triple-builder person-builder "/favorite_thing"),
        place-builder (get-node-builder graph-builder "/place"),
        coords-builder (get-triple-builder place-builder "/coords"),
        thing-builder (get-node-builder graph-builder "/thing"),
        texture-builder (get-triple-builder thing-builder "/texture")]
    (build-triple name-builder "Ms Peacock")
    (build-triple name-builder "Jacqueline")
    (build-triple parent-builder :ms-peacock)
    (build-triple parent-builder :ms-peacock)
    (build-triple birthday-builder (get-hours-ago 20))
    (build-triple birthday-builder (get-hours-ago 200))
    (build-triple location-builder :library)
    (build-triple location-builder :library)
    (build-triple favorite-thing-builder :candlestick)
    (build-triple favorite-thing-builder :candlestick)
    (build-node person-builder :ms-peacock)
    (build-triple coords-builder "coords-A")
    (build-triple coords-builder "coords-B")
    (build-node place-builder :library)
    (build-triple texture-builder :hard)
    (build-triple texture-builder :soft)
    (build-node thing-builder :candlestick)
    (testing "Validate required triples"
      (is (= #{"/birthday is unique, but found 2"
               "/texture is unique, but found 2"
               "/coords is unique, but found 2"
               "/lng is required" "/order is required" "/lat is required"}
             (do-validate graph-builder))))))
