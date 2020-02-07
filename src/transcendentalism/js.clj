(ns transcendentalism.js
  (:require [clojure.string :as str]))

(use 'transcendentalism.xml)

; Whether to generate the site without JS, so that essay segments link directly
; to each other as static pages.
(def static-html-mode false)

(defn seg-id
  "Creates a segment-specific id"
  [encoded_id constant]
  (str encoded_id "-" constant))

(defn- js-seg-id
  "Same as seg-id, but returns js code"
  [encoded_id constant]
  (if (empty? constant)
    (str "'#' + " encoded_id)
    (str "'#' + " encoded_id " + '-" constant "'")))

(defn format-as-string [value] (str "'" value "'"))

(defn format-as-array
  "Formats a list of strings into an array of strings"
  [values]
  (str "[" (str/join "," values) "]"))

(defn- loadwith
  "Function for replacing a div with the results of a load call"
  []
  (str/join "\n" [
    "function loadWith(elem, url, callback) {"
      ; Courtesy Victor 'Chris' Cabral
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
        (str "$('<div class=\"ellipsis\"></div>').insertAfter(" (js-seg-id "a" "footer") ");")
      "}"
    "}"
  ]))

(defn- center-view-on
  "Moves the window to center the view on the start of a given segment"
  []
  (str/join "\n" [
    ; TODO(gierl): Change URL to new segment, caching old one in history.
    "function centerViewOn(encoded_id) {"
      (str "$(" (js-seg-id "encoded_id" "") ").get(0).scrollIntoView({behavior: 'smooth'});")
    "};"
  ]))

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded"
  []
  (str/join "\n" [
    "function segmentLoadedCallback(origin, encoded_id,homes) {"
      "if (homes.length > 0) {"
        (str "loadWith($(" (js-seg-id "encoded_id" "above") "), homes[0] + '.html', function() {")
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
      (str "if ($(" (js-seg-id "encoded_to" "") ").length) {")
        "centerViewOn(encoded_to);"
      "} else {"
        ; TODO(gierl): Clear all segments beneath encoded_from.
        (str "$('<div id=\"insertion-pt\"></div>').insertAfter($(" (js-seg-id "encoded_from" "footer") "));")
        "loadWith($('#insertion-pt'), encoded_to + '.html', function() {"
          (str "$(" (js-seg-id "encoded_to" "above") ").remove();")
          (str "$(" (js-seg-id "encoded_from" "buffer") ").remove();")
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
