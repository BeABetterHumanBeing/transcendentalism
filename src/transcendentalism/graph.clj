(ns transcendentalism.graph)

; The graph is composed of triples. Each triple relates a subject to an object
; by means of a predicate.
(defrecord Triple [sub pred obj])

(defn print-triple
  "Pretty-prints a triple"
  [triple]
  (str (:sub triple) "--" (:pred triple) "->" (:obj triple)))

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
  (all-triples [graph] [graph sub]
    "Returns a collection of all the triples in the graph [for a given sub]")
  (all-nodes [graph] [graph type]
    "Returns a collection of all nodes in the graph [with a given type]")
  (has-type? [graph sub type] "Returns whether the given sub has the given type."))

(defn construct-graph
  "Constructs a graph from a set of triples"
  [triples]
  (let [graph-data (graph-data-from-triples triples)]
    (reify Graph
      (all-triples [graph]
        (flatten (map second (seq graph-data))))
      (all-triples [graph sub]
        (sub graph-data))
      (all-nodes [graph]
        (keys graph-data))
      (all-nodes [graph type]
        (filter #(has-type? graph % "/type/essay_segment") (all-nodes graph)))
      (has-type? [graph sub type]
        (if (contains? graph-data sub)
          (not (nil? (some #(= (:pred %) type) (sub graph-data))))
          false)))))