(ns transcendentalism.schema
  (:require [clojure.string :as str]
    [clojure.set :as set]))

(use 'transcendentalism.graph
     'transcendentalism.time)

(def gq-segment-to-item
  "Returns a graph query that expands from /type/segment to all /type/item that
   it directly contains through its flows"
   (q-chain-v1
     (q-kleene
       (q-or-v1 (q-pred-v1 "/segment/flow/inline")
                (q-pred-v1 "/segment/flow/block")))
     (q-pred-v1 "/segment/contains")))

(def gq-item-to-item
  "Returns a graph query that expands from /type/item to other /type/items that
   are nested within them"
  (q-kleene
     (q-chain-v1
       (q-or-v1 (q-pred-v1 "/item/q_and_a/question")
                (q-pred-v1 "/item/q_and_a/answer")
                (q-pred-v1 "/item/bullet_list/header")
                (q-pred-v1 "/item/bullet_list/point"))
       (q-kleene
         ; Assumes questions, answers, and points are single-blocked.
         (q-pred-v1 "/segment/flow/inline"))
       (q-pred-v1 "/segment/contains"))))

; Code validation. The purpose of validation is to check the assumptions that
; are made by code generation.

(defn- events-obey-causality?
  "Validates that events' timestamps are strickly before their leads_to"
  [graph]
  (reduce
    (fn [result triple]
      (let [sub-time (get-time graph (:sub triple)),
            obj-time (get-time graph (:obj triple))]
        (conj result
          (if (before? sub-time obj-time)
            nil
            (str (:sub triple) " leads_to " (:obj triple)
                 ", but doesn't occur before it")))))
    #{}
    (all-triples graph "/event/leads_to")))

(defn- events-occur-in-past?
  "Validates that /event/leads_to goes from past to present"
  [graph]
  (let [relation (get-relation graph "/event/leads_to")]
    (set/union
      ; Check that the sources are in the past.
      (reduce
        (fn [result sub]
          (conj result
            (if (= (at (get-time graph sub)) "past")
              nil
              (str sub " has no events leading to it, but does not occur at 'past'"))))
        #{} (get-sources relation))
      ; Check that the sinks are in the present.
      (reduce
        (fn [result sub]
          (conj result
            (if (= (at (get-time graph sub)) "present")
              nil
              (str sub " leads to no event, but does not occur at 'present'"))))
        #{} (get-sinks relation)))))

(defn- home-is-monad-rooted-dag?
  "Validates that /essay/flow/home results in a monad-rooted DAG"
  [graph]
  (let [relation (get-relation graph "/essay/flow/home"),
        sinks (get-sinks relation)]
    (reduce
      (fn [result sub]
        (conj result
          (if (= sub :monad)
            nil
            (str sub "'s /essay/flow/home does not lead to :monad"))))
      #{} sinks)))

(defn validate-graph-v1
  "Validates that a given graph conforms to a given schema."
  [graph]
  (let
    [validation-errors (reduce
      (fn [result validation-check]
        (set/union result (validation-check graph)))
      #{}
      [events-occur-in-past? events-obey-causality? home-is-monad-rooted-dag?]),
     ; nil ends up in the set, and ought to be weeded out.
     ; TODO - weed out nil. (conj #{} nil) adds nil to the set.
     errors (set/difference validation-errors #{nil})]
    (doall (map println errors))
    (empty? errors)))
