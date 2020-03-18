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

(defn- graph-from-node-builder
  [node-builder]
  (create-graph (get-built-graph (create-graph-builder) node-builder)))

(deftest empty-graph-test
  (let [graph (create-graph (get-built-graph (create-graph-builder)
                                             (create-node-builder)))]
    (testing "Test empty graph"
      (is (= #{} (get-all-types graph))))))

(deftest graph-nodes-test
  (let [node-builder (create-node-builder)]
    (build-node node-builder "/a" :a1 {})
    (build-node node-builder "/a" :a2 {})
    (build-node node-builder "/b" :b1 {})
    (let [graph (graph-from-node-builder node-builder),
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
  (let [node-builder (create-node-builder)]
    (build-node node-builder "/a" :a1 {})
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/foo" "foo1" {})
      (build-node node-builder "/a" :a2 triple-builder))
    (let [triple-builder (create-triple-builder)]
      (build-triple triple-builder "/foo" "foo1" {})
      (build-triple triple-builder "/foo" "foo2" {})
      (build-triple triple-builder "/bar" "bar1" {})
      (build-node node-builder "/a" :a3 triple-builder))
    (let [graph (graph-from-node-builder node-builder),
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
  (let [node-builder (create-node-builder)]
    (build-node node-builder "/a" :a1 {})
    (let [prop-builder (create-property-builder),
          pred-builder (create-triple-builder)]
      (build-property prop-builder "/do" 111)
      (build-triple pred-builder "/foo" "foo1" prop-builder)
      (build-node node-builder "/a" :a2 pred-builder))
    (let [prop-builder (create-property-builder),
          pred-builder (create-triple-builder)]
      (build-property prop-builder "/do" 111)
      (build-triple pred-builder "/foo" "foo1" prop-builder)
      (build-property prop-builder "/re" 222)
      (build-property prop-builder "/re" 333)
      (build-triple pred-builder "/foo" "foo2" prop-builder)
      (build-node node-builder "/a" :a3 pred-builder))
    (let [graph (graph-from-node-builder node-builder),
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

(deftest graph-builder-shorthand-test
  (testing "GraphBuilder shorthands"
    (let [triple-builder-1 (create-triple-builder),
          property-builder (create-property-builder),
          triple-builder-2 (create-triple-builder)]
      (build-property property-builder "/my_prop" 111)
      (build-triple triple-builder-1 "/my_pred" "My Obj" property-builder)
      (build-triple triple-builder-2 "/my_pred" "My Obj" {"/my_prop" 111})
      (is (= (get-built-triples triple-builder-1)
             (get-built-triples triple-builder-2))))
    (let [triple-builder-1 (create-triple-builder),
          property-builder (create-property-builder),
          triple-builder-2 (create-triple-builder)]
      (build-property property-builder "/my_prop" 111)
      (build-triple triple-builder-1 "/my_pred" "My Obj" property-builder)
      (build-triple triple-builder-2 "/my_pred" "My Obj" {"/my_prop" #{111}})
      (is (= (get-built-triples triple-builder-1)
             (get-built-triples triple-builder-2))))
    (let [triple-builder-1 (create-triple-builder),
          property-builder (create-property-builder),
          triple-builder-2 (create-triple-builder)]
      (build-property property-builder "/my_prop" 111)
      (build-property property-builder "/my_prop" 222)
      (build-property property-builder "/my_prop" 333)
      (build-triple triple-builder-1 "/my_pred" "My Obj" property-builder)
      (build-triple triple-builder-2 "/my_pred" "My Obj" {"/my_prop" #{111 222 333}})
      (is (= (get-built-triples triple-builder-1)
             (get-built-triples triple-builder-2))))
    (let [triple-builder-1 (create-triple-builder),
          property-builder (create-property-builder),
          triple-builder-2 (create-triple-builder)]
      (build-property property-builder "/my_prop" #{111 222 333})
      (build-triple triple-builder-1 "/my_pred" "My Obj" property-builder)
      (build-triple triple-builder-2 "/my_pred" "My Obj" {"/my_prop" #{#{111 222 333}}})
      (is (= (get-built-triples triple-builder-1)
             (get-built-triples triple-builder-2))))
    (let [node-builder-1 (create-node-builder),
          triple-builder (create-triple-builder),
          node-builder-2 (create-node-builder)]
      (build-triple triple-builder "/my_pred" 111 {})
      (build-node node-builder-1 "/type/a" :n1 triple-builder)
      (build-node node-builder-2 "/type/a" :n1 {"/my_pred" 111})
      (is (= (get-built-nodes node-builder-1)
             (get-built-nodes node-builder-2))))
    (let [node-builder-1 (create-node-builder),
          triple-builder (create-triple-builder),
          node-builder-2 (create-node-builder),
          property-builder (create-property-builder)]
      (build-property property-builder "/my_prop" "My Val")
      (build-triple triple-builder "/my_pred" 111 property-builder)
      (build-node node-builder-1 "/type/a" :n1 triple-builder)
      (build-node node-builder-2 "/type/a" :n1 {"/my_pred" [111 property-builder]})
      (is (= (get-built-nodes node-builder-1)
             (get-built-nodes node-builder-2))))
    (let [node-builder-1 (create-node-builder),
          triple-builder (create-triple-builder),
          node-builder-2 (create-node-builder)]
      (build-triple triple-builder "/my_pred" [111 222 333] {})
      (build-node node-builder-1 "/type/a" :n1 triple-builder)
      (build-node node-builder-2 "/type/a" :n1 {"/my_pred" [[111 222 333]]})
      (is (= (get-built-nodes node-builder-1)
             (get-built-nodes node-builder-2))))
    (let [node-builder-1 (create-node-builder),
          triple-builder (create-triple-builder),
          node-builder-2 (create-node-builder),
          property-builder (create-property-builder)]
      (build-triple triple-builder "/my_pred" 111 {})
      (build-property property-builder "/my_prop" "My Val")
      (build-triple triple-builder "/my_pred" 222 property-builder)
      (build-node node-builder-1 "/type/a" :n1 triple-builder)
      (build-node node-builder-2 "/type/a" :n1 {"/my_pred" #{111 [222 property-builder]}})
      (is (= (get-built-nodes node-builder-1)
             (get-built-nodes node-builder-2))))))

(deftest graph-query-test
  (let [node-builder (create-node-builder)]
    (build-node node-builder "/type/person" :bob
      {"/name" "Bob",
       "/sex" :male,
       "/lives_at" :home,
       "/works_at" [:smithy {"/occupation" :smith}],
       "/relative" #{[:alice {"/relationship" :spouse}]
                     [:chad {"/relationship" :enemy}]}})
    (build-node node-builder "/type/person" :alice
      {"/name" "Alice",
       "/sex" :female,
       "/lives_at" :home,
       "/relative" #{[:bob {"/relationship" :spouse}]
                     [:wilma {"/relationship" :parent}]}})
    (build-node node-builder "/type/person" :wilma
      {"/name" "Wilma",
       "/sex" :female,
       "/lives_at" :home,
       "/works_at" [:church {"/occupation" :priest}],
       "/relative" #{[:alice {"/relationship" :child}]
                     [:greg {"/relationship" :child}]}})
    (build-node node-builder "/type/person" :chad
      {"/name" "Chad",
       "/sex" :male,
       "/lives_at" :forest,
       "/works_at" [:smithy {"/occupation" :smith}],
       "/relative" #{[:wilma {"/relationship" :enemy}]
                     [:greg {"/relationship" :enemy}]}})
    (build-node node-builder "/type/person" :greg
      {"/name" "Greg",
       "/sex" :male,
       "/works_at" [:storehouse {"/occupation" :merchant}]
       "/relative" [:wilma {"/relationship" :parent}]})
    (build-node node-builder "/type/place" :church
      {"/name" "Church",
       "/climate" ["indoors" {"/is_dry" true,
                              "/is_warm" false}],
       "/nearby" :home})
    (build-node node-builder "/type/place" :home
      {"/name" "Home",
       "/climate" ["indoors" {"/is_dry" true,
                              "/is_warm" true}],
       "/nearby" #{:church :smithy}})
    (build-node node-builder "/type/place" :smithy
      {"/name" "Smithy",
       "/climate" ["indoors" {"/is_dry" true,
                              "/is_warm" true}],
       "/nearby" #{:home :forest}})
    (build-node node-builder "/type/place" :forest
      {"/name" "Forest",
       "/climate" ["indoors" {"/is_dry" false,
                              "/is_warm" false}],
       "/nearby" #{:smithy :storehouse}})
    (build-node node-builder "/type/place" :storehouse
      {"/name" "Storehouse",
       "/climate" ["indoors" {"/is_dry" true,
                              "/is_warm" false}],
       "/nearby" :forest})
    (let [graph (graph-from-node-builder node-builder),
          querier (create-graph-querier graph)]
      (testing "Graph queries"
        (let [query (with-meta (q-chain (q-pred "/climate") (q-prop "/is_dry"))
                               {:get-val true,
                                :unique true})]
          ; Is the forest dry?
          (is (= false (gq querier query :forest)))
          ; Is the smithy dry?
          (is (= true (gq querier query :smithy)))
          ; Is Alice dry?
          (is (empty? (gq querier query :alice))))
        (let [query (with-meta (q-chain (q-pred "/works_at") (q-prop "/occupation"))
                               {:get-val true,
                                :unique true})]
          ; What is Greg's occupation?
          (is (= :merchant (gq querier query :greg)))
          ; What is Chad's occupation?
          (is (= :smith (gq querier query :chad)))
          ; What is the Church's occupation?
          (is (empty? (gq querier query :church))))
        (let [query-relationship (with-meta (q-prop "/relationship")
                                            {:get-val true, :unique true})
              query (with-meta (q-chain (q-pred "/relative"
                                                (prune-unless
                                                  #(= :spouse
                                                      (gq querier
                                                          query-relationship
                                                          %)))) (q-node)
                                        (q-pred "/name"))
                               {:get-obj true})]
          ; What is Bob's spouse's name?
          (is (= #{"Alice"} (gq querier query :bob)))
          ; What is Alice's spouse's name?
          (is (= #{"Bob"} (gq querier query :alice)))
          ; What is Greg's spouse's name?
          (is (= #{} (gq querier query :greg))))
        (let [query-enemy (q-pred "/relative"
                                  (prune-unless
                                    #(= :enemy
                                        (gq querier
                                            (with-meta (q-prop "/relationship")
                                                       {:get-val true,
                                                        :unique true})
                                            %)))),
              query (with-meta (q-chain query-enemy (q-node)
                                        query-enemy (q-node)
                                        (q-pred "/works_at") (q-node)
                                        (q-pred "/name"))
                               {:get-obj true})]
          ; What are Bob's enemy's enemy's workplaces?
          (is (= #{"Church" "Storehouse"} (gq querier query :bob))))
        (let [query (with-meta (q-chain (q-pred "/nearby") (q-node)
                                        (q-star (q-chain (q-pred "/nearby") (q-node)))
                                        (q-pred "/name"))
                               {:get-obj true})]
          ; What places are transitively near the Church?
          (is (= #{"Church" "Home" "Smithy" "Forest" "Storehouse"}
                 (gq querier query :church))))
        (let [warm-query (with-meta (q-chain (q-pred "/climate")
                                             (q-prop "/is_warm"))
                                    {:get-val true,
                                     :unique true})
              query (with-meta (q-chain (q-pred "/works_at") (q-node)
                                        (q-pred "/nearby")
                                        (q-node
                                          (prune-unless
                                            #(= false
                                                (gq querier warm-query %))))
                                        (q-pred "/name"))
                               {:get-obj true})]
          ; Cold places near where Greg works?
          (is (= #{"Forest"} (gq querier query :greg)))
          ; Cold places near where Wilma works?
          (is (= #{} (gq querier query :wilma))))
        (let [query-relationship (with-meta (q-prop "/relationship")
                                            {:get-val true, :unique true}),
              query (with-meta (q-chain
                                 (q-pred "/relative"
                                         (prune-unless
                                           #(= :parent
                                               (gq querier query-relationship %)))) (q-node)
                                 (q-pred "/relative"
                                         (prune-unless
                                           #(= :child
                                               (gq querier query-relationship %)))) (q-node)
                                 (q-pred "/name"))
                               {:get-obj true})]
          ; Children of Alice's parents?
          (is (= #{"Alice" "Greg"} (gq querier query :alice))))
        (let [query-relationship (with-meta (q-prop "/relationship")
                                            {:get-val true, :unique true}),
              query (with-meta (q-chain
                                 (q-pred "/relative"
                                         (prune-unless
                                           #(= :parent
                                               (gq querier query-relationship %))))
                                 (q-node (prune-unless
                                           #(= :male
                                               (gq querier
                                                   (with-meta (q-pred "/sex")
                                                              {:get-obj true,
                                                               :unique true})
                                                   %))))
                                 (q-pred "/relative"
                                         (prune-unless
                                           #(= :child
                                               (gq querier query-relationship %)))) (q-node)
                                 (q-pred "/name"))
                               {:get-obj true})]
          ; Children of Alice's father?
          (is (= #{} (gq querier query :alice))))
        (let [warm-query (with-meta (q-chain (q-pred "/climate")
                                             (q-prop "/is_warm"))
                                    {:get-val true,
                                     :unique true}),
              query (with-meta (q-chain (q-or (q-pred "/lives_at")
                                              (q-pred "/works_at")) (q-node)
                                        (q-pred "/nearby")
                                        (q-node
                                          (prune-unless
                                            #(= true
                                                (gq querier warm-query %))))
                                        (q-pred "/name"))
                               {:get-obj true})]
          ; Warm places near where Wilma lives or works?
          (is (= #{"Home" "Smithy"} (gq querier query :wilma)))
          ; Warm places near where Greg lives or works?
          (is (= #{} (gq querier query :greg))))))))
