(ns transcendentalism.svg
  (:require [clojure.string :as str]
            [java-time :as jt]
            [clojure.math.numeric-tower :as math]))

; TODO(gierl): generate is only called for some of the XML-related convenience
; functions. Move these into their own file.
(use 'transcendentalism.generate
     'transcendentalism.graph
     'transcendentalism.schema)

; Whether to generate SVGs, or assume that they've already been created. Helps
; optimize the process when changing non-SVG code.
(def generate-svg true)

; Factor for converting from a TAU-based system to a PI-based one.
(def TAU-2-PI (* Math/PI 2))

(defn- polar-to-cartesian
  "Transforms polar coordinates into their cartesian equivalents"
  [width height r tau]
  (let [t (- tau 0.25)] ; To rotate shape so that 0 is up
    [(+ (/ width 2) (* r (Math/cos (* t TAU-2-PI))))
     (+ (/ height 2) (* r (Math/sin (* t TAU-2-PI))))]))

(defn- max-r
  "Determines the maximum r-coord of a node for a SVG of given dimension"
  [dim]
  (* dim 0.45))

(defn- t-to-r
  "Converts a timestamp into a radius"
  [dim k t]
  (let [days-before-present (days-ago t)]
    (* (max-r dim) (Math/pow k days-before-present))))

(defrecord Color [red green blue])

(defn- to-css-color
  "Converts an RGB color to its hex equivalent"
  [color]
  (str "rgb(" (:red color) "," (:green color) "," (:blue color) ")"))

(defn- interpolate
  "Interpolates between two numbers. Returns a when ratio=0, b when =1."
  [a b ratio]
  (+ (* b ratio) (* a (- 1 ratio))))

(defn- avg-and-whiten
  "Averages together a collection of colors, and slightly whitens the result."
  [colors]
  (let [num (count colors),
        avg-r (/ (apply + (map :red colors)) num),
        avg-g (/ (apply + (map :green colors)) num),
        avg-b (/ (apply + (map :blue colors)) num),
        ratio 0.1,
        r (int (interpolate avg-r 255 ratio)),
        g (int (interpolate avg-g 255 ratio)),
        b (int (interpolate avg-b 255 ratio))]
    (->Color r g b)))

(defn- get-objs
  "Returns the objects of the triples on the given subjects with the given pred"
  [graph subs pred]
  (reduce
    (fn [result sub]
      (assoc result sub
        (map #(:obj %) (all-triples graph sub pred))))
    {} subs))

(defn- polar-distance
  "Calculates the Euclidean distance between two polar coordinates"
  [r1 tau1 r2 tau2]
  (math/sqrt
    (+ (* r1 r1)
       (* r2 r2)
       (* -2 r1 r2 (Math/cos (- (* tau1 TAU-2-PI) (* tau2 TAU-2-PI)))))))

(defprotocol Monad
  (r [monad sub] "Returns the r-value of a given subject")
  (tau [monad sub] "Returns the tau-value of a given subject")
  (color [monad sub] "Returns the color-value of a given subject"))

(defn- monadic-canonicalization
  [graph dim k]
  (let [events (all-nodes graph "/type/event"),
        r-values (reduce
          (fn [result sub]
            (assoc result sub (t-to-r dim k (get-time graph sub))))
          {} events),
        ; Most calculations work from the outside in.
        ordered-events (sort #(> (r-values %1) (r-values %2)) events),
        ordered-chunks (reduce
          (fn [result sub]
            (if (or (empty? result)
                    (not (= (r-values (first (last result))) (r-values sub))))
              (conj result [sub])
              (assoc result (dec (count result)) (conj (last result) sub))))
          [] ordered-events),
        ; TODO(gierl) A subject's tau is the value [0, 1) which doesn't cause it
        ; to overlap with other subjects.
        tau-values (reduce
          (fn [result subs]
            (let [leads-to-objs (get-objs graph subs "/event/leads_to")]
              (if (empty? (second (first leads-to-objs)))
                ; The first chunk are all 'present', and so are evenly spaced.
                (reduce
                  (fn [result pair]
                    (assoc result (first pair) (second pair)))
                  result
                  (map vector subs (map #(/ % (count subs))
                                   (range (count subs)))))
                (reduce
                  (fn [result sub]
                    (let [objs (leads-to-objs sub)]
                      (if (= (count objs) 1)
                        ; If the node only leads to one object, the shortest
                        ; path is for it to have the same tau value.
                        (assoc result sub (result (first objs)))
                        (if (= (count objs) 2)
                          ; If the node leads to two objects, the shortest path
                          ; is the min of the two modular midpoints.
                          (let [obj-1 (first objs),
                                obj-2 (second objs),
                                obj-1-tau (result obj-1),
                                obj-2-tau (result obj-2),
                                calc-dist-from-tau
                                  (fn [tau]
                                    (+ (polar-distance
                                         (r-values obj-1) obj-1-tau
                                         (r-values sub) tau)
                                       (polar-distance
                                         (r-values obj-2) obj-2-tau
                                         (r-values sub) tau))),
                                tau-1 (/ (+ obj-1-tau obj-2-tau) 2),
                                tau-2 (if (< tau-1 0.5) (+ tau-1 0.5) (- tau-1 0.5)),
                                dist-1 (calc-dist-from-tau tau-1),
                                dist-2 (calc-dist-from-tau tau-2)]
                            (assoc result sub (if (< dist-1 dist-2) tau-1 tau-2)))
                          (do
                            ; TODO(gierl): Complete the case where the node leads
                            ; to >2 others. In this case, a binary search should
                            ; be conducted to find the correct value.
                            (println "Skipping advanced tau calculation for" sub)
                            (assoc result sub 0))))))
                  result subs)))
            )
          {} ordered-chunks)
        primary-colors [
          (->Color 248 18 7), (->Color 245 186 5), (->Color 200 83 254),
          (->Color 7 202 248), (->Color 60 53 254), (->Color 41 219 0),
          (->Color 244 134 4)],
        color-values (reduce
          (fn [result subs]
            (let [leads-to-objs (get-objs graph subs "/event/leads_to")]
              (if (empty? (second (first leads-to-objs)))
                ; The first chunk are all 'present', and have no leads_to.
                (reduce (fn
                  [result pair] (assoc result (first pair) (second pair)))
                  result (map vector subs primary-colors))
                (reduce (fn
                  [result sub]
                  (assoc result sub
                    (avg-and-whiten (map #(result %) (leads-to-objs sub)))))
                  result subs))))
          {} ordered-chunks)]
    (reify Monad
      (r [monad sub] (r-values sub))
      (tau [monad sub] (tau-values sub))
      (color [monad sub] (color-values sub)))))

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

(defn- line
  [attrs & contents]
  (str/join "\n" (concat [(attr-aware "line" attrs)] contents ["</line>"])))

(defn- circle
  [attrs & contents]
  (str/join "\n" (concat [(attr-aware "circle" attrs)] contents ["</circle>"])))

(defn- circle-r
  "Determines an aesthetically-pleasing radius for a circle"
  [dim r]
  (let [circle-max (/ dim 40)]
    (if (< r 0.001)
      circle-max
      (* circle-max (/ r (max-r dim))))))

(defn- aesthetic-line
  "Makes a line with a particular aesthetic standard"
  [dim r1 tau1 r2 tau2 & contents]
  (let [coords-1 (polar-to-cartesian dim dim r1 tau1),
        coords-2 (polar-to-cartesian dim dim r2 tau2),
        radius (/ (+ (circle-r dim r1) (circle-r dim r2)) 2)]
    (line {
      "x1" (first coords-1),
      "y1" (second coords-1),
      "x2" (first coords-2),
      "y2" (second coords-2),
      "stroke" "black",
      "stroke-width" (/ radius 4),
      } contents)))

(defn- aesthetic-circle
  "Makes a circle with a particular aesthetic standard"
  [dim attrs r tau & contents]
  (let [coords (polar-to-cartesian dim dim r tau),
        radius (circle-r dim r)]
    (circle
      (merge attrs {
        "cx" (first coords),
        "cy" (second coords),
        "r" radius,
        "stroke" "black",
        "stroke-width" (/ radius 4),
        }) contents)))

(defn- monad-graph
  "Returns the graph form of the monad"
  []
  (let [graph
    (construct-graph (concat (flatten [
      (types :monad "/event")
      (->Triple :monad "/event/time" "past")
      (->Triple :monad "/event/leads_to" :intermediate-10)
      (->Triple :monad "/event/leads_to" :subject-2)
      (->Triple :monad "/event/leads_to" :subject-3)
      (->Triple :monad "/event/leads_to" :subject-4)
      (->Triple :monad "/event/leads_to" :subject-5)
      (->Triple :monad "/event/leads_to" :subject-6)
      (->Triple :monad "/event/leads_to" :subject-7)
      (types :intermediate-1 "/event")
      (->Triple :intermediate-1 "/event/time" (jt/instant (jt/offset-date-time 2020 02 04 1)))
      (->Triple :intermediate-1 "/event/leads_to" :subject-1)
      (->Triple :intermediate-1 "/event/leads_to" :subject-3)
      (types :intermediate-2 "/event")
      (->Triple :intermediate-2 "/event/time" (jt/instant (jt/offset-date-time 2020 02 03 1)))
      (->Triple :intermediate-2 "/event/leads_to" :intermediate-1)
      (types :intermediate-3 "/event")
      (->Triple :intermediate-3 "/event/time" (jt/instant (jt/offset-date-time 2020 02 02 1)))
      (->Triple :intermediate-3 "/event/leads_to" :intermediate-2)
      (types :intermediate-4 "/event")
      (->Triple :intermediate-4 "/event/time" (jt/instant (jt/offset-date-time 2020 02 01 1)))
      (->Triple :intermediate-4 "/event/leads_to" :intermediate-3)
      (types :intermediate-5 "/event")
      (->Triple :intermediate-5 "/event/time" (jt/instant (jt/offset-date-time 2020 01 31 1)))
      (->Triple :intermediate-5 "/event/leads_to" :intermediate-4)
      (types :intermediate-6 "/event")
      (->Triple :intermediate-6 "/event/time" (jt/instant (jt/offset-date-time 2020 01 30 1)))
      (->Triple :intermediate-6 "/event/leads_to" :intermediate-5)
      (types :intermediate-7 "/event")
      (->Triple :intermediate-7 "/event/time" (jt/instant (jt/offset-date-time 2020 01 29 1)))
      (->Triple :intermediate-7 "/event/leads_to" :intermediate-6)
      (types :intermediate-8 "/event")
      (->Triple :intermediate-8 "/event/time" (jt/instant (jt/offset-date-time 2020 01 28 1)))
      (->Triple :intermediate-8 "/event/leads_to" :intermediate-7)
      (types :intermediate-9 "/event")
      (->Triple :intermediate-9 "/event/time" (jt/instant (jt/offset-date-time 2020 01 27 1)))
      (->Triple :intermediate-9 "/event/leads_to" :intermediate-8)
      (types :intermediate-10 "/event")
      (->Triple :intermediate-10 "/event/time" (jt/instant (jt/offset-date-time 2020 01 26 1)))
      (->Triple :intermediate-10 "/event/leads_to" :intermediate-9)
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
  (let [dim width,
        k 1.2,
        graph (monad-graph),
        monad (monadic-canonicalization graph dim k)]
    (svg dim dim
      (g {}
        (str/join "\n"
          (map
            (fn [triple]
              (let [sub (:sub triple),
                    obj (:obj triple)]
                (aesthetic-line dim
                  (r monad sub) (tau monad sub) (r monad obj) (tau monad obj))))
            (all-triples graph "/event/leads_to")))
        (str/join "\n"
          (map
            (fn [sub]
              (aesthetic-circle dim {
                "fill" (to-css-color (color monad sub)),
              } (r monad sub) (tau monad sub)))
            (all-nodes graph)))
        ))))

(defn svg-to-image
  "Writes a given SVG to an image with the given name"
  [image-name width height svg-fn]
  (let [filename (str "resources/" image-name ".svg")]
    (if generate-svg
      (spit filename ((eval svg-fn) width height))
      (println "Skipping generating" filename))
    (str "../" filename)))