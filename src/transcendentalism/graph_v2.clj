(ns transcendentalism.graph-v2
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(defrecord V [v])
(defrecord OPV [o p-vs])
(defrecord SPOPV [s p-opvs])
(defrecord G [s-popvs t-ss])

(defmacro reduce-all
  "bingings => [inner-binding ...]
   inner-binding => [name coll]
                 => [name1 name2 map]

   Creates a nested tower of reductions, using reduce if inner-binding has two
   values, or reduce-kv if it has three. The same accumulator is passed through
   the entire tower."
  [result initial-value bindings & body]
  (cond
    (= (count bindings) 0) `(do ~@body)
    (= (count (bindings 0)) 2) `(reduce
                                  (fn [~result ~((bindings 0) 0)]
                                    (reduce-all ~result ~result ~(subvec bindings 1) ~@body))
                                  ~initial-value ~((bindings 0) 1))
    (= (count (bindings 0)) 3) `(reduce-kv
                                  (fn [~result ~((bindings 0) 0) ~((bindings 0) 1)]
                                    (reduce-all ~result ~result ~(subvec bindings 1) ~@body))
                                  ~initial-value ~((bindings 0) 2))
    :else (throw (IllegalArgumentException.
                   "reduce-all only does reduce or reduce-kv"))))

(defn- merge-pvs
  [& pvs]
  (reduce-all result {}
              [[pv pvs]
               [p vs pv]]
    (if (empty? vs)
        result
        (assoc result p (set/union (result p #{}) vs)))))

(defn- merge-popvs
  [& popvs]
  (reduce-all result {}
              [[popv popvs]
               [p opvs popv]]
    (if (empty? opvs)
        result
        (assoc result p (set/union (result p #{}) opvs)))))

(defprotocol Property
  (get-val [property])
  (to-v [property]))
(defprotocol Triple
  (get-obj [triple])
  (get-props [triple])
  (get-properties [triple prop])
  (to-opv [triple]))
(defprotocol Node
  (get-sub [node])
  (get-types [node])
  (get-preds [node])
  (get-triples [node pred])
  (to-spopv [node]))
(defprotocol Graph
  (get-all-types [graph])
  (get-node [graph node])
  (get-nodes [graph type])
  (has-type? [graph node type]))

(defn create-property
  [v]
  (reify Property
    (get-val [property] (:v v))
    (to-v [property] v)))

(defn create-triple
  [opv]
  (reify Triple
    (get-obj [triple] (:o opv))
    (get-props [triple] (keys (:p-vs opv)))
    (get-properties [triple prop] (map create-property ((:p-vs opv) prop #{})))
    (to-opv [triple] opv)))

(defn create-node
  [spopv]
  (reify Node
    (get-sub [node] (:s spopv))
    (get-types [node]
      (into #{} (filter #(str/starts-with? % "/type") (get-preds node))))
    (get-preds [node] (into #{} (keys (:p-opvs spopv))))
    (get-triples [node pred]
      (into #{} (map create-triple ((:p-opvs spopv) pred #{}))))
    (to-spopv [node] spopv)))

(defn create-graph
  [g]
  (reify Graph
    (get-all-types [graph] (into #{} (keys (:t-ss g))))
    (get-node [graph sub]
      (let [popv ((:s-popvs g) sub nil)]
        (if (nil? popv)
            nil
            (create-node (->SPOPV sub popv)))))
    (get-nodes [graph type]
      (into #{} (map #(create-node (->SPOPV % ((:s-popvs g) %)))
                     ((:t-ss g) type #{}))))
    (has-type? [graph sub type] (contains? ((:t-ss g) type #{}) sub))))

(defprotocol GraphBuilder
  (get-node-builder [builder type])
  (get-built-graph [builder]))
(defprotocol NodeBuilder
  (get-triple-builder [builder pred])
  (build-node [builder sub])
  (get-built-nodes [builder]))
(defprotocol TripleBuilder
  (get-property-builder [builder prop])
  (build-triple [builder obj])
  (get-built-triples [builder]))
(defprotocol PropertyBuilder
  (build-property [builder val])
  (get-built-properties [builder]))

(defn- create-property-builder
  [prop]
  (let [my-properties (atom #{})]
    (reify PropertyBuilder
      (build-property [builder val]
        (reset! my-properties (conj @my-properties (-> V val))))
      (get-built-properties [builder] {prop @my-properties}))))

(defn- create-triple-builder
  [pred]
  (let [my-triples (atom #{}),
        my-property-builders (atom [])]
    (reify TripleBuilder
      (get-property-builder [builder prop]
        (let [property-builder (create-property-builder prop)]
          (reset! my-property-builders (conj @my-property-builders property-builder))
          property-builder))
      (build-triple [builder obj]
        (reset! my-triples
          (conj @my-triples
            (->OPV obj (apply merge-pvs
                              (map get-built-properties @my-property-builders))))))
      (get-built-triples [builder] {pred @my-triples}))))

(defn- create-node-builder
  [type]
  (let [full-type (str "/type" type),
        my-nodes (atom #{}),
        my-triple-builders (atom [])]
    (reify NodeBuilder
      (get-triple-builder [builder pred]
        (let [triple-builder (create-triple-builder pred)]
          (reset! my-triple-builders (conj @my-triple-builders triple-builder))
          triple-builder))
      (build-node [builder sub]
        (reset! my-nodes
          (conj @my-nodes
            (->SPOPV sub (apply merge-popvs
                                {full-type #{nil}}
                                (map get-built-triples @my-triple-builders))))))
      (get-built-nodes [builder] @my-nodes))))

(defn- assoc-all
  ([my-map my-val]
    my-map)
  ([my-map my-val & my-keys]
    (apply assoc-all
      (let [first-key (first my-keys)]
        (assoc my-map first-key (conj (my-map first-key #{}) my-val)))
      my-val (rest my-keys))))

(defn create-graph-builder
  []
  (let [my-node-builders (atom [])]
    (reify GraphBuilder
      (get-node-builder [builder type]
        (let [node-builder (create-node-builder type)]
          (reset! my-node-builders (conj @my-node-builders node-builder))
          node-builder))
      (get-built-graph [builder]
        (let [spopvs (apply set/union (map get-built-nodes @my-node-builders)),
              s-popvs (reduce
                        (fn [result spopv]
                          (assoc result (:s spopv) (:p-opvs spopv)))
                        {} spopvs)
              t-ss (reduce
                     (fn [result spopv]
                       (let [types (get-types (create-node spopv))]
                         (apply assoc-all result (:s spopv) types)))
                     {} spopvs)]
          (->G s-popvs t-ss))))))
