(ns transcendentalism.generate
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

; TODO - move these functions to a more appropriate location, and remove the
; file.

(defn clear-directory
  [dirname]
  (doseq [file (.listFiles (io/as-file dirname))]
    (io/delete-file file)))

(defn render-footnote-idx
  [ancestry]
  (if (empty? ancestry)
    ""
    (str "[" (str/join "-" ancestry) "]")))
