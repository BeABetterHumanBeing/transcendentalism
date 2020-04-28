(ns transcendentalism.components.q-and-a
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

(defn q-and-a-component
  [graph]
  (build-component graph :q-and-a-type #{:item-type}
    {
      :description "A question and answer",
      :preds {
        "/item/q_and_a/question" {
          :description "The question being asked",
          :range-type :segment-type,
          :required true,
          :unique true,
        },
        "/item/q_and_a/answer" {
          :description "The answer being given",
          :range-type :segment-type,
          :required true,
          :unique true,
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "q-and-a")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [q-block (unique-or-nil graph sub "/item/q_and_a/question"),
              a-block (unique-or-nil graph sub "/item/q_and_a/answer")]
          (div {"class" "q_and_a"}
            (div {"class" "q_and_a_header"} "Q:")
            (str "<i>" (param-aware-render-sub params graph q-block) "</i>")
            (div {"class" "q_and_a_header"} "A:")
            (div {} (param-aware-render-sub params graph a-block)))))
      (render-css [renderer]
        (css "div" {"class" "q_and_a"}
          (display "grid")
          (grid-template-columns "30px" "[qa_separator]" "auto")
          (grid-row-gap "8px"))
        (css "div" {"class" "q_and_a_header"}
          (font-weight "bold")))
      (render-js [renderer] ""))))
