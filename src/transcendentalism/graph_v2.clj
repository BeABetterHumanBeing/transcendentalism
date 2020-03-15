(ns transcendentalism.graph-v2
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(defrecord V [v])
(defrecord PV [p-vs])
(defrecord OPV [o pv])
(defrecord POPV [p-opvs])
(defrecord SPOPV [s popv])
(defrecord G [s-popvs t-ss])

(defn- merge-pvs
  [& pvs]
  (reduce
    (fn [result pv]
      (reduce-kv
        (fn [result p vs]
          (assoc result p (set/union (result p #{}) vs)))
        result (:p-vs pv)))
    {} pvs))

(defn- merge-popvs
  [& popvs]
  (reduce
    (fn [result popv]
      (reduce-kv
        (fn [result p opvs]
          (assoc result p (set/union (result p #{}) opvs)))
        result (:p-opvs popv)))
    {} popvs))

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
    (get-props [triple] (keys (:pv opv)))
    (get-properties [triple prop] (map create-property ((:pv opv) prop #{})))
    (to-opv [triple] opv)))

(defn create-node
  [spopv]
  (reify Node
    (get-sub [node] (:s spopv))
    (get-types [node]
      (into #{} (filter #(str/starts-with? % "/type") (get-preds node))))
    (get-preds [node] (keys (:popv spopv)))
    (get-triples [node pred] (map create-triple ((:popv spopv) pred #{})))
    (to-spopv [node] spopv)))

(defn create-graph
  [g]
  (reify Graph
    (get-all-types [graph] (into #{} (keys (:t-ss g))))
    (get-node [graph sub]
      (let [popv ((:s-popvs g) sub nil)]
        (if (nil? popv)
            nil
            (create-node (->SPOPV sub (->POPV popv))))))
    (get-nodes [graph type]
      (into #{} (map #(create-node (->SPOPV % (->POPV ((:s-popvs g) %))))
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
      (get-built-properties [builder] (->PV {prop @my-properties})))))

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
            (->OPV obj (apply merge-pvs (map get-built-properties @my-property-builders))))))
      (get-built-triples [builder] (->POPV {pred @my-triples})))))

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
            (->SPOPV sub (apply merge-popvs (->POPV {full-type #{}})
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
                          (assoc result (:s spopv) (:popv spopv)))
                        {} spopvs)
              t-ss (reduce
                     (fn [result spopv]
                       (let [types (get-types (create-node spopv))]
                         (apply assoc-all result (:s spopv) types)))
                     {} spopvs)]
          (->G s-popvs t-ss))))))
