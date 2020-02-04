(ns transcendentalism.svg
  (:require [clojure.string :as str]))
; TODO(gierl): generate is only called for some of the XML-related convenience
; functions. Move these into their own file.
(use 'transcendentalism.generate)

; Whether to generate SVGs, or assume that they've already been created. Helps
; optimize the process when changing non-SVG code.
(def generate-svg true)

(defn- svg
  "Returns the XML for an SVG"
  [width height & contents]
  (str/join "\n"
    ["<?xml version=\"1.0\" encoding=\"utf-8\"?>"
     "<!DOCTYPE svg>"
     (attr-aware "svg" {
      "version" "1.1",
      "width" width,
      "height" height,
      "xmlns" "http://www.w3.org/2000/svg",
      "xmlns:xlink" "http://www.w3.org/1999/xlink",
      })
     (apply str contents)
     "</svg>"]))

(defn svg-monad
  "Generates an svg of an artistic styretrix-monad"
  [width height]
  (svg width height "TODO svg contents"))

(defn svg-to-image
  "Writes a given SVG to an image with the given name"
  [image-name width height svg-fn]
  (let [filename (str "resources/" image-name ".svg")]
    (if generate-svg
      (spit filename ((eval svg-fn) width height))
      (println "Skipping generating" filename))
    (str "../" filename)))