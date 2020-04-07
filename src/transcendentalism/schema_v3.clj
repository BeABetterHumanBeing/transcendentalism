(ns transcendentalism.schema-v3
  (:require [transcendentalism.constraint-v3 :refer :all]
            [transcendentalism.graph-v3 :refer :all]))

; TODO - Finish translating V2 schema into V3 schema.

; TODO - Move all of the schema out of this file into components, ultimately
; removing it.

(defn graph-to-v3
  "Converts V1 to V3 graph"
  [graph-v1]
  ; TODO - Complete
  (create-graph-v3))

(defn event-type
  [graph]
  (build-type-graph graph :event-type #{}
    {
      :description "An event",
      :preds {
        "/leads_to" {
          :description "Relation from one event to its subsequent impacts",
          :range-type :event-type,
        },
        "/time" {
          :description "When an event happened",
          :range-type :time,
          :required true,
          :unique true,
        },
      },
    }))

(defn essay-type
  [graph]
  (build-type-graph graph :essay-type #{}
    {
      :description "Nodes that are externally link-able",
      :preds {
        "/title" {
          :description "The text that appears centered at the top of an essay",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/flow/next" {
          :description "Relation to the next essay",
          :range-type :essay-type,
        },
        "/flow/home" {
          :description "Relation to the 'parent' essay",
          :range-type :essay-type,
          :required true,
          :unique true,
          :default :monad,
          :preds {
            "/label" {
              :description "Metadata about the home",
              :range-type [
                :menu ; If the essay belongs to a menu,
              ],
            },
          },
        },
        "/flow/see_also" {
          :description "Internal link to another essay",
          :range-type :essay-type,
        },
        "/flow/menu" {
          :description "Internal link to an essay menu",
          :range-type :essay-type,
          :preds {
            ; Note that this check is redundant with the /title above, unless
            ; range-type were made an intermediate sub whose value-type is an
            ; :essay-type
            "/title" {
              :description "The title of the menu",
              :range-type :string,
              :required true,
            },
          },
        },
        "/flow/random" {
          :description "Relation to a random essay",
          ; TODO - range ought to be a set of essays
          :range-type nil,
        },
        "/contains" {
          :description "Relation from an essay to the segment it contains",
          :range-type :segment-type,
          :unique true,
          :required true,
        },
        "/label" {
          :description "Symbol label that ascribes a metadata to the essay",
          :range-type #{
            ; Content is not rendered.
            :invisible
            ; Content is under construction.
            :under-construction
            ; Content is religious.
            :religion
            ; Content is political.
            :politics}
        },
      },
    }))

(defn segment-type
 [graph]
 (build-type-graph graph :segment-type #{}
   {
     :description "Nodes that are internally linkable",
     :preds {
       "/flow/block" {
         :description "Relation to the next block segment",
         :range-type :segment-type,
         :unique true,
       },
       "/flow/inline" {
         :description "Relation to the second inline segment",
         :range-type :segment-type,
         :unique true,
       },
       "/contains" {
         :description "Relation from a segment to the item it contains",
         :range-type :item-type,
         :unique true,
         :required true,
       },
       "/author" {
         :description "Who wrote the segment",
         :range-type :string,
       },
      },
    }))

(defn item-type
  [graph]
  (build-type-graph graph :item-type #{}
    {
      :description "A piece of content",
      :abstract true,
    }))

(defn inline-item-type
  [graph]
  (build-type-graph graph :inline-item-type #{:item-type}
    {
      :description "Content that can be inlined",
      :mutually-exclusive #{"/tangent" "/url" "/see_also" "/definition"},
      :preds {
        "/text" {
          :description "The text that appears inline",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/tangent" {
          :description "The item which clicking on this toggles",
          :range-type :segment-type,
          :unique true,
        },
        "/url" {
          :description "The external URL to which the text is linked",
          :range-type :string,
          :unique true,
        },
        "/see_also" {
          :description "Another essay which is relevant",
          :range-type :essay-type,
          :unique true,
        },
        "/definition" {
          :description "A definition which clicking on this toggles",
          :range-type :segment-type,
          :unique true,
        },
      },
    }))

(defn image-type
  [graph]
  (build-type-graph graph :image-type #{:item-type}
    {
      :description "Image content",
      :preds {
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
        "/width" {
          :description "The width to render the image",
          :range-type :number,
          :unique true,
        },
        "/height" {
          :description "The height to render the image",
          :range-type :number,
          :unique true,
        },
      },
    }))

(defn quote-type
  [graph]
  (build-type-graph graph :quote-type #{:item-type}
    {
      :description "A quote",
      :preds {
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
      },
    }))

(defn poem-type
  [graph]
  (build-type-graph graph :poem-type #{:item-type}
    {
      :description "A poem",
      ; TODO implement ordered. May be possible to remove with-order below, and
      ; check that internally. Would be ideal.
      :ordered #{"/line"},
      :preds {
        "/line" (with-order {
          :description "A line that appears in the poem",
          :range-type :string,
          :required true,
        }),
      },
    }))

; (def big-emoji-schema
;   (schematize-type "/item/big_emoji"
;     {
;       :description "A series of big emoji",
;       :super-type "/type/item",
;     }
;     {
;       "/emoji" {
;         :description "The sequence of emoji to render",
;         :range-type :string,
;         :required true,
;         :unique true,
;       },
;     }))

; (def q-and-a-schema
;   (schematize-type "/item/q_and_a"
;     {
;       :description "A question and answer",
;       :super-type "/type/item",
;     }
;     {
;       "/question" {
;         :description "The question being asked",
;         :range-type "/type/segment",
;         :required true,
;         :unique true,
;       },
;       "/answer" {
;         :description "The answer being given",
;         :range-type "/type/segment",
;         :required true,
;         :unique true,
;       },
;     }))

; (def bullet-list-schema
;   (schematize-type "/item/bullet_list"
;     {
;       :description "A list with bullet points",
;       :super-type "/type/item",
;     }
;     {
;       "/header" {
;         :description "What appears above the list to introduce it",
;         :range-type "/type/segment",
;         :unique true,
;       }
;       "/point" (with-order "/item/bullet_list/point" {
;         :description "A bullet-pointed item. Uses :order property to sort",
;         :range-type "/type/segment",
;         :required true,
;       }),
;       "/is_ordered" {
;         :description "Whether the list should be ordered, or bullet-pointed",
;         :range-type :bool,
;         :unique true,
;         :required true,
;       },
;     }))

; (def contact-schema
;   (schematize-type "/item/contact"
;     {
;       :description "Provides contact information",
;       :super-type "/type/item",
;     }
;     {
;       "/email" {
;         :description "An email address that can be mailed to",
;         :range-type :string,
;         :unique true,
;         :required true,
;       },
;     }))

; (def definition-schema
;   (schematize-type "/item/definition"
;     {
;       :description "Provides a definition for a word",
;       :super-type "/type/item",
;     }
;     {
;       "/word" {
;         :description "The word",
;         :range-type :string,
;         :unique true,
;         :required true,
;       },
;       "/part_of_speech" {
;         :description "The part of speech the word belongs to",
;         :range-type [:noun :adjective],
;         :unique true,
;         :required true,
;       },
;       "/definition" (with-order "/item/definition/definition" {
;         :description "A definition of the word",
;         :range-type :string,
;         :required true,
;       }),
;     }))

; (def table-schema
;   (schematize-type "/item/table"
;     {
;       :description "A 2D table of items",
;       :super-type "/type/item",
;     }
;     {
;       "/cell" {
;         :description "A cell in the table",
;         :range-type "/type/segment",
;         :required true,
;         :properties {
;           "/column" {
;             :description "The column the cell appears in",
;             :range-type :number,
;             :required true,
;           },
;           "/row" {
;             :description "The row the cell appears in",
;             :range-type :number,
;             :required true,
;           },
;         }
;       },
;       "/label" {
;         :description "A label for a column or row",
;         :range-type "/type/segment"
;         :properties {
;           "/column" {
;             :description "The column the label applies to",
;             :range-type :number,
;             :exclusive #{"/row"},
;           },
;           "/row" {
;             :description "The row the label applies to",
;             :range-type :number,
;             :exclusive #{"/column"},
;           },
;         },
;       },
;     }))

; (def raw-html-schema
;   (schematize-type "/item/raw_html"
;     {
;       :description "Raw HTML, bypassing the schema. Use with caution.",
;       :super-type "/type/item",
;     }
;     {
;       "/contains" {
;         :description "The HTML content as a string",
;         :range-type :string,
;         :unique true,
;         :required true,
;       },
;     }))

; (def thesis-schema
;   (schematize-type "/item/thesis"
;     {
;       :description "A thesis, key point, or other information to call out",
;       :super-type "/type/item",
;     }
;     {
;       "/contains" {
;         :description "The thesis",
;         :range-type :string,
;         :unique true,
;         :required true,
;       },
;     }))

(def type-graph
  (reduce #(merge-graph %1 (%2 %1))
    (create-graph-v3)
    [event-type essay-type segment-type item-type inline-item-type image-type
     quote-type poem-type]))

(defn validate-graph-v3
  [graph]
  (let [[errors final-graph] (validate (merge-graph graph type-graph))]
    (doall (map #(println %) errors))
    (empty? errors)))
