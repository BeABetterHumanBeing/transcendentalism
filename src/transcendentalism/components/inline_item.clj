(ns transcendentalism.components.inline-item
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.js :refer :all]
            [transcendentalism.render :refer :all]))

(def see-also-segment
  (js-fn "seeAlsoSegment" ["encoded_from" "encoded_to" "title_to"]
    (js-if (chain (jq (js-seg-id "encoded_to")) "length")
      [(c "centerViewOn" "encoded_to" "title_to" "true")]
      [
        (js-assign "var elem" (jq (str "'#' + encoded_from + '-' + encoded_to")))
        (chain "elem"
               "get(0)"
               (c "scrollIntoView" "{behavior: 'smooth', block: 'nearest'}"))
        (chain "elem" (c "addClass" (js-str "shake")))
        (c "setTimeout"
          (js-anon-fn [] (chain "elem" (c "removeClass" (js-str "shake"))))
          "1500")
      ])))

(def toggle-footnote
  (js-fn "toggleFootnote" ["encoded_id"]
    (chain (jq (js-seg-id "encoded_id")) (c "toggle" "250" "'swing'"))))

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
        "/item/inline/sovereign" {
          :description "A reference to a sovereign entity",
          :range-type :sovereign-type,
          :unique true,
        },
      },
    }
    (reify Renderer
      (get-renderer-name [renderer] "inline")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [text (unique-or-nil graph sub "/item/inline/text"),
              tangent (unique-or-nil graph sub "/item/inline/tangent"),
              see-also (unique-or-nil graph sub "/item/inline/see_also"),
              link (unique-or-nil graph sub "/item/inline/url"),
              definition (unique-or-nil graph sub "/item/inline/definition"),
              sovereign (unique-or-nil graph sub "/item/inline/sovereign"),
              footnote-map (params "footnote-map" {}),
              definition-map (params "definition-map" {})]
          (if (nil? tangent)
            (if (nil? see-also)
              (if (nil? link)
                (if (nil? definition)
                  (if (nil? sovereign)
                      (span {} text)
                      (a {"href" sovereign,
                          "class" "sovereign-cxn"} text))
                  (span {"class" "def-tangent",
                         "onclick" (call-js "toggleFootnote"
                                     (js-str (:id (definition-map definition))))}
                    text))
                (a {"href" link,
                    "target" "_blank"}
                  (str text " &#11016")))
              
              (span {"class" "see-also",
                     "onclick" (call-js "seeAlsoSegment"
                                 (js-str (name (params "essay" :noessay)))
                                 (js-str (name see-also))
                                 (js-str (unique-or-nil graph see-also "/essay/title")))}
                (str text " &#8594")))
            (span {"class" "tangent",
                   "onclick" (call-js "toggleFootnote"
                               (js-str (:id (footnote-map tangent))))}
              (str text " "
                (render-footnote-idx (:ancestry (footnote-map tangent))))))))
      (render-css [renderer is-mobile]
        (str/join "\n" [
          (css "span" {"class" "def-tangent"}
            (color (to-css-color red))
            (cursor "pointer"))
          (css "span" {"class" "tangent"}
            (color (to-css-color yellow))
            (cursor "pointer"))
          (css "span" {"class" "see-also"}
            (color (to-css-color yellow))
            (cursor "pointer"))
          (css "a" {"class" "sovereign-cxn"}
            (color (to-css-color red))
            (text-decoration "none"))
          (css "a" {"class" "sovereign-cxn",
                    "selector" "hover"}
            (text-decoration "underline"))
          (css "span" {"class" "mono"}
            (font-family "Monaco" "monospace")
            (font-size "small")
            (color (to-css-color light-blue)))
          (css "span" {"class" "ex"}
            (color (to-css-color green)))
          (keyframes "shake"
            (keyframe-points ["from" "to"]
              (transform "translate3d(0, 0, 0)"))
            (keyframe-points ["10%", "30%", "50%", "70%", "90%"]
              (transform "translate3d(-10px, 0, 0)"))
            (keyframe-points ["20%", "40%", "60%", "80%"]
              (transform "translate3d(10px, 0, 0)")))
          (css "" {"class" "shake"}
            (animation-name "shake")
            (animation-duration "1.5s"))]))
      (render-js [renderer] 
        (str/join "\n" [toggle-footnote see-also-segment])))))
