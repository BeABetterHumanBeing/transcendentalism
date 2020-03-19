(ns transcendentalism.schema-v2)

(use 'transcendentalism.constraint-v2
     'transcendentalism.graph ; For conversion
     'transcendentalism.graph-v2
     'transcendentalism.schema-data-v2)

(defn- triples-by-pred
  [triples]
  (reduce
    (fn [result triple]
      (assoc result (:pred triple) (conj (result (:pred triple)) triple)))
    {} triples))

(defn- triples-to-opv
  [triples]
  (reduce
    (fn [result triple]
      (conj result [(:obj triple) (:p-vs triple)]))
    #{} triples))

(defn graph-to-v2
  [old-graph]
  (let [triples (all-triples old-graph),
        triples-by-sub (index-by-sub triples),
        node-builder (create-node-builder)]
    (doall
      (map
        (fn [[sub triples]]
          (let [a-type (subs (:pred (first (filter #(is-type? (:pred %)) triples))) 5),
                popv (reduce-kv
                       (fn [result pred triples]
                         (assoc result pred (triples-to-opv triples)))
                       {} (triples-by-pred triples))]
            (build-node node-builder a-type sub popv)))
        triples-by-sub))
    (create-graph (get-built-graph (create-graph-builder) node-builder))))

(defn validate-graph-v2
  "Validates that a given graph conforms to a given schema."
  [graph-constraints graph]
  (let [errors (validate graph-constraints graph graph)]
    (doall (map println errors))
    (empty? errors)))

(def schema (create-graph-accessor schema-data))
