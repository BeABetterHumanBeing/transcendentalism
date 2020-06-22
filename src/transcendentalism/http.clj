(ns transcendentalism.http
  (:require [transcendentalism.html :refer :all]))

(defn status-page
  [content status]
  {:status status,
   :headers {"Content-Type" "text/html"},
   :body content})

(defn error-page-img
  [status]
  (case status
    404 "/void.png"
    ; TODO - add an image for 501, not implemented
    "/void.png"))

(def site-icon
  (link {"rel" "icon",
         "type" "image/png",
         "href" "/monad_icon_small.png"}))

(defn error-page
  [status message]
  (-> (str
        site-icon
        (div {"style" "text-align:center;padding-top:100px;"}
          (h1 {"style" "margin:0 auto;"} status)
          (img {"src" (error-page-img status)
                "style" "margin:0 auto;width:200px;height:200px;"})
          (div {"style" "margin:0 auto;"} message)))
      (status-page status)))
