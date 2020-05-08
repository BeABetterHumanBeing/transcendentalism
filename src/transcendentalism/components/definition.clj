(ns transcendentalism.components.definition
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "definition")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [word (unique-or-nil graph sub "/item/definition/word"),
              part-of-speech ({
                :noun "noun",
                :adjective "adj.",
                } (unique-or-nil graph sub "/item/definition/part_of_speech")
                "unk"),
              definitions (get-ordered-objs graph sub "/item/definition/definition")]
          (div {"class" "definition"}
            (span {} (str "<b>Definition</b> " word " ")
            (span {} (str "<i>(" part-of-speech ")</i>:"))
            (apply ul {"class" "bullet_list"}
              (str/join "\n"
                (map #(li {} (read-v graph %)) definitions)))))))
      (render-css [renderer is-mobile]
        (css "div" {"class" "definition"}
          (border-color (to-css-color red))
          (border-width "1px")
          (border-style "solid")
          (padding "10px" "10px" "5px" "10px")))
      (render-js [renderer] ""))))
