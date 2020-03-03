(ns transcendentalism.directive
  (:require
    [clojure.set :as set]
    [clojure.string :as str]))

(use 'transcendentalism.graph
     'transcendentalism.schema)

(defn apply-directives
  "Processes collections of triples, applying any directives found therein"
  [& colls]
  (let [groups (group-by #(instance? transcendentalism.graph.Triple %) (flatten colls)),
        triples (groups true),
        directives (groups false)]
    (reduce
      (fn [result directive] (directive result))
      triples directives)))

(defn directive-under-construction
  "Returns a directive that labels the given subs as under construction"
  [& sub]
  (let [subs (into #{} sub)]
    (fn [triples]
      (concat
        (filter #(or (not (contains? subs (:sub %)))
                     (not (str/starts-with? (:pred %) "/essay/flow"))
                     (= (:pred %) "/essay/flow/home")) triples)
        (into [] (map #(->Triple % "/essay/label" :under-construction {}) subs))
        (into [] (map #(->Triple % "/essay/flow/see_also" :connections {}) subs))))))

(defn directive-see-also-inline-to-flow
  "For every /item/inline/see_also, adds an equivalent /essay/flow/see_also"
  [triples]
  (let [graph (construct-graph triples),
        essay-subs (map :sub (filter #(= (:pred %) "/type/essay") triples))]
    (reduce
      (fn [result essay-sub]
        (let [see_alsos (keys
              (gq
                graph
                (q-chain
                  (q-pred "/essay/contains")
                  gq-segment-to-item
                  gq-item-to-item
                  ; Because the see_also link may be buried in a tangent...
                  (q-kleene
                    (q-chain
                      (q-pred "/item/inline/tangent")
                      gq-segment-to-item
                      gq-item-to-item))
                  (q-pred "/item/inline/see_also"))
               essay-sub))]
          (concat result
            (into []
              (map #(->Triple essay-sub "/essay/flow/see_also" % {})
                   see_alsos)))))
      triples essay-subs)))

(defn directive-dedup-cxns
  "De-dupes /essay/flow triples that have the same sub and obj"
  [triples]
  (let [cxns (filter #(str/starts-with? (:pred %) "/essay/flow") triples),
        sub-to-cxns (index-by-sub cxns),
        dupes
        (into #{}
          (reduce-kv
            (fn [result sub cxns]
              (let [obj-to-cxns (index-by-obj cxns)]
                (set/union
                  result
                  (reduce-kv
                    (fn [result obj cxns]
                      (if (> (count cxns) 1)
                        (let [cxn-preds (set (map :pred cxns)),
                              highest-priority-pred
                              (if (contains? cxn-preds "/essay/flow/home")
                                "/essay/flow/home"
                                (if (contains? cxn-preds "/essay/flow/menu")
                                  "/essay/flow/menu"
                                  "/essay/flow/next"))]
                          (set/union
                            result
                            (filter #(not (= (:pred %) highest-priority-pred))
                                    cxns)))
                        result))
                    #{} obj-to-cxns))))
            #{} sub-to-cxns))]
    (filter #(not (contains? dupes %)) triples)))