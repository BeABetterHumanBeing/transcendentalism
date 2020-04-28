(ns transcendentalism.components.contact
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.js :refer :all]
            [transcendentalism.render :refer :all]))

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
    }
    (reify Renderer
      (get-renderer-name [renderer] "contact")
      (get-priority [renderer] 10)
      (render-html [renderer params graph sub]
        (let [email-address (unique-or-nil graph sub "/item/contact/email"),
              elem_id (gen-key 8)]
          (div {}
            (div {"class" "contact-centered"}
              (input {"class" "contact",
                      "id" elem_id,
                      "type" "text",
                      "value" email-address,
                      "readonly" "readonly"})
              (div {"class" "contact-dash"})
              (div {"class" "contact-buttons"}
                (a {"href" "javascript:void(0);",
                    "onclick" (call-js "copyToClipboard" (js-str elem_id))}
                   "Copy")
                "/"
                (a {"href" (str "mailto:" email-address),
                    "target" "_top"} "Mail"))))))
      (render-css [renderer]
        (css "div" {"class" "contact-centered"}
          (width "150px")
          (height "20px")
          (margin "0" "auto")
          (position "relative"))
        (css "input" {"class" "contact"}
          (border-style "none")
          (font-size "large")
          (height "20px")
          (width "220px")
          (position "absolute")
          (top "50%")
          (left "50%")
          (margin "-10px" "0" "0" "-110px")
          (text-align "center"))
        (css "div" {"class" "contact-dash"}
          (border-style "dashed" "none" "none" "none")
          (border-color "gray")
          (border-width "1px")
          (width "30px")
          (position "absolute")
          (left "190px")
          (top "10px"))
        (css "div" {"class" "contact-buttons"}
          (position "absolute")
          (left "230px")
          (width "100px")))
      (render-js [renderer]
        (js-fn "copyToClipboard" ["elem_id"]
          (js-assign "var copyText" (chain "document" (c "getElementById" "elem_id")))
          (chain "copyText" (c "select"))
          (chain "copyText" (c "setSelectionRange" 0 99999))
          (chain "document" (c "execCommand" (js-str "copy")))
          (chain "window" (c "alert" (js-str "Copied!"))))))))
