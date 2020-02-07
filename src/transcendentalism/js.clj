(ns transcendentalism.js
  (:require [clojure.string :as str]))

(use 'transcendentalism.xml)

; Whether to generate the site without JS, so that essay segments link directly
; to each other as static pages.
(def static-html-mode false)

(defn format-as-string [value] (str "'" value "'"))

(defn format-as-array
  "Formats a list of strings into an array of strings"
  [values]
  (str "[" (str/join "," values) "]"))

(defn top-insertion-id
  "Id of the element into which segments are inserted above a given encoded_id"
  [encoded_id]
  (str encoded_id "-above"))

(defn bottom-insertion-id
  "Id of the element into which segments are inserted below a given encoded_id"
  [encoded_id]
  (str encoded_id "-below"))

(defn- loadwith
  "Function for replacing a div with the results of a load call"
  []
  (str/join "\n" [
    "$.fn.loadWith = function(url, callback){"
      "var c=$(this);"
      "$.get(url,function(d){"
        "c.replaceWith(d);"
        "callback();"
      "});"
    "};"
  ]))

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded"
  []
  (str/join "\n" [
    "function segmentLoadedCallback(encoded_id,homes) {"
      "var top_insertion_pt = encoded_id + '-above';"
      "if (homes.length > 0) {"
        (debug "console.log('inserting ' + homes[0] + ' into ' + top_insertion_pt);")
        "$('#' + top_insertion_pt).loadWith(homes[0] + '.html', function() {"
          "segmentLoadedCallback(homes[0], homes.slice(1, homes.length));"
        "});"
      "}"
    "}"
  ]))

(defn script
  "Return the JavaScript for the website"
  []
  (str/join "\n" [
    (loadwith)
    (segment-loaded-callback)
  ]))

(defn call-js
  "Returns a string for calling a JS function from an HTML embedding"
  [fn-name & args]
  (str fn-name "(" (str/join "," args) ")"))
