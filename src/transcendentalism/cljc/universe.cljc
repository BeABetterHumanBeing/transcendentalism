(ns transcendentalism.cljc.universe)

; A Portal points to a Star. The location gives the namespace, and the name the
; location within that namespace.
(defrecord Portal [location name])

(defn value-type
  "Returns the type keyword of a given primitive value"
  [v]
  (cond
    (number? v) :number
    (string? v) :string
    (boolean? v) :boolean
    (keyword? v) :keyword
    (instance? Portal v) :portal
    :else :unknown))
