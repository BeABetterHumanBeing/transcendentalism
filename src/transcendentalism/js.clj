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

(defn- loadwith
  "Function for replacing a div with the results of a load call"
  []
  (str/join "\n" [
    "function loadWith(elem, url, callback) {"
      "$.get(url,function(d){"
        "elem.replaceWith(d);"
        "callback();"
      "});"
    "}"
  ]))

(defn- maybe-insert-divider
  "Inserts a divider between two segments if they are non-adjacent"
  []
  (str/join "\n" [
    "function maybeInsertDivider(a, b) {"
      "if(!($('#' + a + '-' + b).length)) {"
        "$('<div class=\"ellipsis\"></div>').insertAfter('#' + a + '-footer');"
      "}"
    "}"
  ]))

(defn- center-view-on
  "Moves the window to center the view on the start of a given segment"
  []
  (str/join "\n" [
    ; TODO(gierl): Change URL to new segment, caching old one in history.
    "function centerViewOn(encoded_id) {"
      "$('#' + encoded_id).get(0).scrollIntoView({behavior: 'smooth'});"
    "};"
  ]))

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded"
  []
  (str/join "\n" [
    "function segmentLoadedCallback(origin, encoded_id,homes) {"
      "var top_insertion_pt = encoded_id + '-above';"
      "if (homes.length > 0) {"
        (debug "console.log('inserting ' + homes[0] + ' into ' + top_insertion_pt);")
        "loadWith($('#' + top_insertion_pt), homes[0] + '.html', function() {"
          "maybeInsertDivider(homes[0], encoded_id);"
          "segmentLoadedCallback(origin, homes[0], homes.slice(1, homes.length));"
        "});"
      "} else {"
        "centerViewOn(origin);"
      "}"
    "}"
  ]))

(defn- openSegment
  "Function that is called when an internal link is clicked"
  []
  (str/join "\n" [
    "function openSegment(encoded_from,encoded_to) {"
      (debug "console.log('Opening ' + encoded_to + ' from ' + encoded_from);")
      "if ($('#' + encoded_to).length) {"
        "centerViewOn(encoded_to);"
      "} else {"
        ; TODO(gierl): Clear all segments beneath encoded_from.
        "$('<div id=\"insertion-pt\"></div>').insertAfter($('#' + encoded_from + '-footer'));"
        "loadWith($('#insertion-pt'), encoded_to + '.html', function() {"
          "$('#' + encoded_to + '-above').remove();"
          "$('#' + encoded_from + '-buffer').remove();"
          "centerViewOn(encoded_to);"
        "});"
      "}"
    "}"
  ]))

(defn script
  "Return the JavaScript for the website"
  []
  (str/join "\n" [
    (loadwith)
    (maybe-insert-divider)
    (center-view-on)
    (segment-loaded-callback)
    (openSegment)
  ]))

(defn call-js
  "Returns a string for calling a JS function from an HTML embedding"
  [fn-name & args]
  (str fn-name "(" (str/join "," args) ")"))
