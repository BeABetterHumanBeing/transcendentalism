(ns transcendentalism.components.image
  (:require [transcendentalism.constraint :refer :all]))

(defn image-component
  [graph]
  (build-component graph :image-type #{:item-type}
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
