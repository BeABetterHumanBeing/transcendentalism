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

(defn call-js
  "Returns a string for calling a JS function from an HTML embedding"
  [fn-name & args]
  (str fn-name "(" (str/join "," args) ")"))
