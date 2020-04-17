(ns transcendentalism.components.definition
  (:require [transcendentalism.constraint :refer :all]))

(defn definition-component
  [graph]
  (build-component graph :definition-type #{:item-type}
    {
      :description "Provides a definition for a word",
      :preds {
        "/item/definition/word" {
          :description "The word",
          :range-type :string,
          :unique true,
          :required true,
        },
        "/item/definition/part_of_speech" {
          :description "The part of speech the word belongs to",
          :range-type #{:noun :adjective},
          :unique true,
          :required true,
        },
        "/item/definition/definition" {
          :description "A definition of the word",
          :value-type :string,
          :required true,
          :ordered true,
        },
      },
    }))
