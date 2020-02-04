(ns transcendentalism.svg
  (:require [clojure.string :as str]
            [java-time :as jt]))

; TODO(gierl): generate is only called for some of the XML-related convenience
; functions. Move these into their own file.
(use 'transcendentalism.generate
     'transcendentalism.graph
     'transcendentalism.schema)

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

(defn- monad-graph
  "Returns the graph form of the monad"
  []
  (let [graph
    (construct-graph (concat (flatten [
      (types :monad "/event")
      (->Triple :monad "/event/time" "past")
      (->Triple :monad "/event/leads_to" :intermediate-1)
      (->Triple :monad "/event/leads_to" :subject-2)
      (->Triple :monad "/event/leads_to" :subject-3)
      (->Triple :monad "/event/leads_to" :subject-4)
      (->Triple :monad "/event/leads_to" :subject-5)
      (->Triple :monad "/event/leads_to" :subject-6)
      (->Triple :monad "/event/leads_to" :subject-7)
      (types :intermediate-1 "/event")
      (->Triple :intermediate-1 "/event/time" (jt/instant (jt/offset-date-time 2020 02 03 1)))
      (->Triple :intermediate-1 "/event/leads_to" :subject-1)
      (types :subject-1 "/event")
      (->Triple :subject-1 "/event/time" "present")
      (types :subject-2 "/event")
      (->Triple :subject-2 "/event/time" "present")
      (types :subject-3 "/event")
      (->Triple :subject-3 "/event/time" "present")
      (types :subject-4 "/event")
      (->Triple :subject-4 "/event/time" "present")
      (types :subject-5 "/event")
      (->Triple :subject-5 "/event/time" "present")
      (types :subject-6 "/event")
      (->Triple :subject-6 "/event/time" "present")
      (types :subject-7 "/event")
      (->Triple :subject-7 "/event/time" "present")
      ; TODO - Add intermediate nodes.
     ])))]
    (assert (validate-graph schema graph))
    graph))

(defn svg-monad
  "Generates an svg of an artistic styretrix-monad"
  [width height]
  ; The monad will only generate a square size.
  {:pre [(= width height)]}
  (let [graph (monad-graph)]
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
        ))))

(defn svg-to-image
  "Writes a given SVG to an image with the given name"
  [image-name width height svg-fn]
  (let [filename (str "resources/" image-name ".svg")]
    (if generate-svg
      (spit filename ((eval svg-fn) width height))
      (println "Skipping generating" filename))
    (str "../" filename)))