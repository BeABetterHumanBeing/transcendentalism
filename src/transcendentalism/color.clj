(ns transcendentalism.color)

(defrecord Color [red green blue])

(defn to-css-color
  "Converts an RGB color to its hex equivalent"
  [color]
  (str "rgb(" (:red color) "," (:green color) "," (:blue color) ")"))

(defn interpolate
  "Interpolates between two numbers. Returns a when ratio=0, b when =1."
  [a b ratio]
  (+ (* b ratio) (* a (- 1 ratio))))

(defn avg-and-whiten
  "Averages together a collection of colors, and slightly whitens the result."
  [colors]
  (let [num (count colors),
        avg-r (/ (apply + (map :red colors)) num),
        avg-g (/ (apply + (map :green colors)) num),
        avg-b (/ (apply + (map :blue colors)) num),
        ratio 0.15,
        r (int (interpolate avg-r 255 ratio)),
        g (int (interpolate avg-g 255 ratio)),
        b (int (interpolate avg-b 255 ratio))]
    (->Color r g b)))

(def white (->Color 255 255 255))
(def black (->Color 0 0 0))
(def gray (->Color 127 127 127))
(def light-gray (->Color 191 191 191))
(def red (->Color 248 18 7))
(def orange (->Color 244 134 4))
(def yellow (->Color 245 186 5))
(def green (->Color 41 219 0))
(def light-blue (->Color 7 202 248))
(def blue (->Color 60 53 254))
(def purple (->Color 200 83 254))

(def primary-colors [red orange yellow green light-blue blue purple])
