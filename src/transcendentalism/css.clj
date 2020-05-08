(ns transcendentalism.css
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.xml :refer :all]))

; TODO - check out making a dark mode.

(defn css
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
    (str/join "\n" [(str tagname selector " {") (str/join "\n" contents) "}"])))

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

(defn repeating-linear-gradient [& contents]
  (str "repeating-linear-gradient(" (str/join ",\n" contents) ");"))

(defn media
  [condition & contents]
  (str/join "\n"
    [(str "@media (" condition ") {") (str/join "\n" contents) "}"]))

(defn keyframes
  [name & contents]
  (str/join "\n"
    [(str "@keyframes " name " {" (str/join "\n" contents) "}")]))

(defn keyframe-points
  [pts & contents]
  (str/join "\n" [
    (str (str/join ", " pts) " {")
    (str/join "\n" contents)
    "}"]))
