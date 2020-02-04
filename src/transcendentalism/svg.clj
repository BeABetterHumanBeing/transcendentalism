(ns transcendentalism.svg
  (:require [clojure.string :as str]
            [java-time :as jt]))

; TODO(gierl): generate is only called for some of the XML-related convenience
; functions. Move these into their own file.
(use 'transcendentalism.generate)

; Whether to generate SVGs, or assume that they've already been created. Helps
; optimize the process when changing non-SVG code.
(def generate-svg true)

(defn- polar-to-cartesian
  "Transforms polar coordinates into their cartesian equivalents"
  [width height r tau]
  (let [t (- tau 0.25), ; To rotate shape so that 0 is up
        factor (* Math/PI 2)] ; To get to PI-based system
    [(+ (/ width 2) (* r (Math/cos (* t factor))))
     (+ (/ height 2) (* r (Math/sin (* t factor))))]))

(defn- t-to-r
  "Converts a timestamp into a radius"
  [k max-r t]
  (let [seconds-before-present (jt/as (jt/duration (jt/instant) t) :seconds)]
    (* max-r (Math/pow k seconds-before-present))))

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

(defn- g
  [attrs & contents]
  (str/join "\n" (concat [(attr-aware "g" attrs)] contents ["</g>"])))

(defn- circle
  [attrs & contents]
  (str/join "\n" (concat [(attr-aware "circle" attrs)] contents ["</circle>"])))

(defn svg-monad
  "Generates an svg of an artistic styretrix-monad"
  [width height]
  ; The monad will only generate a square size.
  {:pre [(= width height)]}
  (svg width height
    (g {}
      ; The monad circle
      (circle {
        "cx" (/ width 2),
        "cy" (/ height 2),
        "r" 20,
        "fill" "white",
        "stroke" "black",
        "stroke-width" 5,
      })
      ; The seven subject circles
      (str/join "\n"
        (map (fn [color-number]
          (let [coords (polar-to-cartesian width height
                         (* width 0.45) (/ (second color-number) 7))]
            (circle {
              "cx" (first coords),
              "cy" (second coords),
              "r" 20,
              "fill" (first color-number),
              "stroke" "black",
              "stroke-width" 5,
            })))
          (seq (zipmap
            ["#f6bb05" "#f81308" "#3c35ff" "#08caf8" "#c853ff" "#29db01" "#f58705"]
            (range 7)))))
      ; A bunch of evenly-spaced temporal circles
      (str/join "\n"
        (map (fn [timestamp]
          (let [max-r (* width 0.45),
                r (t-to-r 1.2 max-r timestamp),
                coords (polar-to-cartesian width height r 0),
                radius (+ (* 12 (/ r max-r)) 1)]
            (circle {
              "cx" (first coords),
              "cy" (second coords),
              "r" radius,
              "fill" "white",
              "stroke" "black",
              "stroke-width" (/ radius 3),
            })))
        (take 10 (jt/iterate jt/minus (jt/minus (jt/instant) (jt/seconds 1)) (jt/seconds 1)))))
      )))

(defn svg-to-image
  "Writes a given SVG to an image with the given name"
  [image-name width height svg-fn]
  (let [filename (str "resources/" image-name ".svg")]
    (if generate-svg
      (spit filename ((eval svg-fn) width height))
      (println "Skipping generating" filename))
    (str "../" filename)))