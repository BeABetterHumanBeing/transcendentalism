(ns transcendentalism.graph
  (:require
    [clojure.set :as set]
    [clojure.string :as str]))

(use 'transcendentalism.time)

; The graph is composed of triples. Each triple relates a subject to an object
; by means of a predicate.
(defrecord Triple [sub pred obj p-vs])

(defn property
  [triple pred default]
  ((:p-vs triple) pred default))

(defn index-by-sub
  "Indexes a collection of triples by their subjects"
  [triples]
  (reduce
    (fn [result triple]
      (assoc result (:sub triple) (conj (result (:sub triple)) triple)))
    {} triples))

(defn index-by-obj
  "Indexes a collection of triples by their objects"
  [triples]
  (reduce
    (fn [result triple]
      (assoc result (:obj triple) (conj (result (:obj triple)) triple)))
    {} triples))

; The Graph protocol is the interface through which a graph is accessed.
(defprotocol GraphV1
  (all-triples [graph] [graph sub] [graph sub pred]
    "Returns a collection of all the triples in the graph [for a given [sub and pred]]")
  (all-nodes [graph] [graph type]
    "Returns a collection of all nodes in the graph [with a given type]")
  (transitive-closure [graph sub pred]
    "Returns the sub, and all other subs that can be transitively reached by
     following pred")
  (has-type-v1? [graph sub type] "Returns whether the given sub has the given type.")
  (get-unique [graph sub pred]
    "Returns the obj of the unique triple on sub with pred, or nil if none exists")
  (get-time [graph sub]
    "Returns the time associated with the given sub, or nil if it has none")
  ; (get-relation [graph pred] "Returns the subgraph defined by the given pred")
  (get-node-v1 [graph sub] "Returns the subgraph defined by the given sub"))

; A Relation is the subgraph composed of only a single predicate.
; (defprotocol Relation
;   (participant-nodes [relation] "Returns all nodes in the relation")
;   (all-objs [relation sub] "Returns all the objects of a given sub's relationship")
;   (get-sources [relation] "Returns all subs that are not objects in the relation")
;   (get-sinks [relation] "Returns all subs that are not subjects in the relation"))

; A Node is the subgraph that shares a common sub.
(defprotocol NodeV1
  (get-types-v1 [node] "Returns all types asserted on the node")
  (get-ordered-objs [node pred]
    "Returns all objects with the given pred, in order")
  (unique-or-nil [node pred]
    "Returns the unique object of the given pred, or nil")
  (get-triples-v1 [node] "Returns all triples asserted on the node"))

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
    (reify NodeV1
      (get-types-v1 [node]
        (map :pred (filter #(str/starts-with? (:pred %) "/type") triples)))
      (get-ordered-objs [node pred]
        (map :obj
          (sort #(< (property %1 "/order" 0)
                    (property %2 "/order" 0))
                (pred-to-triples pred []))))
      (unique-or-nil [node pred]
        (let [selected (pred-to-triples pred [])]
          (if (empty? selected)
            nil
            (:obj (first selected)))))
      (get-triples-v1 [node] triples))))

; (defn- construct-relation
;   [relation-map]
;   (reify Relation
;     (participant-nodes [relation]
;       (keys relation-map))
;     (all-objs [relation sub]
;       (relation-map sub))
;     (get-sources [relation]
;       (let [subs (participant-nodes relation)]
;         (reduce
;           (fn [result sub]
;             (apply disj result (all-objs relation sub)))
;           (into #{} subs) subs)))
;     (get-sinks [relation]
;       (filter
;         (fn [sub]
;           (let [objs (all-objs relation sub)]
;             (or (empty? objs)
;                 (= objs [sub]))))
;         (participant-nodes relation)))))

(defn construct-graph
  "Constructs a graph from a set of triples"
  [triples]
  (let [graph-data (index-by-sub triples)]
    (reify GraphV1
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
        (filter #(has-type-v1? graph % type) (all-nodes graph)))
      (transitive-closure [graph sub pred]
        (loop [results [],
               subs-to-process [sub]]
          (if (empty? subs-to-process)
            results
            (recur (conj results (first subs-to-process))
                   (concat (rest subs-to-process)
                           (map :obj (all-triples graph
                                                  (first subs-to-process)
                                                  pred)))))))
      (has-type-v1? [graph sub type]
        (if (contains? graph-data sub)
          (not (nil? (some #(= (:pred %) type) (sub graph-data))))
          false))
      (get-unique [graph sub pred]
        (let [triples (all-triples graph sub pred)]
          (if (empty? triples) nil (:obj (first triples)))))
      (get-time [graph sub]
        (let [value (get-unique graph sub "/event/time")]
          (if (nil? value) nil (to-time value))))
      ; (get-relation [graph pred]
      ;   (construct-relation
      ;     (reduce
      ;       (fn [result triple]
      ;         (let [sub (:sub triple),
      ;               obj (:obj triple)]
      ;           (assoc result
      ;             sub (if (contains? result sub)
      ;                   (conj (result sub) obj)
      ;                   [obj])
      ;             obj (if (contains? result obj)
      ;                   (result obj)
      ;                   []))))
      ;       {} (all-triples graph pred))))
      (get-node-v1 [graph sub]
        (construct-node (sub graph-data))))))

; Graph Queries provide regex support for graph traversals. Call your query
; using gq and using the meta-* versions of the queries if you want your call
; to be metadata-aware.

(defn q-pred-v1
  "Query that expands the path along a given pred. Moves tablet data from input
   sub to output sub, optionally calling a provided fn to update the data."
  ([pred] (q-pred-v1 (fn [sub data] data) pred))
  ([f pred]
   (fn [graph tablet]
     (if (empty? tablet)
       {} ; Early return optimization
       (apply merge
         (map
           (fn [sub]
             (let [prev-data (sub tablet),
                   triples (all-triples graph sub pred)]
               (reduce
                 (fn [result triple]
                   (let [obj (:obj triple),
                         new-data (f triple prev-data)]
                     (assoc result obj new-data)))
                 {}
                 triples)))
           (keys tablet)))))))

(defn q-chain-v1
  "Query that chains together some number of other queries in sequence"
  [& queries]
  (fn [graph tablet]
    (if (empty? tablet)
      {} ; Early return optimization
      (reduce
        (fn [result query]
          (query graph result))
        tablet queries))))

(defn q-or-v1
  "Query that ORs together other queries, so that any path can be taken"
  [& queries]
  (fn [graph tablet]
    (if (empty? tablet)
      {} ; Early return optimization
      (apply merge (map #(% graph tablet) queries)))))

(defn q-kleene
  "Query that applies the kleene-star to another query"
  ([query] (q-kleene (fn [sub data i] data) query))
  ([f query]
    (fn [graph tablet]
      (let [process (fn [data i]
                      (reduce-kv
                        (fn [result k v]
                          (assoc result k (f k v i)))
                       {} data))]
        (loop [result (process tablet 0),
               unprocessed result,
               iteration 1]
          (if (empty? unprocessed)
            result
            (let [next-tablet (process (query graph unprocessed) iteration)]
              (recur (merge result next-tablet)
                     (reduce-kv
                       (fn [result k v]
                         (if (contains? result k)
                           result
                           (assoc result k v)))
                       {} next-tablet)
                     (inc iteration)))))))))

(defn gq-v1
  "Executes a metadata-sensitive graph query"
  [graph query sub]
  (query graph {sub {}}))
