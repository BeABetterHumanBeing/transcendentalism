(ns transcendentalism.css
  (:require [clojure.string :as str]))

(use 'transcendentalism.color
     'transcendentalism.xml)

(defn- css
  [tagname attrs & contents]
  (let [selector
        (str (if (contains? attrs "class")
               (str "." (attrs "class"))
               (if (contains? attrs "id")
                 (str "#" (attrs "id"))
                 ""))
             (if (contains? attrs "selector")
               (str ":" (attrs "selector"))
               ""))]
    (str/join "\n" [(str tagname selector) " {" (str/join "\n" contents) "}"])))

(defn- style [name content] (str name ": " content ";"))

; TODO(gierl) Condense the below functions using macros.

(defn- font-family [& contents] (style "font-family" (str/join ", " contents)))

(defn- font-style [contents] (style "font-style" contents))

(defn- font-weight [contents] (style "font-weight" contents))

(defn- font-size [contents] (style "font-size" contents))

(defn- color [contents] (style "color" contents))

(defn- text-decoration [contents] (style "text-decoration" contents))

(defn- border [contents] (style "border" contents))

(defn- border-style [& contents] (style "border-style" (str/join " " contents)))

(defn- border-width [contents] (style "border-width" contents))

(defn- border-color [contents] (style "border-color" contents))

(defn- height [contents] (style "height" contents))

(defn- width [contents] (style "width" contents))

(defn- top [contents] (style "top" contents))

(defn- left [contents] (style "left" contents))

(defn- padding [& contents] (style "padding" (str/join " " contents)))

(defn- margin [& contents] (style "margin" (str/join " " contents)))

(defn- display [contents] (style "display" contents))

(defn- position [contents] (style "position" contents))

(defn- text-align [contents] (style "text-align" contents))

(defn- vertical-align [contents] (style "vertical-align" contents))

(defn- background [contents] (style "background" contents))

(defn- background-color [contents] (style "background-color" contents))

(defn- background-image [contents] (style "background-image" contents))

(defn- background-position [contents] (style "background-position" contents))

(defn- background-repeat [contents] (style "background-repeat" contents))

(defn- background-size [& contents] (style "background-size" (str/join " " contents)))

(defn- transform [contents] (style "transform" contents))

(defn- repeating-linear-gradient [& contents]
  (str "repeating-linear-gradient(" (str/join ",\n" contents) ");"))

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
        (grid-template-columns "auto" "800px" "auto")
        (padding "50px" "0" "0" "0")))
    (media "max-width: 1000px"
      (css "div" {"class" "segment"}
        (display "grid")
        (grid-template-columns "100px" "auto" "100px")
        (padding "50px" "0" "0" "0")))
    (css "h1" {"class" "header"}
      (text-align "center"))
    (css "img" {}
      (width "100%"))
    (debug (css "div" {"class" "content"}
      (border-style "dashed")
      (border-width "1px")))
    (css "div" {"class" "text"}
      (padding "10px" "25px"))
    (css "div" {"class" "footnote"}
      (display "none")
      (border-width "1px")
      (border-style "solid")
      (border-color (to-css-color yellow)))
    (css "span" {"class" "tangent"}
      (color (to-css-color yellow)))
    (css "div" {"class" "emoji"}
      (padding "5px")
      (font-size "100px")
      (text-align "center"))
    (css "div" {"class" "quote"}
      (padding "0px" "100px")
      (font-size "large")
      (font-family "Times" "serif")
      (font-style "italic"))
    (css "p" {"class" "author"}
      (text-align "right"))
    (css "div" {"class" "footer"} "")
    (css "button" {"class" "link_segment"}
      (border "none")
      (color "blue")
      (font-size "medium"))
    (css "button" {"class" "up"}
      (color (to-css-color purple)))
    (css "button" {"class" "down"}
      (color (to-css-color red)))
    (css "button" {"class" "across"}
      (color (to-css-color yellow)))
    (css "button" {"selector" "hover"}
      (text-decoration "underline"))
    (css "div" {"class" "construction-back"}
      (let [yellow (to-css-color yellow),
            black (to-css-color black),
            white (to-css-color white)]
        (background
          (repeating-linear-gradient
            "45deg"
            black (str black " 20px")
            (str white " 20px") (str white " 25px")
            (str yellow " 25px") (str yellow " 45px")
            (str white " 45px") (str white " 50px"))))
      (width "700px")
      (height "300px")
      (margin "50px" "50px")
      (position "relative"))
    (css "div" {"class" "construction-separator"}
      (width "460px")
      (margin "0" "0" "0" "70px")
      (border-style "dashed")
      (border-width "1px")
      (border-color "gray"))
    (css "div" {"class" "construction-front"}
      (background-color (to-css-color white))
      (width "600px")
      (height "170px")
      (position "absolute")
      (top "50%")
      (left "50%")
      (margin "-100px" "0" "0" "-300px")
      (padding "30px" "0" "0" "0")
      (font-family "Arial Black" "Gadget" "sans-serif")
      (font-weight "bold")
      (text-align "center"))
    (css "div" {"class" "ellipsis"}
      (border-style "none" "dashed" "none" "none")
      (border-width "10px")
      (border-color "black")
      (height "150px")
      (margin "80px" "auto" "30px")
      (transform "translate(-50%, 0%)"))
    (css "div" {"class" "buffer"}
      (height "600px")
      (debug (border-style "dashed"))
      ; TODO(gierl): Swap out crown image for the official one.
      (background-image "url(\"../resources/crown_tmp.png\")")
      (background-position "center")
      (background-repeat "no-repeat")
      (background-size "300px" "200px"))]))
