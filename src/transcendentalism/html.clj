(ns transcendentalism.html
  (:require [clojure.string :as str]))

(use 'transcendentalism.xml)

(defn html [& contents] (xml-tag "html" {} (apply str contents)))

(defn head [contents] (xml-tag "head" {} contents))

(defn body [attrs & contents] (xml-tag "body" attrs (apply str contents)))

(defn script [attrs & contents] (xml-tag "script" attrs (apply str contents)))

(defn div [attrs & contents] (xml-tag "div" attrs (apply str contents)))

(defn p [attrs contents] (xml-tag "p" attrs contents))

(defn span [attrs & contents] (xml-tag "span" attrs (apply str contents)))

(defn a [attrs contents] (xml-tag "a" attrs contents))

(defn i [& contents] (xml-tag "i" {} (str/join " " contents)))

(defn b [& contents] (xml-tag "b" {} (str/join " " contents)))

(defn ul [attrs & contents] (xml-tag "ul" attrs (apply str contents)))

(defn ol [attrs & contents] (xml-tag "ol" attrs (apply str contents)))

(defn li [attrs contents] (xml-tag "li" attrs contents))

(defn hr [] (str "<hr>"))

(defn h1 [attrs contents] (xml-tag "h1" attrs contents))

(defn h2 [attrs contents] (xml-tag "h2" attrs contents))

(defn img [attrs] (xml-open "img" attrs))

(defn button [attrs contents] (xml-tag "button" attrs contents))

(defn input [attrs] (xml-open "input" attrs))

(defn table [attrs contents] (xml-tag "table" attrs contents))

(defn tr [attrs contents] (xml-tag "tr" attrs contents))

(defn td [attrs contents] (xml-tag "td" attrs contents))
