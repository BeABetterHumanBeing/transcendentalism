(ns transcendentalism.schema
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [transcendentalism.components.big-emoji :refer :all]
            [transcendentalism.components.bullet-list :refer :all]
            [transcendentalism.components.contact :refer :all]
            [transcendentalism.components.definition :refer :all]
            [transcendentalism.components.essay :refer :all]
            [transcendentalism.components.event :refer :all]
            [transcendentalism.components.image :refer :all]
            [transcendentalism.components.inline-item :refer :all]
            [transcendentalism.components.poem :refer :all]
            [transcendentalism.components.quote :refer :all]
            [transcendentalism.components.q-and-a :refer :all]
            [transcendentalism.components.segment :refer :all]
            [transcendentalism.components.table :refer :all]
            [transcendentalism.components.thesis :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.render :refer :all]))

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

(defn triples-to-graph-v3
  "Converts triples to V3 graph"
  [triples]
  (reduce
    (fn [graph triple]
      (if (empty? (:p-vs triple))
          (if (str/starts-with? (:pred triple) "/type")
              (write-o graph (:sub triple) "/type" (convert-to-type (:pred triple)))
              (write-o graph (:sub triple) (:pred triple) (:obj triple)))
          (let [obj-sub (keyword (gen-key 10))]
            (write-o (write-preds graph obj-sub (:obj triple) (:p-vs triple))
                     (:sub triple) (:pred triple) obj-sub))))
    (create-graph-v3) triples))

(defn item-component
  [graph]
  (build-component graph :item-type #{}
    {
      :description "A piece of content",
      :abstract true,
    }))

(defn raw-html-component
  [graph]
  (build-component graph :raw-html-type #{:item-type}
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
    }
    (reify Renderer
      (get-renderer-name [renderer] "html")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (unique-or-nil graph sub "/item/raw_html/contains"))
      (render-css [renderer is-mobile] "")
      (render-js [renderer] ""))))

(def component-graph
  (reduce #(merge-graph %1 (%2 %1))
    (write-v (create-graph-v3) :monad-shadow :monad)
    [event-component essay-component segment-component item-component
     inline-item-component image-component quote-component poem-component
     big-emoji-component q-and-a-component bullet-list-component contact-component
     definition-component table-component raw-html-component thesis-component]))

(defn validate-graph-v3
  "Validates a graph. If errors occur, but new graph was generated, re-runs
   validation (to catch the event where the new graph satisfied the errors)"
  [initial-graph]
  ; TODO - Multiple attempts are inefficient. Validation ought to be smart
  ; enough to remember *which* subs failed, and whiche nodes were modified, and
  ; only re-validate just those.
  (loop [graph initial-graph]
    (let [[errors final-graph] (validate (merge-graph graph component-graph))]
      (if (or (= initial-graph final-graph) (empty? errors))
          (do (doall (map println errors))
              [errors final-graph])
          (recur final-graph)))))
