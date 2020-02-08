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

(defn js-str [value] (str "'" value "'"))

(defn js-array [values] (str "[" (str/join "," values) "]"))

(defn- js-fn [name args & contents]
  (str/join "\n"
    [(str "function " name "(" (str/join ", " args) ") {")
     (str/join "\n" contents)
     "}"]))

(defn- js-if
  "Expects if-contents and else-contents to be vectors."
  [condition if-contents else-contents]
  (str/join "\n"
    (concat
      [(str "if (" condition ") {")]
      if-contents
      (if (empty? else-contents)
        ["}"]
        ["} else {"])
      else-contents
      (if (empty? else-contents)
        []
        ["}"]))))

(defn- jq
 [content]
 (str "$(" content ")"))

(defn- loadwith
  "Function for replacing a div with the results of a load call"
  []
  (js-fn "loadWith" ["elem" "url" "callback"]
    ; Courtesy Victor 'Chris' Cabral
    "$.get(url,function(d){"
      "elem.replaceWith(d);"
      "callback();"
    "});"))

(defn- maybe-insert-divider
  "Inserts a divider between two segments if they are non-adjacent"
  []
  (js-fn "maybeInsertDivider" ["a" "b"]
    (js-if (str "!(" (jq "'#' + a + '-' + b") ".length)")
      [(str (jq "'<div class=\"ellipsis\"></div>'") ".insertAfter(" (js-seg-id "a" "footer") ");")]
      [])))

(defn- center-view-on
  "Moves the window to center the view on the start of a given segment"
  []
  (js-fn "centerViewOn" ["encoded_id"]
    ; TODO(gierl): Change URL to new segment, caching old one in history.
    (str (jq (js-seg-id "encoded_id" "")) ".get(0).scrollIntoView({behavior: 'smooth'});")))

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded"
  []
  (js-fn "segmentLoadedCallback" ["origin" "encoded_id" "homes"]
    (js-if "homes.length > 0"
      [(str "loadWith(" (jq (js-seg-id "encoded_id" "above")) ", homes[0] + '.html', function() {")
         "maybeInsertDivider(homes[0], encoded_id);"
         "segmentLoadedCallback(origin, homes[0], homes.slice(1, homes.length));"
       "});"]
      ["centerViewOn(origin);"])))

(defn- openSegment
  "Function that is called when an internal link is clicked"
  []
  (js-fn "openSegment" ["encoded_from" "encoded_to"]
    (debug "console.log('Opening ' + encoded_to + ' from ' + encoded_from);")
    (js-if (str (jq (js-seg-id "encoded_to" "")) ".length")
      ["centerViewOn(encoded_to);"]
      ; TODO(gierl): Clear all segments beneath encoded_from.
      [(str (jq "'<div id=\"insertion-pt\"></div>'") ".insertAfter(" (jq (js-seg-id "encoded_from" "footer")) ");")
       "loadWith(" (jq "'#insertion-pt'") ", encoded_to + '.html', function() {"
         (str (jq (js-seg-id "encoded_to" "above")) ".remove();")
         (str (jq (js-seg-id "encoded_from" "buffer")) ".remove();")
         "centerViewOn(encoded_to);"
       "});"])))

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
