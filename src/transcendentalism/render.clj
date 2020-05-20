(ns transcendentalism.render
  (:require [clojure.string :as str]
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
  [sub]
  (a {"class" "sub_link",
      "href" (str sub "?renderer=default")} sub))

(defn- render-primitive
  [graph sub]
  (cond
    (fn? sub) (render-fn sub)
    (instance? Boolean sub) (render-bool sub)
    (string? sub) (render-string sub)
    (number? sub) (render-number sub)
    (is-valid-time sub) (render-time sub)
    :else (if (contains? (get-raw-data graph) sub)
              (render-sub-link sub)
              (render-enum sub))))

(defn render-default
  [graph sub]
  (div {"id" sub,
        "class" "sub"}
    sub " " (let [v (read-v graph sub)]
              (if (nil? v)
                  ""
                  (render-primitive graph v)))
    (str/join " "
      (reduce
        (fn [result pred]
          (conj result
            (apply div {} (render-pred pred) " "
              (str/join ", "
                (reduce
                  (fn [result obj]
                    (conj result (render-primitive graph obj)))
                  [] (read-os graph sub pred))))))
        [] (read-ps graph sub)))))

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
    (render-html [renderer params graph sub] (render-default graph sub))
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
    (render-html [renderer params graph sub] (render-default graph sub))
    (render-css [renderer is-mobile]
      (str/join "\n" [
        (css "div" {"class" "sub"}
          (margin "0" "auto")
          (text-align "left"))
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
                        (println "Selecting renderer" sub renderers name)
                        (if (contains? renderers name)
                            (renderers name)
                            (invalid-renderer name)))
                      (apply max-key #(get-priority %) (vals renderers)))]
     (render-html renderer params graph sub))))

(defn get-all-renderers
  [graph]
  (reduce
    (fn [result sub]
      (into result (read-os graph sub "/renderer")))
    [fn-renderer bool-renderer string-renderer number-renderer time-renderer
     enum-renderer type-renderer default-renderer]
    (read-ss graph)))
