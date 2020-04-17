(ns transcendentalism.components.poem
  (:require [transcendentalism.constraint :refer :all]))

(defn poem-component
  [graph]
  (build-component graph :poem-type #{:item-type}
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
