(ns transcendentalism.components.inline-item
  (:require [transcendentalism.constraint :refer :all]))

(defn inline-item-component
  [graph]
  (build-component graph :inline-item-type #{:item-type}
    {
      :description "Content that can be inlined",
      :mutually-exclusive #{"/tangent" "/url" "/see_also" "/definition"},
      :preds {
        "/item/inline/text" {
          :description "The text that appears inline",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/item/inline/tangent" {
          :description "The item which clicking on this toggles",
          :range-type :segment-type,
          :unique true,
        },
        "/item/inline/url" {
          :description "The external URL to which the text is linked",
          :range-type :string,
          :unique true,
        },
        "/item/inline/see_also" {
          :description "Another essay which is relevant",
          :range-type :essay-type,
          :unique true,
        },
        "/item/inline/definition" {
          :description "A definition which clicking on this toggles",
          :range-type :segment-type,
          :unique true,
        },
      },
    }))
