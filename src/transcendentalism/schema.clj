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
            [transcendentalism.graph :as g1]
            [transcendentalism.graph-v3 :refer :all]))

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
    }))

(def component-graph
  (reduce #(merge-graph %1 (%2 %1))
    (write-v (create-graph-v3) :monad-shadow :monad)
    [event-component essay-component segment-component item-component
     inline-item-component image-component quote-component poem-component
     big-emoji-component q-and-a-component bullet-list-component contact-component
     definition-component table-component raw-html-component thesis-component]))

(defn validate-graph-v3
  [graph]
  (let [[errors final-graph] (validate (merge-graph graph component-graph))]
    (doall (map #(println %) errors))
    [errors final-graph]))
