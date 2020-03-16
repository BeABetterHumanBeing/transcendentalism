(ns transcendentalism.constraint-v2
  (:require [clojure.set :as set]))

(use 'transcendentalism.graph-v2
     'transcendentalism.time)

(defprotocol Constraint
  (validate [constraint graph data]))

(defn- and-constraint
  [graph data & constraints]
  (apply set/union (map #(validate % graph data) constraints)))

(defn- nil-constraint
  [name]
  (reify Constraint
    (validate [constraint graph data] #{})))

(defn- child-keyword
  [level]
  (case level
    :triple :properties
    :node :predicates
    :graph :types
    nil))

(defn- get-child-keys
  [level]
  (case level
    :triple get-props
    :node get-preds
    :graph get-all-types
    nil))

(defn- get-children
  [level]
  (case level
    :triple get-properties
    :node get-triples
    :graph get-nodes
    nil))

(defn- get-inner-data
  [level]
  (case level
    :property get-val
    :triple get-obj
    nil))

(defn- range-type-constraint
  [level range-type allow-nodes]
  (reify Constraint
    (validate [constraint graph wrapped-data]
      (let [data ((get-inner-data level) wrapped-data)]
        (if (or (nil? range-type)
              (and (= range-type :string)
                   (string? data))
              (and (= range-type :number)
                   (number? data))
              (and (= range-type :bool)
                   (instance? Boolean data))
              (and (= range-type :time)
                   (is-valid-time data))
              (and (string? range-type)
                   allow-nodes
                   (has-type? graph data range-type))
              (and (vector? range-type)
                   (not (nil? (some #(= data %) range-type)))))
          #{}
          #{(str data " does not match range type " range-type)})))))

(defn- required-constraint
  [tpp level]
  (reify Constraint
    (validate [constraint graph data]
      (if (contains? (into #{} ((get-child-keys level) data)) tpp)
          #{}
          #{(str tpp " is required")}))))

(defn- unique-constraint
  [tpp level]
  (reify Constraint
    (validate [constraint graph data]
      (let [child-count (count ((get-children level) data tpp))]
        (if (> child-count 1)
            #{(str tpp " is unique, but found " child-count)}
            #{})))))

(defn- exclusive-constraint
  [tpp level exclusions]
  (reify Constraint
    (validate [constraint graph data]
      (let [overlap (set/intersection exclusions
                                      (into #{} ((get-child-keys level) data)))]
      (if (empty? overlap)
          #{}
          #{(str tpp " excludes " exclusions ", but found " overlap)})))))

(defn- constraints-from-schema
  "f takes two args: the key and value from schema"
  [schema f]
  (reduce-kv
    (fn [result k v]
      (let [constraint (f k v)]
        (if (nil? constraint)
          result
          (conj result constraint))))
    (schema :constraints [])
    schema))

(defn- meta-constraints-from-schema
  "f takes three args: the child's key, and the key and value from child schema"
  [schema level f]
  (reduce-kv
    (fn [result prop child-schema]
      (reduce-kv
        (fn [result k v]
          (let [constraint (f prop k v)]
            (if (nil? constraint)
                result
                (conj result constraint))))
        (concat result (child-schema :meta-constraints []))
        child-schema))
    [] (schema (child-keyword level) {})))

(defn- child-constraints
  [schema level constraint-f]
  (reduce-kv
    (fn [result s child-schema]
      (assoc result
        s (constraint-f child-schema)))
    {} (schema (child-keyword level) {})))

(defn- check-child-constraints
  [graph data level constraints]
  (reduce
    (fn [result s]
      (let [constraint (constraints s (nil-constraint s))]
        (reduce
          (fn [result v]
            (set/union result (validate constraint graph v)))
          result ((get-children level) data s))))
    #{} ((get-child-keys level) data)))

(defn- create-property-constraints
  [prop-schema]
  (let [constraints (constraints-from-schema prop-schema
                      #(case %1
                        :range-type (range-type-constraint :property %2 false)
                        nil))]
    (reify Constraint
      (validate [constraint graph property]
        (apply and-constraint graph property constraints)))))

(defn- create-triple-constraints
  [pred-schema]
  (let [prop-constraints
          (child-constraints pred-schema :triple create-property-constraints),
        prop-meta-constraints
          (meta-constraints-from-schema pred-schema :triple
            #(case %2
              :required (if %3 (required-constraint %1 :triple) nil)
              :unique (if %3 (unique-constraint %1 :triple) nil)
              :exclusive (if (empty? %3) nil (exclusive-constraint %1 :triple %3))
              nil)),
        triple-constraints (constraints-from-schema pred-schema
                             #(case %1
                               :range-type (range-type-constraint :triple %2 true)
                               nil))]
    (reify Constraint
      (validate [constraint graph triple]
        (set/union
          (check-child-constraints graph triple :triple prop-constraints)
          (apply set/union (map #(validate % graph triple) prop-meta-constraints))
          (apply and-constraint graph triple triple-constraints))))))

(defn- create-node-constraints
  [type-schema]
  (let [pred-constraints
          (child-constraints type-schema :node create-triple-constraints),
        pred-meta-constraints
          (meta-constraints-from-schema type-schema :node
            #(case %2
              :required (if %3 (required-constraint %1 :node) nil)
              :unique (if %3 (unique-constraint %1 :node) nil)
              :exclusive (if (empty? %3) nil (exclusive-constraint %1 :node %3))
              nil)),
        node-constraints (constraints-from-schema type-schema (fn [_ _] nil))]
    (reify Constraint
      (validate [constraint graph node]
        (set/union
          (check-child-constraints graph node :node pred-constraints)
          (apply set/union (map #(validate % graph node) pred-meta-constraints))
          (apply and-constraint graph node node-constraints))))))

(defn create-graph-constraints
  [graph-schema]
  (let [type-constraints
          (child-constraints graph-schema :graph create-node-constraints),
        type-meta-constraints
          (meta-constraints-from-schema graph-schema :graph (fn [_ _ _] nil)),
        graph-constraints (constraints-from-schema graph-schema (fn [_ _] nil))]
    (reify Constraint
      (validate [constraint _ graph]
        (set/union
          (check-child-constraints graph graph :graph type-constraints)
          (apply set/union (map #(validate % graph graph) type-meta-constraints))
          (apply and-constraint graph graph graph-constraints))))))
