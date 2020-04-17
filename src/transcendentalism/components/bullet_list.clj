(ns transcendentalism.components.bullet-list
  (:require [transcendentalism.constraint :refer :all]))

(defn bullet-list-component
  [graph]
  (build-component graph :bullet-list-type #{:item-type}
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
