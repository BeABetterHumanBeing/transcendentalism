(ns transcendentalism.schema-data)

; The schema determines what predicates are allowed in the graph, as well as all
; constraints that bound the triples to which those predicates belong.

(defn- schematize-type
  "Expands a partial schema of a given type"
  [type type-schema schema]
  (let [full-type (str "/type" type)]
    (assoc
      (reduce-kv
        (fn [result k v]
          (assoc result (str type k) v))
        {} schema)
      full-type type-schema)))

(def event-schema
  (schematize-type "/event"
    {} {}))

(def essay-schema
  (schematize-type "/essay"
    {} {}))

(def segment-schema
  (schematize-type "/segment"
    {} {}))

(def item-schema
 (schematize-type "/item"
  {
    :abstract true,
  }
  {}))

(def inline-item-schema
  (schematize-type "/item/inline"
    {
      :super-type "/type/item",
    }
    {}
  ))

(def image-schema
  (schematize-type "/item/image"
    {
      :super-type "/type/item",
    }
    {}))

(def quote-schema
  (schematize-type "/item/quote"
    {
      :super-type "/type/item",
    }
    {}))

(def poem-schema
  (schematize-type "/item/poem"
    {
      :super-type "/type/item",
    }
    {}))

(def big-emoji-schema
  (schematize-type "/item/big_emoji"
    {
      :super-type "/type/item",
    }
    {}))

(def q-and-a-schema
  (schematize-type "/item/q_and_a"
    {
      :super-type "/type/item",
    }
    {}))

(def bullet-list-schema
  (schematize-type "/item/bullet_list"
    {
      :super-type "/type/item",
    }
    {}))

(def contact-schema
  (schematize-type "/item/contact"
    {
      :super-type "/type/item",
    }
    {}))

(def definition-schema
  (schematize-type "/item/definition"
    {
      :super-type "/type/item",
    }
    {}))

(def table-schema
  (schematize-type "/item/table"
    {
      :super-type "/type/item",
    }
    {}))

(def raw-html-schema
  (schematize-type "/item/raw_html"
    {
      :super-type "/type/item",
    }
    {}))

(def thesis-schema
  (schematize-type "/item/thesis"
    {
      :super-type "/type/item",
    }
    {}))

(def schema-data
  (merge essay-schema event-schema image-schema quote-schema inline-item-schema
    poem-schema segment-schema big-emoji-schema item-schema q-and-a-schema
    bullet-list-schema contact-schema definition-schema table-schema
    raw-html-schema thesis-schema))
