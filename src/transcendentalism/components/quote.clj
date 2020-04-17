(ns transcendentalism.components.quote
  (:require [transcendentalism.constraint :refer :all]))

(defn quote-component
  [graph]
  (build-component graph :quote-type #{:item-type}
    {
      :description "A quote",
      :preds {
        "/item/quote/text" {
          :description "The text contents of the quote",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/quote/author" {
          :description "To whom the quote is attributed",
          :range-type :string,
          :unique true,
        },
      },
    }))
