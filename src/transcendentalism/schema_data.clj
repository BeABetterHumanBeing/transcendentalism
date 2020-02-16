(ns transcendentalism.schema-data)

; The schema determines what predicates are allowed in the graph, as well as all
; constraints that bound the triples to which those predicates belong.
(def extra-schema {
  "/type/item" {
    :description "A piece of content",
  },
  "/type/item/text" {
    :description "Textual content",
    :super-type "/type/item",
  },
  "/type/item/ordered_set" {
    :description "An ordered collection of items",
    :super-type "/type/item",
  },
  "/item/contains" {
    :description "Relation to a child node of an ordered set",
    :domain-type "/type/item/ordered_set",
    :range-type "/type/item",
  },
  "/item/footnote" {
    :description "Relation to a piece of footnote content",
    :domain-type "/type/essay",
    :range-type "/type/item",
  },
  "/item/text/text" {
    :description "The contents of a text item",
    :domain-type "/type/item/text",
    :range-type :string,
    :required true,
  },
})

(defn- schematize-type
  "Expands a partial schema of a given type"
  [type type-schema schema]
  (let [full-type (str "/type" type)]
    (assoc
      (reduce-kv
        (fn [result k v]
          (assoc result
            (str type k)
            (assoc v
              :domain-type
              full-type
              :range-type
              (if (contains? v :range-type) (:range-type v) full-type))))
        {} schema)
      full-type type-schema)))

(def event-schema
  (schematize-type "/event"
    {
      :description "An event",
    }
    {
      "/leads_to" {
        :description "Relation from one event to its subsequent impacts",
      },
      "/time" {
        :description "When an event happened",
        :range-type :time,
        :required true,
        :unique true,
      },
    }))

(def essay-schema
  (schematize-type "/essay"
    {
      :description "Nodes that are externally link-able",
    }
    {
      "/title" {
        :description "The text that appears centered at the top of an essay",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/flow/next" {
        :description "Relation to the next essay",
      },
      "/flow/home" {
        :description "Relation to the 'parent' essay",
        :required true,
        :unique true,
      },
      "/flow/see_also" {
        :description "Internal link to another essay",
      },
      "/contains" {
        :description "Relation from an essay to the item it contains",
        :range-type "/type/item",
        :unique true,
        :required true,
      },
      "/label" {
        :description "Symbol label that ascribes a metadata to the essay",
        :range-type [
          ; Content is about surrounding content.
          :meta
          ; Content is under construction.
          :under-construction
          ; Content is religious.
          :religion
          ; Content is political.
          :politics]
      },
    }))

(def segment-schema
  (schematize-type "/segment"
    {
      :description "Nodes that are internally linkable",
    }
    {
      "/flow/block" {
        :description "Relation to the next block segment",
        :unique true,
      },
      "/flow/tangent" {
        :description "Relation to a footnote segment",
      },
      "/contains" {
        :description "Relation from a segment to the item it contains",
        :range-type "/type/item",
        :unique true,
        :required true,
      },
    }))

(def inline-segment-schema
  (schematize-type "/segment/inline"
    {
      :description "Segments that can be inlined",
      :super-type "/type/segment",
    }
    {
      "/flow/inline" {
        :description "Relation to the next inline segment",
        :unique true,
      },
      "/contains" {
        :description "Relation from a segment to the item it contains",
        :range-type "/type/item/inline",
        :unique true,
        :required true,
      },
    }))

(def image-schema
  (schematize-type "/item/image"
    {
      :description "Image content",
      :super-type "/type/item",
    }
    {
      "/url" {
        :description "URL of image",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/alt_text" {
        :description "Alt text of image",
        :range-type :string,
        :required true,
        :unique true,
      },
    }))

(def quote-schema
  (schematize-type "/item/quote"
    {
      :description "A quote",
      :super-type "/type/item",
    }
    {
      "/text" {
        :description "The text contents of the quote",
        :range-type :string,
        :required true,
        :unique true,
      },
      "/author" {
        :description "To whom the quote is attributed",
        :range-type :string,
        :unique true,
      },
    }))

(def poem-schema
  (schematize-type "/item/poem"
    {
      :description "A poem",
      :super-type "/type/item",
    }
    {
      "/line" {
        :description "A line that appears in the poem",
        :range-type :string,
        :required true,
      },
    }))

(def big-emoji-schema
  (schematize-type "/item/big_emoji"
    {
      :description "A series of big emoji",
      :super-type "/type/item",
    }
    {
      "/emoji" {
        :description "The sequence of emoji to render",
        :domain-type "/type/item/big_emoji",
        :range-type :string,
        :required true,
        :unique true,
      },
    }))

(def schema-data
  (merge extra-schema essay-schema event-schema image-schema quote-schema
    poem-schema segment-schema inline-segment-schema big-emoji-schema))