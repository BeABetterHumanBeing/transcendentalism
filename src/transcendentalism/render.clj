(ns transcendentalism.render
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.time :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defn- render-fn
  [sub]
  (span {} "(" (span {"class" "fn"} sub) ")"))

(defn- render-bool
  [sub]
  (span {"class" "bool"} (if sub "true" "false")))

(defn- render-string
  [sub]
  (span {"class" "str"} "\"" sub "\""))

(defn- render-number
  [sub]
  (span {"class" "num"} sub))

(defn- render-time
  [sub]
  (span {"class" "time"} (str sub)))

(defn- render-enum
  [sub]
  (span {"class" "enum"} sub))

(defn- render-pred
  [pred]
  (span {"class" "pred"} pred))

(defn- render-sub-link
  [params sub]
  (a {"class" "sub_link",
      "href" (str sub "?"
                  (str/join "&"
                    (concat []
                      (map #(str (first %) "=" (second %))
                           params))))} sub))

(defn render-default
  [params graph sub]
  (letfn [(render-primitive [og-params params graph sub]
            (cond
              (fn? sub) (render-fn sub)
              (instance? Boolean sub) (render-bool sub)
              (string? sub) (render-string sub)
              (number? sub) (render-number sub)
              (is-valid-time sub) (render-time sub)
              :else (if (contains? (get-raw-data graph) sub)
                        (if (= (params "depth" 0) 0)
                            (render-sub-link og-params sub)
                            (inner-render-default og-params params graph sub))
                        (render-enum sub))))
          (inner-render-default [og-params params graph sub]
            (let [depth (params "depth" 1),
                  new-params (assoc params "depth" (dec depth))]
              (div {"id" sub,
                    "class" "sub"}
                sub " " (let [v (read-v graph sub)]
                          (if (nil? v)
                              ""
                              (render-primitive og-params new-params graph v)))
                (str/join " "
                  (reduce
                    (fn [result pred]
                      (conj result
                        (apply div {"class" "pred_tree"}
                          (str (render-pred pred) " ")
                          (str/join (div {}) ; Empty div to fill first grid column.
                            (reduce
                              (fn [result obj]
                                (conj result
                                  (render-primitive og-params new-params graph obj)))
                              [] (read-os graph sub pred))))))
                    [] (read-ps graph sub))))))]
    (inner-render-default params params graph sub)))

(defn render-footnote-idx
  [ancestry]
  (if (empty? ancestry)
    ""
    (str "[" (str/join "-" ancestry) "]")))

(defprotocol Renderer
  (get-renderer-name [renderer] "Returns the name of the renderer")
  (get-priority [renderer]
    "Returns the priority of this renderer. If no renderer is explicitly called
     for, the highest-priority wins.")
  (render-html [renderer params graph sub] "Produces HTML rendering the given sub")
  (render-css [renderer is-mobile] "Produces CSS required by the HTML")
  (render-js [renderer] "Produces JS required by the HTML"))

(def fn-renderer
  (reify Renderer
    (get-renderer-name [renderer] "fn")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-fn sub))
    (render-css [renderer is-mobile]
      (css "span" {"class" "fn"}
        (color (to-css-color red))))
    (render-js [renderer] "")))

(def bool-renderer
  (reify Renderer
    (get-renderer-name [renderer] "bool")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-bool sub))
    (render-css [renderer is-mobile]
      (css "span" {"class" "bool"}
        (color (to-css-color orange))))
    (render-js [renderer] "")))

(def string-renderer
  (reify Renderer
    (get-renderer-name [renderer] "string")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-string sub))
    (render-css [renderer is-mobile]
      (css "span" {"class" "str"}
        (color (to-css-color yellow))))
    (render-js [renderer] "")))

(def number-renderer
  (reify Renderer
    (get-renderer-name [renderer] "number")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-number))
    (render-css [renderer is-mobile]
      (css "span" {"class" "num"}
        (color (to-css-color light-blue))))
    (render-js [renderer] "")))

(def time-renderer
  (reify Renderer
    (get-renderer-name [renderer] "time")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-time sub))
    (render-css [renderer is-mobile]
      (css "span" {"class" "time"}
        (color (to-css-color green))))
    (render-js [renderer] "")))

(def enum-renderer
  (reify Renderer
    (get-renderer-name [renderer] "enum")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-enum sub))
    (render-css [renderer is-mobile]
      (css "span" {"class" "enum"}
        (color (to-css-color purple))))
    (render-js [renderer] "")))

(def type-renderer
  (reify Renderer
    (get-renderer-name [renderer] "type")
    (get-priority [renderer] 2)
    (render-html [renderer params graph sub] (render-default params graph sub))
    (render-css [renderer is-mobile] "")
    (render-js [renderer] "")))

(defn- invalid-renderer
  [name]
  (reify Renderer
    (get-renderer-name [renderer] "invalid")
    (get-priority [renderer] 0)
    (render-html [renderer params graph sub]
      (str sub " cannot be rendered as " name))
    (render-css [renderer is-mobile] "")
    (render-js [renderer] "")))

(def default-renderer
  (reify Renderer
    (get-renderer-name [renderer] "default")
    (get-priority [renderer] 1)
    (render-html [renderer params graph sub] (render-default params graph sub))
    (render-css [renderer is-mobile]
      (str/join "\n" [
        (css "div" {"class" "sub"}
          (margin "0" "auto" "0" "0")
          (text-align "left")
          (white-space "nowrap"))
        (css "div" {"class" "pred_tree"}
          (display "grid")
          (grid-template-columns "min-content" "min-content")
          (grid-column-gap "5px"))
        (css "a" {"class" "sub_link"}
          (color (to-css-color black))
          (text-decoration "none"))
        (css "a" {"class" "sub_link",
                  "selector" "hover"}
          (text-decoration "underline"))
        (css "span" {"class" "pred"}
          (color (to-css-color gray)))]))
    (render-js [renderer] "")))

(defn- get-renderers
  [graph type]
  (case type
    :fn #{fn-renderer}
    :bool #{bool-renderer}
    :string #{string-renderer}
    :number #{number-renderer}
    :time #{time-renderer}
    :type #{type-renderer default-renderer}
    (if (contains? (get-raw-data graph) type)
        (conj (read-os graph type "/renderer") default-renderer)
        #{enum-renderer})))

(defn- sanitize-params
  "Converts params from string values into expected types, type-checking as it
   goes"
  [params]
  (reduce-kv
    (fn [result k v]
      (assoc result k
        (case k
          "depth" (let [parsed-v (edn/read-string v)]
                    (if (and (integer? parsed-v)
                             (> parsed-v 0))
                        parsed-v
                        1))
          v)))
    {} params))

(defn render-sub
  ([graph sub] (render-sub {} graph sub))
  ([params graph sub]
   (render-sub params graph sub (get-types graph sub)))
  ([params graph sub types]
   (let [renderers (reduce-all result {}
                               [[type types]
                                [renderer (get-renderers graph type)]]
                     (assoc result (get-renderer-name renderer) renderer)),
         renderer (if (contains? params "renderer")
                      (let [name (params "renderer")]
                        (if (contains? renderers name)
                            (renderers name)
                            (invalid-renderer name)))
                      (apply max-key #(get-priority %) (vals renderers)))]
     (render-html renderer (sanitize-params params) graph sub))))

(defn get-all-renderers
  [graph]
  (reduce
    (fn [result sub]
      (into result (read-os graph sub "/renderer")))
    [fn-renderer bool-renderer string-renderer number-renderer time-renderer
     enum-renderer type-renderer default-renderer]
    (read-ss graph)))

; TODO - Move all this to the appropriate components, once the new frontend's
; framework is settled enough to be stable.
(defn cljs-css
  [is-mobile]
  (str/join "\n" [
    (css "div" {"class" "dot-action"}
      (border-color (to-css-color white)))
    (css "div" {"class" "dot-action"
                "selector" "hover"}
      (border-color (to-css-color yellow))
      (cursor "pointer"))]))
