(ns transcendentalism.components.poem
  (:require [clojure.string :as str]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "poem")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (div {"class" "poem"}
          (str/join "\n"
            (map #(p {"class" "poem-line"} (read-v graph %))
                 (get-ordered-objs graph sub "/item/poem/line")))))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (css "div" {"class" "poem"}
            (text-align "center"))
          (css "p" {"class" "poem-line"}
            (margin "5px" "0px"))]))
      (render-js [renderer] ""))))
