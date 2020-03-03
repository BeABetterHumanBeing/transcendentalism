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

(defmacro contents-1
  "Creates a CSS style for a name that only accepts a single content arg"
  ([] true)
  ([name]
   `(def ~name
     (fn [contents#]
       (style (str/replace (second (re-find #"\$(.*)@" (str ~name))) #"_" "-")
              contents#))))
  ([name & names]
   `(do
      (def ~name
        (fn [contents#]
          (style (str/replace (second (re-find #"\$(.*)@" (str ~name))) #"_" "-")
                 contents#)))
     (contents-1 ~@names))))

(defmacro contents-N
  "Creates a CSS style for a name that accepts variadic args"
  ([] true)
  ([delimiter name]
   `(def ~name
      (fn [& contents#]
        (style (str/replace (second (re-find #"\$(.*)@" (str ~name))) #"_" "-")
               (str/join ~delimiter contents#)))))
  ([delimiter name & names]
   `(do
      (def ~name
        (fn [& contents#]
          (style (str/replace (second (re-find #"\$(.*)@" (str ~name))) #"_" "-")
                 (str/join ~delimiter contents#))))
      (contents-N ~delimiter ~@names))))

(contents-1
  font-style font-weight font-size color text-decoration direction cursor border
  border-color height width top left right bottom margin-block-start
  margin-block-end display position text-align vertical-align background
  background-color background-image background-position background-repeat
  transform animation-name animation-duration grid-row-gap border-collapse)
(contents-N ", " font-family)
(contents-N " "
  border-style border-width padding margin background-size grid-template-columns)

(defn- repeating-linear-gradient [& contents]
  (str "repeating-linear-gradient(" (str/join ",\n" contents) ");"))

(defn- media
  [condition & contents]
  (str/join "\n"
    [(str "@media (" condition ") {") (str/join "\n" contents) "}"]))

(defn- keyframes
  [name & contents]
  (str/join "\n"
    [(str "@keyframes " name " {" (str/join "\n" contents) "}")]))

(defn- keyframe-points
  [pts & contents]
  (str/join "\n" [
    (str (str/join ", " pts) " {")
    (str/join "\n" contents)
    "}"]))

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
      (margin "0" "auto")
      (display "block"))
    (debug (css "div" {"class" "dbg"}
      (border-style "dashed")
      (border-width "1px")))
    (css "div" {"class" "footnote"}
      (display "none")
      (border-width "1px")
      (border-style "solid")
      (border-color (to-css-color yellow))
      (margin "10px" "-15px" "0px"))
    (css "div" {"class" "topmost-footnote"}
      (display "none")
      (border-width "1px")
      (border-style "solid")
      (border-color (to-css-color yellow))
      (margin "10px" "-15px" "0px")
      (position "relative"))
    (css "div" {"class" "block"}
      (padding "10px" "25px"))
    (css "span" {"class" "tangent"}
      (color (to-css-color yellow))
      (cursor "pointer"))
    (css "span" {"class" "footnote-anchor"}
      (position "absolute")
      (width "100px")
      (left "-110px")
      (direction "rtl")
      (color (to-css-color yellow)))
    (css "span" {"class" "footnote-chain"}
      (border-style "dashed")
      (border-color (to-css-color yellow))
      (border-width "1px" "0px" "0px" "0px")
      (position "absolute")
      (left "98px")
      (top "9px"))
    (css "span" {"class" "see-also"}
      (color (to-css-color yellow))
      (cursor "pointer"))
    (css "span" {"class" "mono"}
      (font-family "Monaco" "monospace")
      (font-size "small")
      (color (to-css-color light-blue)))
    (css "div" {"class" "authors-parent"}
      (position "relative")
      (border-style "none" "solid" "none" "none")
      (border-width "1px")
      (border-color (to-css-color orange)))
    (css "div" {"class" "authors-chain"}
      (position "relative")
      (width "30px")
      (border-style "dashed")
      (border-color (to-css-color orange))
      (border-width "1px" "0" "0" "0")
      (right "35px")
      (bottom "-10px"))
    (css "div" {"class" "authors"}
      (position "absolute")
      (width "100px")
      (right "-130px")
      (bottom "0px")
      (color (to-css-color orange)))
    (keyframes "shake"
      (keyframe-points ["from" "to"]
        (transform "translate3d(0, 0, 0)"))
      (keyframe-points ["10%", "30%", "50%", "70%", "90%"]
        (transform "translate3d(-10px, 0, 0)"))
      (keyframe-points ["20%", "40%", "60%", "80%"]
        (transform "translate3d(10px, 0, 0)")))
    (css "" {"class" "shake"}
      (animation-name "shake")
      (animation-duration "1.5s"))
    (css "div" {"class" "emoji"}
      (font-size "100px")
      (text-align "center"))
    (css "div" {"class" "poem"}
      (text-align "center"))
    (css "p" {"class" "poem-line"}
      (margin "5px" "0px"))
    (css "div" {"class" "quote"}
      (padding "0px" "75px")
      (font-size "large")
      (font-family "Times" "serif")
      (font-style "italic"))
    (css "p" {"class" "author"}
      (text-align "right"))
    (css "span" {"class" "ex"}
      (color (to-css-color green)))
    (css "div" {"class" "q_and_a"}
      (display "grid")
      (grid-template-columns "30px" "[qa_separator]" "auto")
      (grid-row-gap "8px"))
    (css "div" {"class" "q_and_a_header"}
      (font-weight "bold"))
    (css "" {"class" "bullet_list"}
      (margin-block-start "5px")
      (margin-block-end "5px"))
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
      (width "100px"))
    (css "div" {"class" "definition"}
      (border-color (to-css-color red))
      (border-width "1px")
      (border-style "solid")
      (padding "10px" "10px" "5px" "10px"))
    (css "table" {"class" "t"}
      (border-collapse "collapse")
      (margin "0px" "auto"))
    (css "td" {"class" "label"}
      (font-style "italic")
      (text-align "center")
      (padding "5px"))
    (css "td" {"class" "cell"}
      (border-style "solid")
      (border-width "1px")
      (border-color (to-css-color light-gray))
      (padding "5px"))
    (css "button" {"class" "link_segment"}
      (border "none")
      (font-size "medium")
      (margin "3px"))
    (css "button" {"class" "up"}
      (color (to-css-color purple)))
    (css "button" {"class" "down"}
      (color (to-css-color red)))
    (css "button" {"class" "across"}
      (color (to-css-color yellow)))
    (css "button" {"class" "menu"}
      (color "gray"))
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
      (background-image "url(\"../resources/crown.jpeg\")")
      (background-position "center")
      (background-repeat "no-repeat")
      (background-size "150px" "150px"))]))
