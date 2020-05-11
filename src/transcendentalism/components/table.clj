(ns transcendentalism.components.table
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

(defn- unique-or
  [graph sub pred default]
  (let [val (unique-or-nil graph sub pred)]
    (if (nil? val)
        default
        val)))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "table")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [labels-n-cells
                (read-os graph sub #"/item/table/label|/item/table/cell")
              row-max (apply max (map #(unique-or graph % "/row" -1) labels-n-cells)),
              col-max (apply max (map #(unique-or graph % "/column" -1) labels-n-cells)),
              triples-by-row (group-by #(unique-or graph % "/row" -1) labels-n-cells)]
          (table {"class" "t"}
            (str/join "\n"
              (map (fn [row]
                     (let [triples-by-column
                              (group-by #(unique-or graph % "/column" -1)
                                        (triples-by-row row []))]
                       (tr {}
                         (str/join "\n"
                           (map (fn [col]
                                  (let [val (triples-by-column col nil),
                                        class (if (or (= col -1) (= row -1))
                                                  "label"
                                                  "cell"),
                                        target (read-v graph (first val))]
                                    (td {"class" class}
                                        (if (nil? val)
                                            ""
                                            (render-sub params graph target)))))
                                (range -1 (inc col-max)))))))
                   (range -1 (inc row-max)))))))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (css "table" {"class" "t"}
            (border-collapse "collapse")
            (margin "0px" "auto"))
          (css "td" {"class" "label"}
            (font-style "italic")
            (text-align "center")
            (font-size (if is-mobile "2em" "1em"))
            (padding "5px"))
          (css "td" {"class" "cell"}
            (border-style "solid")
            (border-width "1px")
            (border-color (to-css-color light-gray))
            (font-size (if is-mobile "2em" "1em"))
            (padding "5px"))]))
      (render-js [renderer] ""))))
