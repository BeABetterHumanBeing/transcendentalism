(ns transcendentalism.svg
  (:require [clojure.string :as str]
            [java-time :as jt]
            [clojure.math.numeric-tower :as math]))

(use 'transcendentalism.color
     'transcendentalism.flags
     'transcendentalism.graph
     'transcendentalism.schema
     'transcendentalism.time
     'transcendentalism.xml)

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
  (let [hours-before-present (hours-ago t)]
    (* (max-r dim) (Math/pow k hours-before-present))))

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
        color-values (reduce
          (fn [result subs]
            (let [leads-to-objs (get-objs graph subs "/event/leads_to")]
              (if (empty? (second (first leads-to-objs)))
                ; The first chunk are all 'present', and have no leads_to.
                (reduce (fn
                  [result pair] (assoc result (first pair) (second pair)))
                  result (map vector subs primary-colors))
                (if (and (= (count subs) 1) (= (first subs) :monad))
                  (assoc result :monad white)
                  (reduce (fn
                    [result sub]
                    (assoc result sub
                      (avg-and-whiten (map #(result %) (leads-to-objs sub)))))
                    result subs)))))
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
     (xml-tag "svg" {
      "version" "1.1",
      "viewBox" "0 0 800 800",
      "xmlns" "http://www.w3.org/2000/svg",
      "xmlns:xlink" "http://www.w3.org/1999/xlink",
      }
      (apply str contents))]))

(defn- g [attrs & contents] (xml-tag "g" attrs (str/join "\n" contents)))

(defn- animate [attrs] (xml-tag "animate" attrs ""))

(defn- text [attrs contents] (xml-tag "text" attrs contents))

(defn- line [attrs contents] (xml-tag "line" attrs contents))

(defn- circle [attrs contents] (xml-tag "circle" attrs contents))

(defn- circle-r
  "Determines an aesthetically-pleasing radius for a circle"
  [dim r]
  (let [circle-max (/ dim 40)]
    (if (< r 0.001)
      circle-max
      (* circle-max (/ r (max-r dim))))))

(defn- aesthetic-line
  "Makes a line with a particular aesthetic standard"
  [obj dim r1 tau1 r2 tau2]
  (let [coords-1 (polar-to-cartesian dim dim r1 tau1),
        coords-2 (polar-to-cartesian dim dim r2 tau2),
        radius (/ (+ (circle-r dim r1) (circle-r dim r2)) 2),
        width (/ radius 4)]
    (line {
      "x1" (first coords-1),
      "y1" (second coords-1),
      "x2" (first coords-2),
      "y2" (second coords-2),
      "stroke" "gray",
      "stroke-width" width,
      }
      (str/join "\n" [
        (animate {
          "attributeType" "XML",
          "attributeName" "stroke-width",
          "values" (str width ";" (* width 1.4) ";" width),
          "begin" (str obj ".end+0.02s"),
          "dur" "0.2s",
        })
        (animate {
          "attributeType" "XML",
          "attributeName" "stroke",
          "values" "gray;black;gray",
          "begin" (str obj ".end+0.02s"),
          "dur" "0.2s",
        })]))))

(defn- aesthetic-circle
  "Makes a circle with a particular aesthetic standard"
  [dim attrs sub objs r tau]
  (let [coords (polar-to-cartesian dim dim r tau),
        radius (circle-r dim r)]
    (circle
      (merge attrs {
        "cx" (first coords),
        "cy" (second coords),
        "r" radius,
        "stroke" "black",
        "stroke-width" (/ radius 4),
        })
      (let [base-animation {
          "id" sub,
          "attributeType" "XML",
          "attributeName" "r",
          "values" (str radius ";" (* radius 1.4) ";" radius),
        }]
        (if (= sub :monad)
          ; The Monad merely beats like a heart.
          (animate (merge base-animation {
            "dur" "2s",
            "repeatCount" "indefinite",
          }))
          (if (empty? objs)
            ; Subject nodes pulse irregularly at random.
            (let [period (format "%.1f" (double (/ (+ 20 (rand-int 90)) 10)))]
              (animate (merge base-animation {
                "begin" (str period "s;" sub ".end+" period "s;"),
                "dur" "0.5s",
              })))
            ; Interior nodes cascade from their descendents.
            (animate (merge base-animation {
              "begin" (str/join ";" (map #(str % ".end+0.02s") objs)),
              "dur" "0.2s",
            }))))))))

(defn- subname
  "Returns a keyword name for a subject"
  [prefix & values]
  (keyword (str/join "-" (concat [prefix] values))))

(defn- interior-triples
  "Returns a vector of triples for an interior slice of the graph between two subjects"
  [vertex-map sub]
  (let [succ (fn [n]
               (loop [cnt n
                      value sub]
                 (if (zero? cnt)
                   value
                   (recur (dec cnt) (vertex-map value))))),
        rootname (subname "root" sub (succ 1)),
        inner (fn [lvl n] (subname "inner" lvl (succ (dec n)) (succ n)))]
    [(->Triple (inner 1 1) "/type/event" nil {})
     (->Triple (inner 1 1) "/event/time" (get-hours-ago 2) {})
     (->Triple (inner 1 1) "/event/leads_to" (subname "subject" sub) {})
     (->Triple (inner 1 1) "/event/leads_to" (subname "subject" (succ 1)) {})
     (map
      (fn [n]
        [(->Triple (inner n 1) "/type/event" nil {})
         (->Triple (inner n 1) "/event/time" (get-hours-ago (* 2 n)) {})
         (->Triple (inner n 1) "/event/leads_to" (inner (dec n) 1) {})
         (->Triple (inner n 1) "/event/leads_to" (inner (dec n) 2) {})])
      (range 2 6))
     (->Triple rootname "/type/event" nil {})
     (->Triple rootname "/event/time" (get-hours-ago 12) {})
     (->Triple rootname "/event/leads_to" (inner 5 1) {})
     (->Triple rootname "/event/leads_to" (inner 5 2) {})
     ]))

; Courtesy Michal Marczyk
(defn rotate [n s]
  (lazy-cat (drop n s)
            (take n s)))

(defn- seq-pairs
  "Returns adjacent pairs from a sequence"
  [sequence]
  (let [pairs (map vector sequence (rotate 1 sequence))]
    (reduce
      (fn [result pair]
        (assoc result (first pair) (second pair)))
      {} pairs)))

(defn- monad-graph
  "Returns the graph form of the monad"
  []
  (let [vertex-sequence [3 6 7 2 4 5 1],
        vertex-map (seq-pairs vertex-sequence),
        graph
    (construct-graph (concat (flatten [
      (->Triple :monad "/type/event" nil {})
      (->Triple :monad "/event/time" "past" {})
      (map #(->Triple :monad "/event/leads_to"
             (subname "root" (first %) (second %)) {})
        vertex-map)
      (map #(interior-triples vertex-map %) vertex-sequence)
      (map #(->Triple (subname "subject" %) "/type/event" nil {}) vertex-sequence)
      (map #(->Triple (subname "subject" %) "/event/time" "present" {})
        vertex-sequence)
     ])))]
    (assert (validate-graph-v1 graph))
    graph))

(defn svg-monad
  "Generates an svg of an artistic styretrix-monad"
  [width height]
  ; The monad will only generate a square size.
  {:pre [(= width height)]}
  (let [dim width,
        k 1.2,
        graph (monad-graph),
        monad (monadic-canonicalization graph dim k),
        leads-to-objs (get-objs graph (all-nodes graph) "/event/leads_to")]
    (svg dim dim
      (str/join "\n"
       ["<style>"
        ".dbg-txt { font: 8px sans-serif; }"
        "</style>"])
      (g {}
        (str/join "\n"
          (map
            (fn [triple]
              (let [sub (:sub triple),
                    obj (:obj triple)]
                (if (= sub :monad)
                  ""
                  (aesthetic-line obj dim
                    (r monad sub) (tau monad sub) (r monad obj) (tau monad obj)))))
            (all-triples graph "/event/leads_to")))
        (str/join "\n"
          (map
            (fn [sub]
              (aesthetic-circle dim {
                "fill" (to-css-color (color monad sub)),
              } sub (leads-to-objs sub) (r monad sub) (tau monad sub)))
            (all-nodes graph)))
        (debug
          (str/join "\n"
            (map
              (fn [sub]
                (let [coords (polar-to-cartesian dim dim (r monad sub) (tau monad sub))]
                  (text {
                    "x" (first coords),
                    "y" (second coords),
                    "class" "dbg-txt",
                    } sub)))
              (all-nodes graph))))
        ))))

(defn svg-to-image
  "Writes a given SVG to an image with the given name"
  [image-name width height svg-fn]
  (let [filename (str "resources/" image-name ".svg")]
    (if (flag :generate-svg)
      (spit filename ((eval svg-fn) width height))
      (println "Skipping generating" filename))
    (str "../" filename)))
