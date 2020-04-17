(ns transcendentalism.components.thesis
  (:require [transcendentalism.constraint :refer :all]))

(defn thesis-component
  [graph]
  (build-component graph :thesis-type #{:item-type}
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
