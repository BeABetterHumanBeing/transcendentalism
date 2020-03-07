(ns transcendentalism.xml
  (:require [clojure.string :as str]))

(use 'transcendentalism.flags)

(defn debug
  "Passes through content if in debugging-mode, otherwise nullifies it"
  [content]
  (if (flag :debugging) content ""))

(defn xml-open
  "Creates an open tag with attributes"
  [tagname attrs]
  (if (empty? attrs)
    (str "<" tagname ">")
    (let [formatted-attrs (map #(str (first %) "=\"" (second %) "\"") (seq attrs))]
      (str "<" tagname " " (str/join " " formatted-attrs) ">"))))

(defn xml-tag
  "Creates an open-close tag with attributes and content"
  [tagname attrs content]
  (str (xml-open tagname attrs) content "</" tagname ">"))
