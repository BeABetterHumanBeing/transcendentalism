(ns transcendentalism.components.table
  (:require [transcendentalism.constraint :refer :all]))

(defn table-component
  [graph]
  (build-component graph :table-type #{:item-type}
    {
      :description "A 2D table of items",
      :preds {
        "/item/table/cell" {
          :description "A cell in the table",
          :value-type :segment-type,
          :required true,
          :properties {
            "/column" {
              :description "The column the cell appears in",
              :range-type :number,
              :required true,
            },
            "/row" {
              :description "The row the cell appears in",
              :range-type :number,
              :required true,
            },
          }
        },
        "/item/table/label" {
          :description "A label for a column or row",
          :value-type :segment-type,
          :mutually-exclusive #{"/row" "/column"},
          :properties {
            "/column" {
              :description "The column the label applies to",
              :range-type :number,
            },
            "/row" {
              :description "The row the label applies to",
              :range-type :number,
            },
          },
        },
      },
    }))
