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
    {}
    {
      "/leads_to" {},
      "/time" {},
    }))

(def essay-schema
  (schematize-type "/essay"
    {}
    {
      "/title" {},
      "/flow/next" {},
      "/flow/home" {
        :properties {
          "/label" {},
        },
      },
      "/flow/see_also" {},
      "/flow/menu" {
        :properties {
          "/title" {},
        },
      },
      "/flow/random" {},
      "/contains" {},
      "/label" {},
    }))

(def segment-schema
  (schematize-type "/segment"
    {}
    {
      "/flow/block" {},
      "/flow/inline" {},
      "/contains" {},
      "/author" {},
    }))

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
    {
      "/text" {},
      "/tangent" {},
      "/url" {},
      "/see_also" {},
      "/definition" {},
    },
  ))

(def image-schema
  (schematize-type "/item/image"
    {
      :super-type "/type/item",
    }
    {
      "/url" {},
      "/alt_text" {},
      "/width" {},
      "/height" {},
    }))

(def quote-schema
  (schematize-type "/item/quote"
    {
      :super-type "/type/item",
    }
    {
      "/text" {},
      "/author" {},
    }))

(def poem-schema
  (schematize-type "/item/poem"
    {
      :super-type "/type/item",
    }
    {
      "/line" {
        :properties {
          "/order" {},
        },
      },
    }))

(def big-emoji-schema
  (schematize-type "/item/big_emoji"
    {
      :super-type "/type/item",
    }
    {
      "/emoji" {},
    }))

(def q-and-a-schema
  (schematize-type "/item/q_and_a"
    {
      :super-type "/type/item",
    }
    {
      "/question" {},
      "/answer" {},
    }))

(def bullet-list-schema
  (schematize-type "/item/bullet_list"
    {
      :super-type "/type/item",
    }
    {
      "/header" {},
      "/point" {
        :properties {
          "/order" {},
        },
      },
      "/is_ordered" {},
    }))

(def contact-schema
  (schematize-type "/item/contact"
    {
      :super-type "/type/item",
    }
    {
      "/email" {},
    }))

(def definition-schema
  (schematize-type "/item/definition"
    {
      :super-type "/type/item",
    }
    {
      "/word" {},
      "/part_of_speech" {},
      "/definition" {
        :properties {
          "/order" {},
        },
      },
    }))

(def table-schema
  (schematize-type "/item/table"
    {
      :super-type "/type/item",
    }
    {
      "/cell" {
        :properties {
          "/column" {},
          "/row" {},
        }
      },
      "/label" {
        :properties {
          "/column" {},
          "/row" {},
        },
      },
    }))

(def raw-html-schema
  (schematize-type "/item/raw_html"
    {
      :super-type "/type/item",
    }
    {
      "/contains" {},
    }))

(def thesis-schema
  (schematize-type "/item/thesis"
    {
      :super-type "/type/item",
    }
    {
      "/contains" {},
    }))

(def schema-data
  (merge essay-schema event-schema image-schema quote-schema inline-item-schema
    poem-schema segment-schema big-emoji-schema item-schema q-and-a-schema
    bullet-list-schema contact-schema definition-schema table-schema
    raw-html-schema thesis-schema))
