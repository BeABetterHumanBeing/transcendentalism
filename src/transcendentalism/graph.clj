(ns transcendentalism.graph
  (:require [java-time :as jt]))

; The graph is composed of triples. Each triple relates a subject to an object
; by means of a predicate.
(defrecord Triple [sub pred obj])

(defn print-triple
  "Pretty-prints a triple"
  [triple]
  (str (:sub triple) "--" (:pred triple) "->" (:obj triple)))

; The time protocol is designed for events between the bounds of the infinite
; past ("past"), and sempeternal present ("present"). Between these bounds, it
; uses java-time instants.
(defprotocol Time
  (at [t] "Returns the given time")
  (before? [t other] "Returns whether this time strictly precedes another time")
  (days-ago [t] "Returns the number of days ago that this time occurred"))

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
              (< t-obj other-obj))))))
    (days-ago [t]
      (if (= t-obj "past")
        -100000
        (if (= t-obj "present")
          0
          (jt/as (jt/duration (jt/instant) t-obj) :days))))))

(defn is-valid-time
  "Returns whether the given value is a valid time"
  [value]
  (or (= value "present")
      (= value "past")
      (jt/instant? value)))

(defn- graph-data-from-triples
  "Constructs a graph by associating triples by subject"
  [triples]
  (reduce
    (fn [graph triple]
      (assoc graph (:sub triple) (conj (graph (:sub triple)) triple)))
    {}
    triples))

; The Graph protocol is the interface through which a graph is accessed.
(defprotocol Graph
  (all-triples [graph] [graph sub] [graph sub pred]
    "Returns a collection of all the triples in the graph [for a given [sub and pred]]")
  (all-nodes [graph] [graph type]
    "Returns a collection of all nodes in the graph [with a given type]")
  (has-type? [graph sub type] "Returns whether the given sub has the given type.")
  (get-time [graph sub]
    "Returns the time associated with the given sub, or nil if it has none"))

(defn construct-graph
  "Constructs a graph from a set of triples"
  [triples]
  (let [graph-data (graph-data-from-triples triples)]
    (reify Graph
      (all-triples [graph]
        (flatten (map second (seq graph-data))))
      ; TODO(gierl): Make this function sub-or-pred matching.
      (all-triples [graph sub]
        (sub graph-data))
      (all-triples [graph sub pred]
        (filter #(= (:pred %) pred) (all-triples graph sub)))
      (all-nodes [graph]
        (keys graph-data))
      (all-nodes [graph type]
        (filter #(has-type? graph % "/type/essay_segment") (all-nodes graph)))
      (has-type? [graph sub type]
        (if (contains? graph-data sub)
          (not (nil? (some #(= (:pred %) type) (sub graph-data))))
          false))
      (get-time [graph sub]
        (let [triples (all-triples graph sub "/event/time")]
          (if (empty? triples)
            nil
            (to-time (:obj (first triples)))))))))

; TODO(gierl): Add chronological orders (i.e. vectors, with tail-append attached
; to timestamp).

; TODO(gierl): Add graups (i.e. sets, with type specialization[s] attached).