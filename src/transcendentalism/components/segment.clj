(ns transcendentalism.components.segment
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.encoding :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defmacro assoc*
  "Associates a key-value pair in a given dictionary only if the key is not
   already present"
  [dict k v]
  `(if (contains? ~dict ~k)
       ~dict
       (assoc ~dict ~k ~v)))

(defn- inc-meta
  [k]
  (p! (fn [_ _ metadata]
        (assoc metadata k (inc (k metadata 0))))))

(defn- prop-to-meta
  [k pred default]
  (p! (fn [tablet sub metadata]
        (let [os (read-os (get-target-graph tablet) sub pred),
              val (if (empty? os) default (first os))]
          (assoc metadata k val)))))

(defn block-exploration-path
  [final-pred]
  (build-path
    [(p* [(inc-meta :inline) "/segment/flow/inline"])
     "/segment/contains"
     (p* [(inc-meta :in-item)
          #{"/item/q_and_a/question"
            "/item/q_and_a/answer"
            ["/item/table/cell" (prop-to-meta :row "/row" -1)
                                (prop-to-meta :col "/col" -1) "/"]
            "/item/bullet_list/header"
            ; TODO - nested bullet lists are not necessarily returned in the
            ; correct order (I suspect they are clobbering each other)
            ["/item/bullet_list/point" (prop-to-meta :order "/order" 0) "/"]}
          (p* [(inc-meta :in-item-inline) "/segment/flow/inline"])
          "/segment/contains"])
     final-pred]))

(def tangent-path (block-exploration-path "/item/inline/tangent"))
(def definition-path (block-exploration-path "/item/inline/definition"))

(defn- collect-block-results
  "Follows a sequence of inline segments, collecting their tangents"
  [path graph sub]
  (let [result-tablet (follow-path path
                        (create-read-write-tablet {sub {}} {} graph)),
        result (get-results result-tablet (fn [data] data)),
        sorted-result
        (sort (compare-by-priority result
                :inline :in-item :order :row :col :in-item-inline)
              (keys result))]
    (into [] sorted-result)))

; TODO - definition map calculation is not finding any definitions
(defn- calculate-definition-map
  "Returns a sub->{:id :root} map of all definitions under a given segment"
  [graph sub]
  (letfn
    [(inner-definition-map [sub]
       (let [definitions (collect-block-results definition-path graph sub),
             next-block (unique-or-nil graph sub "/segment/flow/block"),
             new-definitions (reduce
               (fn [result definition]
                 (assoc result
                   definition
                   {:id (gen-key 8),
                    :root sub}))
               {} definitions)]
         (apply merge
           new-definitions
           (if (nil? next-block)
             {}
             (inner-definition-map next-block))
           (map #(inner-definition-map %) definitions))))]
    (inner-definition-map sub)))

(defn- calculate-footnote-map
  "Returns a sub->{:ancestry :id :root} map of all footnotes under a given segment"
  [graph sub]
  (letfn
    [(inner-footnote-map [sub ancestry idx]
       (let [tangents (collect-block-results tangent-path graph sub),
             next-block (unique-or-nil graph sub "/segment/flow/block"),
             new-tangents (reduce
               (fn [result i]
                 (assoc result
                   (get tangents i)
                   {:ancestry (conj ancestry (+ i idx)),
                    :id (gen-key 8),
                    :root sub}))
               {} (range (count tangents)))]
         (apply merge
           new-tangents
           (if (nil? next-block)
             {}
             (inner-footnote-map next-block ancestry (+ idx (count tangents))))
           (map #(inner-footnote-map % (:ancestry (new-tangents %)) 1)
                tangents))))]
    (inner-footnote-map sub [] 1)))

(defn- maybe-add-footnote-anchor
  "If the given sub is a footnote, adds the anchor (e.g. [1-2-1])"
  [footnote-map sub]
  (if (contains? footnote-map sub)
      (span {"class" "footnote-anchor"}
        (span {"class" "footnote-chain",
               "style" (str "width:" (+ (* (count (:ancestry (footnote-map sub)))
                                           11)
                                        5) "px")})
        (render-footnote-idx (:ancestry (footnote-map sub))))
      ""))

(defn- maybe-wrap-footnote
  "If the given sub is a footnote, wraps the provided content"
  [footnote-map definition-map sub content]
  (if (contains? footnote-map sub)
    (div {"class" (if (= (count (:ancestry (footnote-map sub)))
                         1)
                      "topmost-footnote"
                      "footnote"),
          "id" (:id (footnote-map sub))}
      content)
    (if (contains? definition-map sub)
      (div {"class" "glossary-definition",
            "id" (:id (definition-map sub))}
        content)
      content)))

(defn segment-component
  [graph]
  (build-component graph :segment-type #{}
    {
      :description "Nodes that are internally linkable",
      :preds {
        "/segment/flow/block" {
          :description "Relation to the next block segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/flow/inline" {
          :description "Relation to the second inline segment",
          :range-type :segment-type,
          :unique true,
        },
        "/segment/contains" {
          :description "Relation from a segment to the item it contains",
          :range-type :item-type,
          :unique true,
          :required true,
        },
        "/segment/author" {
          :description "Who wrote the segment",
          :range-type :string,
        },
       },
     }
     (reify Renderer
       (get-renderer-name [renderer] "segment")
       (get-priority [renderer] 10)
       (render-html [renderer params graph sub]
         (let [authors (read-os graph sub "/segment/author"),
               old-definition-map (params "definition-map" {}),
               new-params (-> params
                 (assoc* "footnote-map" (calculate-footnote-map graph sub))
                 (assoc "definition-map" (merge (calculate-definition-map graph sub)
                                                (params "definition-map" {}))))
               footnote-map (new-params "footnote-map" {}),
               definition-map (new-params "definition-map" {}),
               inline-params (assoc new-params "no-block" true),
               contents (str (render-sub inline-params graph
                               (unique-or-nil graph sub "/segment/contains"))
                             (let [inline (unique-or-nil graph sub "/segment/flow/inline")]
                               (if (nil? inline)
                                   ""
                                   (render-sub inline-params graph inline))))]
           (maybe-wrap-footnote footnote-map old-definition-map sub
             (str/join "\n" [
               (let [anchor (maybe-add-footnote-anchor footnote-map sub),
                     authorized-contents (if (empty? authors)
                                             contents
                                             (div {"class" "authors-parent"}
                                               (div {"class" "authors"}
                                                 (div {"class" "authors-chain"} "")
                                                 (str/join ", " authors))
                                               contents))]
                 (if (params "no-block" false)
                     (str anchor
                          authorized-contents)
                     (div {"class" "block"}
                       anchor
                       authorized-contents
                       (reduce-kv
                         (fn [result k v]
                           (if (= (:root v) sub)
                               (str result (render-sub new-params graph k))
                               result))
                         "" definition-map)
                       (reduce-kv
                         (fn [result k v]
                           (if (= (:root v) sub)
                               (str result (render-sub new-params graph k))
                               result))
                         "" footnote-map))))
               (let [next-block (unique-or-nil graph sub "/segment/flow/block")]
                 (if (nil? next-block)
                     ""
                     (render-sub new-params graph next-block)))
               ]))))
       (render-css [renderer is-mobile]
         (str/join "\n" [
           (css "div" {"class" "block"}
             (padding "10px" "25px"))
           (css "div" {"class" "authors-parent"}
             (position "relative")
             (border-style "none" "solid" "none" "none")
             (border-width "1px")
             (border-color (to-css-color orange)))
           ; TODO - fix author CSS s.t. it's right-justified correctly in
           ; nested blocks.
           (css "div" {"class" "authors-chain"}
             (position "relative")
             (width "30px")
             (border-style "dashed")
             (border-color (to-css-color orange))
             (border-width "1px" "0" "0" "0")
             (right "35px")
             (bottom "-10px"))
           (css "div" {"class" "authors"}
             (position "absolute")
             (width "100px")
             (right "-130px")
             (bottom "0px")
             (color (to-css-color orange)))
           (css "div" {"class" "footnote"}
             (display "none")
             (border-width "1px")
             (border-style "solid")
             (border-color (to-css-color yellow))
             (margin "10px" "-15px" "0px"))
           (css "div" {"class" "topmost-footnote"}
             (display "none")
             (border-width "1px")
             (border-style "solid")
             (border-color (to-css-color yellow))
             (margin "10px" "-15px" "0px")
             (position "relative"))
           (css "span" {"class" "footnote-anchor"}
             (position "absolute")
             (width "100px")
             (left "-110px")
             (direction "rtl")
             (color (to-css-color yellow)))
           (css "span" {"class" "footnote-chain"}
             (border-style "dashed")
             (border-color (to-css-color yellow))
             (border-width "1px" "0px" "0px" "0px")
             (position "absolute")
             (left "98px")
             (top "9px"))
           (css "div" {"class" "glossary-definition"}
             (display "none"))]))
       (render-js [renderer] ""))))
