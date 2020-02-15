(ns transcendentalism.time
  (:require [java-time :as jt]))

; The time protocol is designed for events between the bounds of the infinite
; past ("past"), and sempeternal present ("present"). Between these bounds, it
; uses java-time instants.
(defprotocol Time
  (at [t] "Returns the given time")
  (before? [t other] "Returns whether this time strictly precedes another time")
  (hours-ago [t] "Returns the number of hours ago that this time occurred"))

(defn to-time
  "Constructs a Time from a value"
  [t-obj]
  (reify Time
    (at [t] t-obj)
    (before? [t other]
      (let [other-obj (at other)]
        (if (= t-obj other-obj)
          false
          (if (or (= t-obj "past") (= other-obj "present"))
            true
            (if (or (= t-obj "present") (= other-obj "past"))
              false
              (jt/before? t-obj other-obj))))))
    (hours-ago [t]
      (if (= t-obj "past")
        -1000000
        (if (= t-obj "present")
          0
          (jt/as (jt/duration (jt/instant) t-obj) :hours))))))

(defn get-hours-ago
  "Constructs a value corresponding to a given number of hours ago"
  [hours]
  (jt/minus (jt/instant) (jt/hours hours)))

(defn is-valid-time
  "Returns whether the given value is a valid time"
  [value]
  (or (= value "present")
      (= value "past")
      (jt/instant? value)))