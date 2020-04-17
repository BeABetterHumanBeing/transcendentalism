(ns transcendentalism.components.q-and-a
  (:require [transcendentalism.constraint :refer :all]))

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
    }))
