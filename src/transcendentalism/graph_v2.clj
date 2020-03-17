(ns transcendentalism.graph-v2
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(defrecord V [v]) ; TODO - remove this pointless wrapper.
(defrecord OPV [o p-vs])
(defrecord SPOPV [s p-opvs])
(defrecord G [s-popvs t-ss])

(defn is-type?
  [pred]
  (str/starts-with? pred "/type"))

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
    (get-props [triple] (into #{} (keys (:p-vs opv))))
    (get-properties [triple prop]
      (into #{} (map create-property ((:p-vs opv) prop #{}))))
    (to-opv [triple] opv)))

(defn create-node
  [spopv]
  (reify Node
    (get-sub [node] (:s spopv))
    (get-types [node]
      (into #{} (filter is-type?) (get-preds node)))
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
  (get-built-graph [builder node-builder-or-data]))
(defprotocol NodeBuilder
  (build-node [builder type sub triple-builder-or-data])
  (get-built-nodes [builder]))
(defprotocol TripleBuilder
  (build-triple [builder pred obj property-builder-or-data])
  (get-built-triples [builder]))
(defprotocol PropertyBuilder
  (build-property [builder prop val])
  (get-built-properties [builder]))

(defn create-property-builder
  []
  (let [my-properties (atom {})]
    (reify PropertyBuilder
      (build-property [builder prop val]
        (reset! my-properties
          (assoc @my-properties
            prop (conj (@my-properties prop #{}) (->V val)))))
      (get-built-properties [builder] @my-properties))))

(defn create-triple-builder
  []
  (let [my-triples (atom {})]
    (reify TripleBuilder
      (build-triple [builder pred obj property-builder-or-data]
        (let [properties (if (satisfies? PropertyBuilder property-builder-or-data)
                             (get-built-properties property-builder-or-data)
                             property-builder-or-data)]
          (reset! my-triples
            (assoc @my-triples
              pred (conj (@my-triples pred #{}) (->OPV obj properties))))))
      (get-built-triples [builder] @my-triples))))

(defn create-node-builder
  []
  (let [my-nodes (atom [])]
    (reify NodeBuilder
      (build-node [builder type sub triple-builder-or-data]
        (let [triples (if (satisfies? TripleBuilder triple-builder-or-data)
                          (get-built-triples triple-builder-or-data)
                          triple-builder-or-data)]
          (reset! my-nodes
            (conj @my-nodes
              (->SPOPV sub (merge-popvs {(str "/type" type) #{nil}}
                                        triples))))))
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
  (reify GraphBuilder
    (get-built-graph [builder node-builder-or-data]
      (let [spopvs (if (satisfies? NodeBuilder node-builder-or-data)
                       (get-built-nodes node-builder-or-data)
                       node-builder-or-data),
            s-popvs (reduce
                      (fn [result spopv]
                        (assoc result (:s spopv) (:p-opvs spopv)))
                      {} spopvs)
            t-ss (reduce
                   (fn [result spopv]
                     (let [types (get-types (create-node spopv))]
                       (apply assoc-all result (:s spopv) types)))
                   {} spopvs)]
        (->G s-popvs t-ss)))))
