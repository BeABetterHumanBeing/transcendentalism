(ns transcendentalism.schema-v3
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [transcendentalism.constraint-v3 :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph :as g1]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

; TODO - Move all of the schema out of this file into components, ultimately
; removing it.

(defn- write-preds
  [graph sub obj p-vs]
  (reduce-kv
    (fn [result pred val]
      (write-o result sub pred val))
    (write-v graph sub obj) p-vs))

(defn- read-preds
  [graph obj]
  (reduce
    (fn [result pred]
      (reduce
        (fn [result obj]
          ; Assumes V1 properties are unique.
          (assoc result pred obj))
        result (read-os graph obj pred)))
    {} (read-ps graph obj)))

(defn- convert-to-type
  [pred]
  (case pred
    "/type/event" :event-type
    "/type/essay" :essay-type
    "/type/segment" :segment-type
    "/type/item" :item-type
    "/type/item/inline" :inline-item-type
    "/type/item/image" :image-type
    "/type/item/quote" :quote-type
    "/type/item/poem" :poem-type
    "/type/item/big_emoji" :big-emoji-type
    "/type/item/q_and_a" :q-and-a-type
    "/type/item/bullet_list" :bullet-list-type
    "/type/item/contact" :contact-type
    "/type/item/definition" :definition-type
    "/type/item/table" :table-type
    "/type/item/raw_html" :raw-html-type
    "/type/item/thesis" :thesis-type
    (assert false (str pred " not supported"))))

(defn- convert-from-type
  [sub objs]
  (let [obj (if (= (count objs) 1)
                (first objs)
                (first (set/difference objs #{:item-type})))]
    (g1/->Triple sub
                 (case obj
                   :type "/type"
                   :event-type "/type/event"
                   :essay-type "/type/essay"
                   :segment-type "/type/segment"
                   :item-type "/type/item"
                   :inline-item-type "/type/item/inline"
                   :image-type "/type/item/image"
                   :quote-type "/type/item/quote"
                   :poem-type "/type/item/poem"
                   :big-emoji-type "/type/item/big_emoji"
                   :q-and-a-type "/type/item/q_and_a"
                   :bullet-list-type "/type/item/bullet_list"
                   :contact-type "/type/item/contact"
                   :definition-type "/type/item/definition"
                   :table-type "/type/item/table"
                   :raw-html-type "/type/item/raw_html"
                   :thesis-type "/type/item/thesis"
                   (assert false (str obj " not supported")))
                 nil {})))

(defn graph-to-v3
  "Converts V1 to V3 graph"
  [graph-v1]
  (let [triples (g1/all-triples graph-v1)]
    (reduce
      (fn [graph triple]
        (if (empty? (:p-vs triple))
            (if (str/starts-with? (:pred triple) "/type")
                (write-o graph (:sub triple) "/type" (convert-to-type (:pred triple)))
                (write-o graph (:sub triple) (:pred triple) (:obj triple)))
            (let [obj-sub (keyword (gen-key 10))]
              (write-o (write-preds graph obj-sub (:obj triple) (:p-vs triple))
                       (:sub triple) (:pred triple) obj-sub))))
      (create-graph-v3) triples)))

(defn graph-to-v1
  [graph-v3]
  (g1/construct-graph
    (reduce
      (fn [result sub]
        (if (nil? (read-v graph-v3 sub))
            (reduce
              (fn [result pred]
                (if (= pred "/type")
                    (conj result (convert-from-type sub (read-os graph-v3 sub pred)))
                    (reduce
                      (fn [result obj]
                        (if (or (not (keyword? obj))
                                (nil? (read-v graph-v3 obj)))
                            (conj result (g1/->Triple sub pred obj {}))
                            (conj result (g1/->Triple sub pred (read-v graph-v3 obj)
                                                               (read-preds graph-v3 obj)))))
                      result (read-os graph-v3 sub pred))))
              result (read-ps graph-v3 sub))
            result))
      [] (read-ss graph-v3))))

(defn event-type
  [graph]
  (build-type-graph graph :event-type #{}
    {
      :description "An event",
      :constraints [
        ; Check that /event/leads_to goes strictly forward in time.
        (reify ConstraintV3
          (check-constraint [constraint graph sub]
            (let [t (unique-or-nil graph sub "/event/time"),
                  sub-ts (map #(unique-or-nil graph % "/event/time")
                              (read-os graph sub "/event/leads_to"))]
              [(reduce
                 (fn [result sub-t]
                   (if (before? (to-time t) (to-time sub-t))
                       result
                       (conj result
                             (str t " /event/leads_to an earlier time " sub-t))))
                 #{} sub-ts)
               graph])))
      ],
      :preds {
        "/event/leads_to" {
          :description "Relation from one event to its subsequent impacts",
          :range-type :event-type,
        },
        "/event/time" {
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
      :constraints [
        ; Check that /essay/flow/home eventually leads to :monad.
        (reify ConstraintV3
          (check-constraint [constraint graph sub]
            (let [homes (read-path graph sub (p* ["/essay/flow/home" "/"]))]
              (if (or (contains? homes :monad) (contains? homes nil))
                  [#{} graph]
                  [#{(str sub " /essay/flow/home does not lead to :monad")}
                   graph]))))
      ],
      :preds {
        "/essay/title" {
          :description "The text that appears centered at the top of an essay",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/essay/flow/next" {
          :description "Relation to the next essay",
          :range-type :essay-type,
        },
        "/essay/flow/home" {
          :description "Relation to the 'parent' essay",
          :value-type :essay-type,
          :required true,
          :unique true,
          :default :monad-shadow,
          :preds {
            "/label" {
              :description "Metadata about the home",
              :range-type #{
                :none ; Hack to ensure that V1->V3 conversion uses pred-aware path.
                :menu ; If the essay belongs to a menu,
              },
            },
          },
        },
        "/essay/flow/see_also" {
          :description "Internal link to another essay",
          :range-type :essay-type,
        },
        "/essay/flow/menu" {
          :description "Internal link to an essay menu",
          :value-type :essay-type,
          :preds {
            "/title" {
              :description "The title of the menu",
              :range-type :string,
              :required true,
            },
          },
        },
        "/essay/flow/random" {
          :description "Relation to a random essay",
          ; TODO - range ought to be a set of essays
          :range-type nil,
        },
        "/essay/contains" {
          :description "Relation from an essay to the segment it contains",
          :range-type :segment-type,
          :unique true,
          :required true,
        },
        "/essay/label" {
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
       "/segment/flow/block" {
         :description "Relation to the next block segment",
         :range-type :segment-type,
         :unique true,
       },
       "/segment/flow/inline" {
         :description "Relation to the second inline segment",
         :range-type :segment-type,
         :unique true,
       },
       "/segment/contains" {
         :description "Relation from a segment to the item it contains",
         :range-type :item-type,
         :unique true,
         :required true,
       },
       "/segment/author" {
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
        "/item/inline/text" {
          :description "The text that appears inline",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/inline/tangent" {
          :description "The item which clicking on this toggles",
          :range-type :segment-type,
          :unique true,
        },
        "/item/inline/url" {
          :description "The external URL to which the text is linked",
          :range-type :string,
          :unique true,
        },
        "/item/inline/see_also" {
          :description "Another essay which is relevant",
          :range-type :essay-type,
          :unique true,
        },
        "/item/inline/definition" {
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
        "/item/image/url" {
          :description "URL of image",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/image/alt_text" {
          :description "Alt text of image",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/image/width" {
          :description "The width to render the image",
          :range-type :number,
          :unique true,
        },
        "/item/image/height" {
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
        "/item/quote/text" {
          :description "The text contents of the quote",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/quote/author" {
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
        "/item/poem/line" {
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
        "/item/big_emoji/emoji" {
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
        "/item/q_and_a/question" {
          :description "The question being asked",
          :range-type :segment-type,
          :required true,
          :unique true,
        },
        "/item/q_and_a/answer" {
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
        "/item/bullet_list/header" {
          :description "What appears above the list to introduce it",
          :range-type :segment-type,
          :unique true,
        }
        "/item/bullet_list/point" {
          :description "A bullet-pointed item. Uses :order property to sort",
          :value-type :segment-type,
          :required true,
          :ordered true,
        },
        "/item/bullet_list/is_ordered" {
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
        "/item/contact/email" {
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
        "/item/definition/word" {
          :description "The word",
          :range-type :string,
          :unique true,
          :required true,
        },
        "/item/definition/part_of_speech" {
          :description "The part of speech the word belongs to",
          :range-type #{:noun :adjective},
          :unique true,
          :required true,
        },
        "/item/definition/definition" {
          :description "A definition of the word",
          :value-type :string,
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
        "/item/table/cell" {
          :description "A cell in the table",
          :value-type :segment-type,
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
        "/item/table/label" {
          :description "A label for a column or row",
          :value-type :segment-type,
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
        "/item/raw_html/contains" {
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
        "/item/thesis/contains" {
          :description "The thesis",
          :range-type :string,
          :unique true,
          :required true,
        },
      },
    }))

(def type-graph
  (reduce #(merge-graph %1 (%2 %1))
    (write-v (create-graph-v3) :monad-shadow :monad)
    [event-type essay-type segment-type item-type inline-item-type image-type
     quote-type poem-type big-emoji-type q-and-a-type bullet-list-type
     contact-type definition-type table-type raw-html-type thesis-type]))

(defn validate-graph-v3
  [graph]
  (let [[errors final-graph] (validate (merge-graph graph type-graph))]
    (doall (map #(println %) errors))
    [errors final-graph]))
