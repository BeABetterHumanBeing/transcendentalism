(ns transcendentalism.components.quote
  (:require [clojure.string :as str]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "quote")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [quote-text (unique-or-nil graph sub "/item/quote/text"),
              author-or-nil (unique-or-nil graph sub "/item/quote/author"),
              author (if (nil? author-or-nil) "Anonymous" author-or-nil)]
          (div {"class" "quote"}
            (p {} (str "\"" quote-text "\""))
            (p {"class" "author"} (str "-" author)))))
      (render-css [renderer]
        (str/join "\n" [
          (css "div" {"class" "quote"}
            (padding "0px" "75px")
            (font-size "large")
            (font-family "Times" "serif")
            (font-style "italic"))
          (css "p" {"class" "author"}
            (text-align "right"))]))
      (render-js [renderer] ""))))
