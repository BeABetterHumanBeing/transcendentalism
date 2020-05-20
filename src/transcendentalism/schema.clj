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
            [transcendentalism.graph :refer :all]
            [transcendentalism.render :refer :all]))

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
    (write-v (create-graph) :monad-shadow :monad)
    [event-component essay-component segment-component item-component
     inline-item-component image-component quote-component poem-component
     big-emoji-component q-and-a-component bullet-list-component contact-component
     definition-component table-component raw-html-component thesis-component]))
