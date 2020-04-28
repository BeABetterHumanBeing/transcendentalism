(ns transcendentalism.components.thesis
  (:require [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "thesis")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (div {"class" "thesis"}
          (unique-or-nil graph sub "/item/thesis/contains")))
      (render-css [renderer]
        (css "div" {"class" "thesis"}
          (border-style "solid")
          (border-width "2px")
          (border-color (to-css-color gray))
          (margin "0px" "75px")
          (padding "25px")
          (text-align "center")))
      (render-js [renderer] ""))))
