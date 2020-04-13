(ns transcendentalism.render
  (:require [clojure.string :as str]
            [transcendentalism.color :refer :all]
            [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.time :refer :all]
            [transcendentalism.toolbox :refer :all]))

(defn- render-fn
  [sub]
  (span {} "(" (span {"style" (str "color:" (to-css-color red))} sub) ")"))

(defn- render-bool
  [sub]
  (span {"style" (str "color:" (to-css-color orange))} (if sub "true" "false")))

(defn- render-string
  [sub]
  (span {"style" (str "color:" (to-css-color yellow))} "\"" sub "\""))

(defn- render-number
  [sub]
  (span {"style" (str "color:" (to-css-color light-blue))} sub))

(defn- render-time
  [sub]
  (span {"style" (str "color:" (to-css-color green))} (str sub)))

(defn- render-enum
  [sub]
  (span {"style" (str "color:" (to-css-color purple))} sub))

(defn- render-pred
  [pred]
  (span {"style" (str "color:" (to-css-color gray))} pred))

(defn- render-primitive
  [graph sub]
  (cond
    (fn? sub) (render-fn sub)
    (instance? Boolean sub) (render-bool sub)
    (string? sub) (render-string sub)
    (number? sub) (render-number sub)
    (is-valid-time sub) (render-time sub)
    :else (if (contains? (get-raw-data graph) sub)
              sub
              (render-enum sub))))

(defn- render-default
  [graph sub]
  (div {"id" sub,
        "style" "margin:0 auto;text-align:left;"}
    sub " " (let [v (read-v graph sub)]
              (if (nil? v)
                  ""
                  (render-primitive graph v)))
    (str/join " "
      (reduce
        (fn [result pred]
          (conj result
            (apply div {} (render-pred pred) " "
              (reduce
                (fn [result obj]
                  (conj result (render-primitive graph obj)))
                [] (read-os graph sub pred)))))
        [] (read-ps graph sub)))))

(defprotocol Renderer
  (get-renderer-name [renderer] "Returns the name of the renderer")
  (get-priority [renderer]
    "Returns the priority of this renderer. If no renderer is explicitly called
     for, the highest-priority wins.")
  (render [renderer params graph sub] "Produces HTML rendering the given sub"))

(def fn-renderer
  (reify Renderer
    (get-renderer-name [renderer] "fn")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-fn sub))))

(def bool-renderer
  (reify Renderer
    (get-renderer-name [renderer] "bool")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-bool sub))))

(def string-renderer
  (reify Renderer
    (get-renderer-name [renderer] "string")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-string sub))))

(def number-renderer
  (reify Renderer
    (get-renderer-name [renderer] "number")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-number))))

(def time-renderer
  (reify Renderer
    (get-renderer-name [renderer] "time")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-time sub))))

(def enum-renderer
  (reify Renderer
    (get-renderer-name [renderer] "enum")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-enum sub))))

(def type-renderer
  (reify Renderer
    (get-renderer-name [renderer] "type")
    (get-priority [renderer] 2)
    (render [renderer params graph sub] (render-default graph sub))))

(defn- invalid-renderer
  [name]
  (reify Renderer
    (get-renderer-name [renderer] "invalid")
    (get-priority [renderer] 0)
    (render [renderer params graph sub]
      (str sub " cannot be rendered as " name))))

(def default-renderer
  (reify Renderer
    (get-renderer-name [renderer] "default")
    (get-priority [renderer] 1)
    (render [renderer params graph sub] (render-default graph sub))))

(defn- get-renderers
  [graph type]
  (case type
    :fn #{fn-renderer}
    :bool #{bool-renderer}
    :string #{string-renderer}
    :number #{number-renderer}
    :time #{time-renderer}
    :type #{type-renderer}
    (if (do (println type) (contains? (get-raw-data graph) type))
        (conj (read-os graph type "/renderer") default-renderer)
        #{enum-renderer})))

(defn param-aware-render-sub
  [params graph sub types]
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
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (render renderer params graph sub)}))
