(ns transcendentalism.components.big-emoji
  (:require [transcendentalism.constraint :refer :all]))

(defn big-emoji-component
  [graph]
  (build-component graph :big-emoji-type #{:item-type}
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
