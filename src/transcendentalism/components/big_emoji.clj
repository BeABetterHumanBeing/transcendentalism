(ns transcendentalism.components.big-emoji
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "big-emoji")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (div {"class" "emoji"}
          (unique-or-nil graph sub "/item/big_emoji/emoji")))
      (render-css [renderer is-mobile]
        (css "div" {"class" "emoji"}
          (font-size "100px")
          (text-align "center")))
      (render-js [renderer] ""))))
