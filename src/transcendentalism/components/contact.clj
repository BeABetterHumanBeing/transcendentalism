(ns transcendentalism.components.contact
  (:require [transcendentalism.constraint :refer :all]))

(defn contact-component
  [graph]
  (build-component graph :contact-type #{:item-type}
    {
      :description "Provides contact information",
      :preds {
        "/item/contact/email" {
          :description "An email address that can be mailed to",
          :range-type :string,
          :unique true,
          :required true,
        },
      },
    }))
