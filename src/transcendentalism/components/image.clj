(ns transcendentalism.components.image
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
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
        },
        "/item/image/height" {
          :description "The height to render the image",
          :range-type :number,
          :unique true,
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
              image-height (unique-or-nil graph sub "/item/image/height")]
          (img {"src" image-url-triple,
                "alt" image-alt-text-triple,
                "style" (str (if (nil? image-width)
                                 ""
                                 (str "width:" image-width "px"))
                             ";"
                             (if (nil? image-height)
                                 ""
                                 (str "height:" image-height "px")))})))
      (render-css [renderer is-mobile]
        (css "img" {}
          (margin "0" "auto")
          (display "block")))
      (render-js [renderer] ""))))
