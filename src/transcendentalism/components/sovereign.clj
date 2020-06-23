(ns transcendentalism.components.sovereign
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

(defn sovereign-component
  [graph]
  (build-component graph :sovereign-type #{}
    {
      :description "Data belonging to a human being",
      :preds {
        "/sovereign/username" {
          :description "The sovereign's username",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/sovereign/fullname" {
          :description "The sovereign's full name",
          :range-type :string,
          :unique true,
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "sovereign")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (div {"class" "sovereign-box"}
          (img {"src" "crown.jpeg", "class" "sovereign-crown"})
          (if-let [fullname (unique-or-nil graph sub "/sovereign/fullname")]
            (div {"class" "sovereign-fullname"} fullname))
          (div {"class" "sovereign-username"}
            "username: " (unique-or-nil graph sub "/sovereign/username"))))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (css "div" {"class" "sovereign-box"}
            (margin "0" "auto")
            (padding-top (if is-mobile "200px" "100px"))
            (text-align "center"))
          (css "img" {"class" "sovereign-crown"}
            (width (if is-mobile "400px" "200px"))
            (height (if is-mobile "400px" "200px")))
          (css "div" {"class" "sovereign-fullname"}
            (color (to-css-color red))
            (font-size (if is-mobile "3em" "1.5em")))
          (css "div" {"class" "sovereign-username"}
            (font-family "Monaco" "monospace")
            (font-size (if is-mobile "2em" "1em")))]))
      (render-js [renderer] ""))))
