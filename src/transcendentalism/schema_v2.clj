(ns transcendentalism.schema-v2)

(use 'transcendentalism.constraint-v2
     'transcendentalism.graph ; For conversion
     'transcendentalism.graph-v2
     'transcendentalism.schema-data-v2)

(defn graph-to-v2
  [old-graph]
  ; (create-graph (get-built-graph (create-graph-builder) node-builder))
  (create-graph (get-built-graph (create-graph-builder) (create-node-builder)))
  )

(defn validate-graph-v2
  "Validates that a given graph conforms to a given schema."
  [graph-constraints graph]
  (validate graph-constraints nil graph))

(def schema-v2 (create-graph-constraints schema-data))
