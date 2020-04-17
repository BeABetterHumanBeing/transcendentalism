(ns transcendentalism.components.event
  (:require [transcendentalism.constraint :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.time :refer :all]))

(defn event-component
  [graph]
  (build-component graph :event-type #{}
    {
      :description "An event",
      :constraints [
        ; Check that /event/leads_to goes strictly forward in time.
        (reify ConstraintV3
          (check-constraint [constraint graph sub]
            (let [t (unique-or-nil graph sub "/event/time"),
                  sub-ts (map #(unique-or-nil graph % "/event/time")
                              (read-os graph sub "/event/leads_to"))]
              [(reduce
                 (fn [result sub-t]
                   (if (before? (to-time t) (to-time sub-t))
                       result
                       (conj result
                             (str t " /event/leads_to an earlier time " sub-t))))
                 #{} sub-ts)
               graph])))
      ],
      :preds {
        "/event/leads_to" {
          :description "Relation from one event to its subsequent impacts",
          :range-type :event-type,
        },
        "/event/time" {
          :description "When an event happened",
          :range-type :time,
          :required true,
          :unique true,
        },
      },
    }))
