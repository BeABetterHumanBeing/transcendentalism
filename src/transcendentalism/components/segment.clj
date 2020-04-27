(ns transcendentalism.components.segment
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]))

(defn segment-component
  [graph]
  (build-component graph :segment-type #{}
    {
      :description "Nodes that are internally linkable",
      :preds {
        "/segment/flow/block" {
          :description "Relation to the next block segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/flow/inline" {
          :description "Relation to the second inline segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/contains" {
          :description "Relation from a segment to the item it contains",
          :range-type :item-type,
          :unique true,
          :required true,
        },
        "/segment/author" {
          :description "Who wrote the segment",
          :range-type :string,
        },
       },
     }
     (reify Renderer
       (get-renderer-name [renderer] "segment")
       (get-priority [renderer] 10)
       (render-html [renderer params graph sub]
         (let [authors (read-os graph sub "/segment/author"),
               new-params (assoc params "no-foot" true),
               contents (str (param-aware-render-sub new-params graph
                               (unique-or-nil graph sub "/segment/contains"))
                             (let [inline (unique-or-nil graph sub "/segment/flow/inline")]
                               (if (nil? inline)
                                   ""
                                   (param-aware-render-sub new-params graph inline))))]
           (div {"class" "segment-block"}
             (if (empty? authors)
                 contents
                 (div {"class" "authors-parent"}
                   (div {"class" "authors"}
                     (div {"class" "authors-chain"} "")
                     (str/join ", " authors))
                   contents))
             (if (params "no-foot")
                 ""
                 (str
                   ; TODO - any definitions come here
                   ; TODO - any footnotes come here
                   ))
             (let [next-block (unique-or-nil graph sub "/segment/flow/block")]
               (if (nil? next-block)
                   ""
                   (param-aware-render-sub graph next-block)))
             )))
       (render-css [renderer]
         (str/join "\n" [
           (css "div" {"class" "authors-parent"}
             (position "relative")
             (border-style "none" "solid" "none" "none")
             (border-width "1px")
             (border-color (to-css-color orange)))
           (css "div" {"class" "authors-chain"}
             (position "relative")
             (width "30px")
             (border-style "dashed")
             (border-color (to-css-color orange))
             (border-width "1px" "0" "0" "0")
             (right "35px")
             (bottom "-10px"))
           (css "div" {"class" "authors"}
             (position "absolute")
             (width "100px")
             (right "-130px")
             (bottom "0px")
             (color (to-css-color orange)))]))
       (render-js [renderer] ""))))
