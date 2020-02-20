(ns transcendentalism.graph
  (:require [clojure.string :as str]))

(use 'transcendentalism.time)

; The graph is composed of triples. Each triple relates a subject to an object
; by means of a predicate.
(defrecord Triple [sub pred obj])

(defn print-triple
  "Pretty-prints a triple"
  [triple]
  (str (:sub triple) "--" (:pred triple) "->" (:obj triple)))

(defn get-property
  "Returns the property associated with an obj, or a default value"
  [property obj default]
  (let [metadata (meta obj)]
    (if (nil? metadata)
      default
      (metadata property default))))

(defn- graph-data-from-triples
  "Constructs a graph by associating triples by subject"
  [triples]
  (reduce
    (fn [graph triple]
      (assoc graph (:sub triple) (conj (graph (:sub triple)) triple)))
    {}
    triples))

; The Graph protocol is the interface through which a graph is accessed.
(defprotocol Graph
  (all-triples [graph] [graph sub] [graph sub pred]
    "Returns a collection of all the triples in the graph [for a given [sub and pred]]")
  (all-nodes [graph] [graph type]
    "Returns a collection of all nodes in the graph [with a given type]")
  (has-type? [graph sub type] "Returns whether the given sub has the given type.")
  (get-unique [graph sub pred]
    "Returns the obj of the unique triple on sub with pred, or nil if none exists")
  (get-time [graph sub]
    "Returns the time associated with the given sub, or nil if it has none")
  (get-relation [graph pred] "Returns the subgraph defined by the given pred")
  (get-node [graph sub] "Returns the subgraph defined by the given sub"))

; A Relation is the subgraph composed of only a single predicate.
(defprotocol Relation
  (participant-nodes [relation] "Returns all nodes in the relation")
  (all-objs [relation sub] "Returns all the objects of a given sub's relationship")
  (get-sources [relation] "Returns all subs that are not objects in the relation")
  (get-sinks [relation] "Returns all subs that are not subjects in the relation"))

; A Node is the subgraph that shares a common sub.
(defprotocol Node
  (get-types [node] "Returns all types asserted on the node")
  (get-ordered-objs [node pred]
    "Returns all objects with the given pred, in order")
  (unique-or-nil [node pred]
    "Returns the unique object of the given pred, or nil"))

(defn- construct-node
  [triples]
  (let [pred-to-triples
        (reduce
          (fn [result triple]
            (let [pred (:pred triple)]
              (if (contains? result pred)
                (assoc result pred (conj (result pred) triple))
                (assoc result pred [triple]))))
          {} triples)]
    (reify Node
      (get-types [node]
        (map :pred (filter #(str/starts-with? (:pred %) "/type") triples)))
      (get-ordered-objs [node pred]
        (map #(first (:obj %))
          (sort #(< (get-property :order (:obj %1) 0)
                    (get-property :order (:obj %2) 0))
                (pred-to-triples pred []))))
      (unique-or-nil [node pred]
        (let [selected (pred-to-triples pred [])]
          (if (empty? selected)
            nil
            (:obj (first selected))))))))

(defn- construct-relation
  [relation-map]
  (reify Relation
    (participant-nodes [relation]
      (keys relation-map))
    (all-objs [relation sub]
      (relation-map sub))
    (get-sources [relation]
      (let [subs (participant-nodes relation)]
        (reduce
          (fn [result sub]
            (apply disj result (all-objs relation sub)))
          (into #{} subs) subs)))
    (get-sinks [relation]
      (filter
        (fn [sub]
          (let [objs (all-objs relation sub)]
            (or (empty? objs)
                (= objs [sub]))))
        (participant-nodes relation)))))

(defn construct-graph
  "Constructs a graph from a set of triples"
  [triples]
  (let [graph-data (graph-data-from-triples triples)]
    (reify Graph
      (all-triples [graph]
        (flatten (map second (seq graph-data))))
      (all-triples [graph sub-or-pred]
        (if (string? sub-or-pred)
          (filter #(= (:pred %) sub-or-pred) (all-triples graph))
          (sub-or-pred graph-data)))
      (all-triples [graph sub pred]
        (filter #(= (:pred %) pred) (all-triples graph sub)))
      (all-nodes [graph]
        (keys graph-data))
      (all-nodes [graph type]
        (filter #(has-type? graph % type) (all-nodes graph)))
      (has-type? [graph sub type]
        (if (contains? graph-data sub)
          (not (nil? (some #(= (:pred %) type) (sub graph-data))))
          false))
      (get-unique [graph sub pred]
        (let [triples (all-triples graph sub pred)]
          (if (empty? triples) nil (:obj (first triples)))))
      (get-time [graph sub]
        (let [value (get-unique graph sub "/event/time")]
          (if (nil? value) nil (to-time value))))
      (get-relation [graph pred]
        (construct-relation
          (reduce
            (fn [result triple]
              (let [sub (:sub triple),
                    obj (:obj triple)]
                (assoc result
                  sub (if (contains? result sub)
                        (conj (result sub) obj)
                        [obj])
                  obj (if (contains? result obj)
                        (result obj)
                        []))))
            {} (all-triples graph pred))))
      (get-node [graph sub]
        (construct-node (sub graph-data))))))
