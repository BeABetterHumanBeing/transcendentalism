(ns transcendentalism.schema-v3
  (:require [clojure.string :as str]
            [transcendentalism.constraint-v3 :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph :as g1]
            [transcendentalism.graph-v3 :refer :all]))

; TODO - Move all of the schema out of this file into components, ultimately
; removing it.

(defn write-preds
  [graph sub obj p-vs]
  (reduce-kv
    (fn [result pred val]
      (write-o result sub pred val))
    (write-v graph sub obj) p-vs))

(defn convert-to-type
  [pred]
  (case pred
    "/type/event" :event-type
    "/type/essay" :essay-type
    "/type/segment" :segment-type
    "/type/item" :item-type
    "/type/inline_item" :inline-item-type
    "/type/image" :image-type
    "/type/quote" :quote-type
    "/type/poem" :poem-type
    "/type/big_emoji" :big-emoji-type
    "/type/q_and_a" :q-and-a-type
    "/type/bullet_list" :bullet-list-type
    "/type/contact" :contact-type
    "/type/definition" :definition-type
    "/type/table" :table-type
    "/type/raw_html" :raw-html-type
    "/type/thesis" :thesis-type
    (assert (str pred " not supported"))))

(defn strip-pred
  [triple]
  (str "/" (last (re-seq #"\w+" (:pred triple)))))

(defn graph-to-v3
  "Converts V1 to V3 graph"
  [graph-v1]
  (let [triples (g1/all-triples graph-v1)]
    (reduce
      (fn [graph triple]
        (if (empty? (:p-vs triple))
            (if (str/starts-with? (:pred triple) "/type")
                (write-o graph (:sub triple) "/type" (convert-to-type (:pred triple)))
                (write-o graph (:sub triple) (strip-pred triple) (:obj triple)))
            (let [obj-sub (keyword (gen-key 10))]
              (write-o (write-preds graph obj-sub (:obj triple) (:p-vs triple))
                       (:sub triple) (strip-pred triple) obj-sub))))
      (create-graph-v3) triples)))

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
      :preds {
        "/line" {
          :description "A line that appears in the poem",
          :value-type :string,
          :required true,
          :ordered true,
        },
      },
    }))

(defn big-emoji-type
  [graph]
  (build-type-graph graph :big-emoji-type #{:item-type}
    {
      :description "A series of big emoji",
      :preds {
        "/emoji" {
          :description "The sequence of emoji to render",
          :range-type :string,
          :required true,
          :unique true,
        },
      },
    }))

(defn q-and-a-type
  [graph]
  (build-type-graph graph :q-and-a-type #{:item-type}
    {
      :description "A question and answer",
      :preds {
        "/question" {
          :description "The question being asked",
          :range-type :segment-type,
          :required true,
          :unique true,
        },
        "/answer" {
          :description "The answer being given",
          :range-type :segment-type,
          :required true,
          :unique true,
        },
      },
    }))

(defn bullet-list-type
  [graph]
  (build-type-graph graph :bullet-list-type #{:item-type}
    {
      :description "A list with bullet points",
      :preds {
        "/header" {
          :description "What appears above the list to introduce it",
          :range-type :segment-type,
          :unique true,
        }
        "/point" {
          :description "A bullet-pointed item. Uses :order property to sort",
          :range-type :segment-type,
          :required true,
          :ordered true,
        },
        "/is_ordered" {
          :description "Whether the list should be ordered, or bullet-pointed",
          :range-type :bool,
          :unique true,
          :required true,
        },
      },
    }))

(defn contact-type
  [graph]
  (build-type-graph graph :contact-type #{:item-type}
    {
      :description "Provides contact information",
      :preds {
        "/email" {
          :description "An email address that can be mailed to",
          :range-type :string,
          :unique true,
          :required true,
        },
      },
    }))

(defn definition-type
  [graph]
  (build-type-graph graph :definition-type #{:item-type}
    {
      :description "Provides a definition for a word",
      :preds {
        "/word" {
          :description "The word",
          :range-type :string,
          :unique true,
          :required true,
        },
        "/part_of_speech" {
          :description "The part of speech the word belongs to",
          :range-type #{:noun :adjective},
          :unique true,
          :required true,
        },
        "/definition" {
          :description "A definition of the word",
          :range-type :string,
          :required true,
          :ordered true,
        },
      },
    }))

(defn table-type
  [graph]
  (build-type-graph graph :table-type #{:item-type}
    {
      :description "A 2D table of items",
      :preds {
        "/cell" {
          :description "A cell in the table",
          :range-type :segment-type,
          :required true,
          :properties {
            "/column" {
              :description "The column the cell appears in",
              :range-type :number,
              :required true,
            },
            "/row" {
              :description "The row the cell appears in",
              :range-type :number,
              :required true,
            },
          }
        },
        "/label" {
          :description "A label for a column or row",
          :range-type :segment-type,
          :mutually-exclusive #{"/row" "/column"},
          :properties {
            "/column" {
              :description "The column the label applies to",
              :range-type :number,
            },
            "/row" {
              :description "The row the label applies to",
              :range-type :number,
            },
          },
        },
      },
    }))

(defn raw-html-type
  [graph]
  (build-type-graph graph :raw-html-type #{:item-type}
    {
      :description "Raw HTML, bypassing the schema. Use with caution.",
      :preds {
        "/contains" {
          :description "The HTML content as a string",
          :range-type :string,
          :unique true,
          :required true,
        },
      },
    }))

(defn thesis-type
  [graph]
  (build-type-graph graph :thesis-type #{:item-type}
    {
      :description "A thesis, key point, or other information to call out",
      :preds {
        "/contains" {
          :description "The thesis",
          :range-type :string,
          :unique true,
          :required true,
        },
      },
    }))

(def type-graph
  (reduce #(merge-graph %1 (%2 %1))
    (create-graph-v3)
    [event-type essay-type segment-type item-type inline-item-type image-type
     quote-type poem-type big-emoji-type q-and-a-type bullet-list-type
     definition-type table-type raw-html-type thesis-type]))

(defn validate-graph-v3
  [graph]
  (let [[errors final-graph] (validate (merge-graph graph type-graph))]
    (doall (map #(println %) errors))
    (empty? errors)))
