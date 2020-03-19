(ns transcendentalism.constraint-v2
  (:require [clojure.set :as set]))

(use 'transcendentalism.graph-v2
     'transcendentalism.time)

(defprotocol Constraint
  (validate [constraint graph data] "The set of validation error strings"))

(defn- and-constraint
  [graph data & constraints]
  (apply set/union (map #(validate % graph data) constraints)))

(defn- nil-constraint
  [name]
  (reify Constraint
    (validate [constraint graph data] #{})))

(defprotocol Directive
  (direct [directive graph data] "The set of SPOPV additions to the graph"))

(defprotocol SchemaAccessor
  (get-translator [accessor] "Returns the inner translator")
  (builtin-constraints [accessor])
  (custom-constraints [accessor])
  (builtin-meta-constraints [accessor])
  (custom-meta-constraints [accessor] "Returns custom meta-constraints")
  (check-child-constraints [accessor graph data])
  (check-custom-constraints [accessor graph data]))

(defprotocol SchemaTranslator
  (child-keyword [translator] "Returns the keyword that keys child schema")
  (get-child-keys [translator]
    "Returns the function that can be used to access a child's keys")
  (get-children [translator] "Returns the function that gets the children")
  (get-inner-data [translator]
    "Returns the function that gets the info stored on the child"))

(defprotocol BuiltinInterpreter
  (builtin-rule-to-constraint [interpreter key value]
    "Converts a schema rule to its equivalent constraint, or nil")
  (builtin-meta-rule-to-constraint [interpreter s key value]
    "Converts a schema meta-rule to its equivalent constraint, or nil"))

(defn create-schema-accessor
  [schema child-constraint-creator translator interpreter]
  (let [child-constraints
          (reduce-kv
            (fn [result s child-schema]
              (assoc result
                s (child-constraint-creator child-schema)))
            {} (schema (child-keyword translator) {}))]
    (reify
      SchemaAccessor
      (get-translator [accessor] translator)
      (builtin-constraints [accessor]
        (reduce-kv
          (fn [result k v]
            (let [constraint (builtin-rule-to-constraint interpreter k v)]
              (if (nil? constraint)
                result
                (conj result constraint))))
          [] schema))
      (custom-constraints [accessor] (schema :constraints []))
      (builtin-meta-constraints [accessor]
        (reduce-all result []
                    [[s child-schema (schema (child-keyword translator) {})]
                     [k v child-schema]]
          (let [constraint (builtin-meta-rule-to-constraint interpreter s k v)]
            (if (nil? constraint)
                result
            (conj result constraint)))))
      (custom-meta-constraints [accessor]
        (reduce-kv
          (fn [result _ child-schema]
            (concat result (child-schema :meta-constraints [])))
          [] (schema (child-keyword translator) {})))
      (check-child-constraints [accessor graph data]
        (reduce
          (fn [result s]
            (let [constraint (child-constraints s (nil-constraint s))]
              (reduce
                (fn [result v]
                  (set/union result (validate constraint graph v)))
                result ((get-children translator) data s))))
          #{} ((get-child-keys translator) data)))
      (check-custom-constraints [accessor graph data]
        (set/union
          (apply set/union (map #(validate % graph data)
                                (custom-meta-constraints accessor)))
          (apply and-constraint graph data (custom-constraints accessor))))
      Constraint
      (validate [accessor graph data]
        (let [builtin-checks
               (set/union
                 (check-child-constraints accessor graph data)
                 (apply set/union (map #(validate % graph data)
                                       (builtin-meta-constraints accessor)))
                 (apply and-constraint graph data (builtin-constraints accessor)))]
          (if (empty? builtin-checks)
              (check-custom-constraints accessor graph data)
              builtin-checks)))
      Directive
      (direct [accessor graph data]
        ; TODO - implement.
        [])
      )))

(defn- range-type-constraint
  [translator range-type allow-nodes]
  (reify Constraint
    (validate [constraint graph wrapped-data]
      (let [data ((get-inner-data translator) wrapped-data)]
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
  [tpp translator]
  (reify Constraint
    (validate [constraint graph data]
      (if (contains? (into #{} ((get-child-keys translator) data)) tpp)
          #{}
          #{(str tpp " is required")}))))

(defn- unique-constraint
  [tpp translator]
  (reify Constraint
    (validate [constraint graph data]
      (let [child-count (count ((get-children translator) data tpp))]
        (if (> child-count 1)
            #{(str tpp " is unique, but found " child-count)}
            #{})))))

(defn- exclusive-constraint
  [tpp translator exclusions]
  (reify Constraint
    (validate [constraint graph data]
      (if (empty? ((get-children translator) data tpp))
          #{}
          (let [overlap (set/intersection exclusions
                                          (into #{} ((get-child-keys translator) data)))]
          (if (empty? overlap)
              #{}
              #{(str tpp " excludes " exclusions ", but found " overlap)}))))))

(defn- create-property-interpreter
  [translator]
  (reify BuiltinInterpreter
    (builtin-rule-to-constraint [interpreter key value]
      (case key
        :range-type (range-type-constraint translator value false)
        nil))
    (builtin-meta-rule-to-constraint [interpreter s key value] nil)))

(defn- create-triple-interpreter
  [translator]
  (reify BuiltinInterpreter
    (builtin-rule-to-constraint [interpreter key value]
      (case key
        :range-type (range-type-constraint translator value true)
        nil))
    (builtin-meta-rule-to-constraint [interpreter s key value]
      (case key
        :required (if value (required-constraint s translator) nil)
        :unique (if value (unique-constraint s translator) nil)
        :exclusive (if (empty? value)
                       nil
                       (exclusive-constraint s translator value))
        nil))))

(defn- create-node-interpreter
  [translator]
  (reify BuiltinInterpreter
    (builtin-rule-to-constraint [interpreter key value] nil)
    (builtin-meta-rule-to-constraint [interpreter s key value]
      (case key
        :required (if value (required-constraint s translator) nil)
        :unique (if value (unique-constraint s translator) nil)
        :exclusive (if (empty? value)
                       nil
                       (exclusive-constraint s translator value))
        nil))))

(defn- create-graph-interpreter
  []
  (reify BuiltinInterpreter
    (builtin-rule-to-constraint [interpreter key value] nil)
    (builtin-meta-rule-to-constraint [interpreter s key value] nil)))

(defn- create-property-accessor
  [schema]
  (let [translator (reify SchemaTranslator
                     (child-keyword [translator] nil)
                     (get-child-keys [translator] (fn [_] #{}))
                     (get-children [translator] (fn [_ _] #{}))
                     (get-inner-data [translator] get-val))]
    (create-schema-accessor
      schema (fn [_] nil) translator (create-property-interpreter translator))))

(defn- create-triple-accessor
  [schema]
  (let [translator (reify SchemaTranslator
                     (child-keyword [translator] :properties)
                     (get-child-keys [translator] get-props)
                     (get-children [translator] get-properties)
                     (get-inner-data [translator] get-obj))]
    (create-schema-accessor
      schema create-property-accessor translator
      (create-triple-interpreter translator))))

(defn- create-node-accessor
  [schema]
  (let [translator (reify SchemaTranslator
                     (child-keyword [translator] :predicates)
                     (get-child-keys [translator] get-preds)
                     (get-children [translator] get-triples)
                     (get-inner-data [translator] get-sub))]
    (create-schema-accessor
      schema create-triple-accessor translator
      (create-node-interpreter translator))))

(defn create-graph-accessor
  [schema]
  (create-schema-accessor
    schema create-node-accessor
    (reify SchemaTranslator
      (child-keyword [translator] :types)
      (get-child-keys [translator] get-all-types)
      (get-children [translator] get-nodes)
      (get-inner-data [translator] nil))
    (create-graph-interpreter)))

(defn valid-type-constraint
  [types-schema]
  (let [valid-types (into #{} (keys types-schema))]
    (reify Constraint
      (validate [constraint _ graph]
        (reduce
          (fn [result type]
            (if (contains? valid-types type)
                result
                (conj result (str type " is not an allowed type"))))
          #{} (get-all-types graph))))))

(defn- valid-order-constraint
  [pred]
  (reify Constraint
    (validate [constraint graph node]
      (let [ordinals
              (into []
                (map get-val
                  (map #(first (get-properties % "/order"))
                       (get-triples node pred))))]
        (if (or (empty? ordinals)
                (apply distinct? ordinals))
            #{}
            #{(str "/orders " ordinals " on " pred " are not distinct")})))))

(defn with-order
  [pred schema]
  (assoc schema
    :properties (assoc
      (schema :properties {})
      "/order" {
        :range-type :number,
        :required true,
        :unique true,
      })
    :meta-constraints (conj
      (schema :meta-constraints [])
      (valid-order-constraint pred))
    ))

(defn abstract-type-constraint
  [types-schema]
  (let [abstract-types (reduce-kv
                         (fn [result type schema]
                           (if (schema :abstract false)
                               (conj result type)
                               result))
                         #{} types-schema)]
    (reify Constraint
      (validate [constraint _ graph]
        (reduce
          (fn [result type]
            (let [maybe-abstract-nodes (get-nodes graph type)]
              (reduce
                (fn [result node]
                  (let [types (get-types node)]
                    (if (nil? (some #(not (contains? abstract-types %)) types))
                        (conj result
                              (str (get-sub node) " only has abstract types "
                                   types))
                        result)))
                result maybe-abstract-nodes)))
          #{} abstract-types)))))

(defn required-supertypes-constraint
  [types-schema]
  (let [immediate-supertypes (reduce-kv
                               (fn [result type schema]
                                 (let [supertypes (schema :super-type #{})]
                                   (assoc result
                                     type (if (set? supertypes)
                                              supertypes
                                              #{supertypes}))))
                               {} types-schema),
        expand-type-collection (fn [types expansion-mapping]
                                 (reduce
                                   (fn [result type]
                                     (conj (set/union result
                                             (expansion-mapping type))
                                           type))
                                   #{} types))
        all-supertypes
          (loop [result immediate-supertypes]
            (let [next (reduce-kv
                         (fn [result type supertypes]
                           (assoc result type
                             (expand-type-collection supertypes immediate-supertypes)
                             ))
                         {} result)]
              (if (= next result)
                  result
                  (recur next))))]
    (reify Constraint
      (validate [constraint _ graph]
        (reduce
          (fn [result node]
            (let [types (get-types node),
                  required-types (expand-type-collection types all-supertypes)]
              (if (= types required-types)
                  result
                  (conj result
                    (str (get-sub node) " is missing required supertypes "
                         (set/difference required-types types))))))
          #{} (get-all-nodes graph))))))
