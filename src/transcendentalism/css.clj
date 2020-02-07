(ns transcendentalism.css
  (:require [clojure.string :as str]))

(use 'transcendentalism.xml)

(defn- css
  [tagname attrs & contents]
  (let [selector
        (if (contains? attrs "class")
          (str "." (attrs "class"))
          (if (contains? attrs "id")
            (str "#" (attrs "id"))
            ""))]
    (str/join "\n" [(str tagname selector) " {" (str/join "\n" contents) "}"])))

(defn- style [name content] (str name ": " content ";"))

(defn- font-family [& contents] (style "font-family" (str/join ", " contents)))

(defn- font-style [contents] (style "font-style" contents))

(defn- font-size [contents] (style "font-size" contents))

(defn- border-style [& contents] (style "border-style" (str/join " " contents)))

(defn- border-width [contents] (style "border-width" contents))

(defn- border-color [contents] (style "border-color" contents))

(defn- height [contents] (style "height" contents))

(defn- padding [& contents] (style "padding" (str/join " " contents)))

(defn- margin [& contents] (style "margin" (str/join " " contents)))

(defn- display [contents] (style "display" contents))

(defn- text-align [contents] (style "text-align" contents))

(defn- grid-template-columns
  [& contents]
  (style "grid-template-columns" (str/join " " contents)))

(defn- media
  [condition & contents]
  (str/join "\n"
    [(str "@media (" condition ") {") (str/join "\n" contents) "}"]))

(defn stylesheet
  []
  (str/join "\n"
    [(debug (css "div" {"class" "debug"}
      (font-family "Monaco" "monospace")
      (font-size "small")
      (border-style "dashed")
      (border-width "1px")
      (border-color "red")))
    (media "min-width: 1000px"
      (css "div" {"class" "segment"}
        (display "grid")
        (grid-template-columns "auto" "800px" "auto")))
    (media "max-width: 1000px"
      (css "div" {"class" "segment"}
        (display "grid")
        (grid-template-columns "100px" "auto" "100px")))
    (css "h1" {"class" "header"}
      (text-align "center"))
    (debug (css "div" {"class" "content"}
      (border-style "dashed")
      (border-width "1px")))
    (css "div" {"class" "quote"}
      (padding "0px" "100px")
      (font-size "large")
      (font-family "Times" "serif")
      (font-style "italic"))
    (css "p" {"class" "author"}
      (text-align "right"))
    (css "div" {"class" "footer"} "")
    (css "div" {"class" "ellipsis"}
      (border-style "none" "dashed" "none" "none")
      (border-width "10px")
      (border-color "black")
      (height "100px")
      (margin "0px" "400px"))]))
