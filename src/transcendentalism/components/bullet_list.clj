(ns transcendentalism.components.bullet-list
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "list")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [header-block-or-nil (unique-or-nil graph sub "/item/bullet_list/header"),
              point-blocks (map #(read-v graph %)
                                (get-ordered-objs graph sub "/item/bullet_list/point")),
              is_ordered (unique-or-nil graph sub "/item/bullet_list/is_ordered")]
          (div {}
            (if (nil? header-block-or-nil)
                ""
                (div {}
                  (param-aware-render-sub params graph header-block-or-nil)))
            (apply
              (if (or (nil? is_ordered) (not is_ordered)) ul ol)
              {"class" "bullet_list"}
              (into [] (map #(li {} (param-aware-render-sub params graph %))
                            point-blocks))))))
      (render-css [renderer]
        (css "" {"class" "bullet_list"}
          (margin-block-start "5px")
          (margin-block-end "5px")))
      (render-js [renderer] ""))))
