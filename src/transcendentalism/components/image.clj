(ns transcendentalism.components.image
  (:require [clojure.string :as str]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
          :required true,
        },
        "/item/image/height" {
          :description "The height to render the image",
          :range-type :number,
          :unique true,
          :required true,
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "image")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [image-url-triple (unique-or-nil graph sub "/item/image/url"),
              image-alt-text-triple (unique-or-nil graph sub "/item/image/alt_text"),
              image-width (unique-or-nil graph sub "/item/image/width"),
              image-height (unique-or-nil graph sub "/item/image/height"),
              height (str "height:" image-height "px")]
          (div {"class" "img-back",
                "style" (str "width:auto;" height)}
            (img {"class" "img-item",
                  "src" image-url-triple,
                  "alt" image-alt-text-triple,
                  "style" (str (str "width:" image-width "px")
                               ";"
                               height)}))))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (css "div" {"class" "img-back"}
            (position "relative"))
          (css "img" {"class" "img-item"}
            (position "absolute")
            (left "50%")
            (transform (translate "-50%" "0")))]))
      (render-js [renderer] ""))))
