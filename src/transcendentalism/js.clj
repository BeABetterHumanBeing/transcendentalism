(ns transcendentalism.js
  (:require [clojure.string :as str]))

(use 'transcendentalism.xml)

(defn seg-id
  "Creates a segment-specific id"
  [encoded_id constant]
  (str encoded_id "-" constant))

(defn js-seg-id
  "Same as seg-id, but returns js code"
  ([encoded_id]
    (str "'#' + " encoded_id))
  ([encoded_id constant]
   (str "'#' + " encoded_id " + '-" constant "'")))

(defn- js-stmt [contents] (str contents ";"))

(defn chain [& contents] (str/join "." contents))

(defn js-str [value] (str "'" value "'"))

(defn js-array [values] (str "[" (str/join "," values) "]"))

(defn js-fn
  [name args & contents]
  (str/join "\n"
    [(str "function " name "(" (str/join ", " args) ") {")
     (str/join "\n" (map js-stmt contents))
     "}"]))

(defn js-anon-fn
  "Anonymous functions, typically used for callbacks"
  [args & contents]
  (apply js-fn "" args contents))

(defn c
  "Call that JS function!"
  [fn-name & args]
  (str fn-name "(" (str/join ", " args) ")"))

(defn js-if
  "Expects if-contents and else-contents to be vectors."
  ([condition if-contents]
    (str/join "\n"
     (concat
       [(str "if (" condition ") {")]
       (map js-stmt if-contents)
       ["}"])))
  ([condition if-contents else-contents]
   (str/join "\n"
     (concat
       [(str "if (" condition ") {")]
       (map js-stmt if-contents)
       ["} else {"]
       (map js-stmt else-contents)
       ["}"]))))

(defn js-assign
  "Assigns a JS variable"
  [varname value]
  (str varname " = " value))

(defn jq
 [content]
 (str "$(" content ")"))

(defn log [content] (c "console.log" content))

(defn- loadwith
  "Function for replacing a div with the results of a load call"
  []
  (js-fn "loadWith" ["elem" "url" "callback"]
    ; Courtesy Victor 'Chris' Cabral for original JS
    (c "$.get" "url"
      (js-anon-fn ["d"]
        (c "elem.replaceWith" "d")
        (c "callback")))))

(defn- on-pop-state
  "Handles history changes"
  []
  (js-assign
    "window.onpopstate"
    (js-anon-fn ["event"]
      (c "centerViewOn" "event.state.encoded_id" "event.state.title" "false"))))

(defn- maybe-insert-divider
  "Inserts a divider between two segments if they are non-adjacent"
  []
  (js-fn "maybeInsertDivider" ["a" "b"]
    (js-if (c "!" (chain (jq "'#' + a + '-' + b") "length"))
      [(chain
         (jq (js-str (xml-tag "div" {"class" "ellipsis"} "")))
         (c "insertAfter" (js-seg-id "a")))])))

(defn- center-view-on
  "Moves the window to center the view on the start of a given segment"
  []
  (js-fn "centerViewOn" ["encoded_id" "title" "recordHistory"]
    ; Change URL to new segment, caching old one in history.
    (js-if "recordHistory"
      [(c "window.history.pushState"
         "{'encoded_id':encoded_id, 'title':title}"
         (js-str "") "encoded_id + '.html'")])
    (js-assign "document.title" "title")
    (js-assign "window.history.scrollRestoration" (js-str "manual"))
    ; Scroll to the newly focused segment after a tiny delay for the element to
    ; get fetched.
    (c "setTimeout"
      (str "() => " (chain
        (jq (js-seg-id "encoded_id"))
        "get(0)"
        (c "scrollIntoView" "{behavior: 'smooth', block: 'start'}")))
      "50")))

(defn- segment-loaded-callback
  "Function that is called when a segment's body is loaded"
  []
  (js-fn "segmentLoadedCallback" ["origin" "origin_title" "encoded_id" "homes"]
    (js-if "homes.length > 0"
      [(c "loadWith" (jq (js-seg-id "encoded_id" "above")) "homes[0] + '.html'"
          (js-anon-fn []
            (chain (jq (js-seg-id "homes[0]" "buffer")) (c "remove"))
            (c "maybeInsertDivider" "homes[0]" "encoded_id")
            (c "segmentLoadedCallback"
              "origin" "origin_title" "homes[0]"
              (c "homes.slice" "1" "homes.length"))))]
      [(c "centerViewOn" "origin" "origin_title" "true")])))

(defn- openSegment
  "Function that is called when an internal link is clicked"
  []
  (js-fn "openSegment" ["encoded_from" "encoded_to" "title_to"]
    (debug (log "'Opening ' + encoded_to + ' from ' + encoded_from"))
    (js-if (chain (jq (js-seg-id "encoded_to")) "length")
      [(c "centerViewOn" "encoded_to" "title_to" "true")]
      [(chain (jq (js-seg-id "encoded_from")) (c "nextAll") (c "remove"))
       (chain
         (jq (js-str (xml-tag "div" {"id" "insertion-pt"} "")))
         (c "insertAfter"
           (chain (jq (js-seg-id "encoded_from" "footer"))
             ; Go up until it'll be inserted as a sibling to the current segment.
             (c "parent") (c "parent"))))
       (c "loadWith" (jq "'#insertion-pt'") "encoded_to + '.html'"
         (js-anon-fn []
           (chain (jq (js-seg-id "encoded_to" "above")) (c "remove"))
           (chain (jq (js-seg-id "encoded_from" "buffer")) (c "remove"))
           ; Remove the first three elements that have been inserted:
           ; The link to the stylesheet, jQuery, and the javascript. All are
           ; redundant.
           (chain (jq (js-seg-id "encoded_from")) (c "next") (c "remove"))
           (chain (jq (js-seg-id "encoded_from")) (c "next") (c "remove"))
           (chain (jq (js-seg-id "encoded_from")) (c "next") (c "remove"))
           (c "centerViewOn" "encoded_to" "title_to" "true")))])))

(defn- openRandomSegment
  "Function that is called when 'Random' is selected"
  []
  (js-fn "openRandomSegment" ["encoded_from" "possible_encoded_tos"]
    (js-assign "var idx" (c "Math.floor"
                            "Math.random() * possible_encoded_tos.length"))
    (c "openSegment" "encoded_from" "possible_encoded_tos[idx]" (js-str "Random"))))

(defn- seeAlsoSegment
  "Function that moves to the given segment if it's already open somewhere, or
   moves and highlights the connecting link"
  []
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

(defn- toggleFootnote
  "Function that toggles a footnote's visibility"
  []
  (js-fn "toggleFootnote" ["encoded_id"]
    (chain (jq (js-seg-id "encoded_id")) (c "toggle" "250" "'swing'"))))

(defn- copyToClipboard
  "Function that copies some text to the clipboard"
  []
  (js-fn "copyToClipboard" ["elem_id"]
    (js-assign "var copyText" (chain "document" (c "getElementById" "elem_id")))
    (chain "copyText" (c "select"))
    (chain "copyText" (c "setSelectionRange" 0 99999))
    (chain "document" (c "execCommand" (js-str "copy")))
    (chain "window" (c "alert" (js-str "Copied!")))))

(defn script
  "Return the JavaScript for the website"
  []
  (str/join "\n" [
    (loadwith)
    (on-pop-state)
    (maybe-insert-divider)
    (center-view-on)
    (segment-loaded-callback)
    (openSegment)
    (openRandomSegment)
    (seeAlsoSegment)
    (toggleFootnote)
    (copyToClipboard)
  ]))

(defn call-js
  "Returns a string for calling a JS function from an HTML embedding"
  [fn-name & args]
  (str fn-name "(" (str/join "," args) ")"))
