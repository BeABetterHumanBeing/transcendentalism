(ns transcendentalism.components.essay
  (:require [clojure.string :as str]
            [transcendentalism.css :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.js :refer :all]
            [transcendentalism.render :refer :all]))

(defn essay-component
  [graph]
  (build-component graph :essay-type #{}
    {
      :description "Nodes that are externally link-able",
      :constraints [
        ; Check that /essay/flow/home eventually leads to :monad.
        (reify ConstraintV3
          (check-constraint [constraint graph sub]
            (let [homes (read-path graph sub (p* ["/essay/flow/home" "/"]))]
              (if (or (contains? homes :monad) (contains? homes nil))
                  [#{} graph]
                  [#{(str sub " /essay/flow/home does not lead to :monad")}
                   graph]))))
      ],
      :preds {
        "/essay/title" {
          :description "The text that appears centered at the top of an essay",
          :range-type :string,
          :required true,
          :unique true,
        },
        "/essay/flow/next" {
          :description "Relation to the next essay",
          :range-type :essay-type,
        },
        "/essay/flow/home" {
          :description "Relation to the 'parent' essay",
          :value-type :essay-type,
          :required true,
          :unique true,
          :default :monad-shadow,
          :preds {
            "/label" {
              :description "Metadata about the home",
              :range-type #{
                :none ; Hack to ensure that V1->V3 conversion uses pred-aware path.
                :menu ; If the essay belongs to a menu,
              },
            },
          },
        },
        "/essay/flow/see_also" {
          :description "Internal link to another essay",
          :range-type :essay-type,
        },
        "/essay/flow/menu" {
          :description "Internal link to an essay menu",
          :value-type :essay-type,
          :preds {
            "/title" {
              :description "The title of the menu",
              :range-type :string,
              :required true,
            },
          },
        },
        "/essay/flow/random" {
          :description "Relation to a random essay",
          ; TODO - range ought to be a set of essays
          :range-type nil,
        },
        "/essay/contains" {
          :description "Relation from an essay to the segment it contains",
          :range-type :segment-type,
          :unique true,
          :required true,
        },
        "/essay/label" {
          :description "Symbol label that ascribes a metadata to the essay",
          :range-type #{
            ; Content is not rendered.
            :invisible
            ; Content is under construction.
            :under-construction
            ; Content is religious.
            :religion
            ; Content is political.
            :politics}
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "essay")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (div {"id" sub,
              "class" "essay"}
          (div {} "") ; Empty divs occupy first cell in grid.
          (div {}
            (let [title (unique-or-nil graph sub "/essay/title")]
              (h1 {"class" "header"} title))
            (hr)
            (let [content (unique-or-nil graph sub "/essay/contains")]
              (param-aware-render-sub graph content))
            (hr)
            ; TODO - add connections here in the footer
            ; (div {"id" (seg-id id "footer")}
            ;   (let [cxns (sort-by-cxn-type (build-cxns graph encodings sub))]
            ;     (str/join " " (map #(generate-link id %) cxns))))
            (div {"id" (seg-id sub "buffer"),
                  "class" "buffer"}))))
      (render-css [renderer]
        (str/join "\n" [
          (media "min-width: 1000px"
            (css "div" {"class" "essay"}
              (display "grid")
              (grid-template-columns "auto" "800px" "auto")
              (padding "50px" "0" "0" "0")))
          (media "max-width: 1000px"
            (css "div" {"class" "essay"}
              (display "grid")
              (grid-template-columns "100px" "auto" "100px")
              (padding "50px" "0" "0" "0")))
          (css "h1" {"class" "header"}
            (text-align "center"))
          (css "div" {"class" "buffer"}
            (height "600px")
            (background-image "url(\"../resources/crown.jpeg\")")
            (background-position "center")
            (background-repeat "no-repeat")
            (background-size "150px" "150px"))]))
      (render-js [renderer] ""))))
