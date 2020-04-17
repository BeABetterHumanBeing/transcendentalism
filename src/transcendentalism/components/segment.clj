(ns transcendentalism.components.segment
  (:require [transcendentalism.constraint :refer :all]))

(defn segment-component
  [graph]
  (build-component graph :segment-type #{}
    {
      :description "Nodes that are internally linkable",
      :preds {
        "/segment/flow/block" {
          :description "Relation to the next block segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/flow/inline" {
          :description "Relation to the second inline segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/contains" {
          :description "Relation from a segment to the item it contains",
          :range-type :item-type,
          :unique true,
          :required true,
        },
        "/segment/author" {
          :description "Who wrote the segment",
          :range-type :string,
        },
       },
     }))
