(ns transcendentalism.js
  (:require [clojure.string :as str]))

; Whether to generate the site without JS, so that essay segments link directly
; to each other as static pages.
(def static-html-mode false)

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded. Takes the encoded ID of the segment as its argument"
  []
  (str/join "\n" [
    "function segmentLoadedCallback(encoded_id) {"
    "console.log(encoded_id + \" has loaded\");"
    "}"
  ]))

(defn script
  "Return the JavaScript for the website"
  []
  (str/join "\n" [
    (segment-loaded-callback)
  ]))

(defn call-js
  "Returns a string for calling a JS function from an HTML embedding"
  [fn-name & args]
  (str fn-name "(" (str/join "," (map #(str "'" % "'") args)) ")"))
