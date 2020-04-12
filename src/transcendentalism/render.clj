(ns transcendentalism.render
  (:require [transcendentalism.graph-v3 :refer :all]
            [transcendentalism.toolbox :refer :all]))

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
    (render [renderer params graph sub]
      (str sub " is a fn"))))

(def bool-renderer
  (reify Renderer
    (get-renderer-name [renderer] "bool")
    (get-priority [renderer] 2)
    (render [renderer params graph sub]
      (str sub " is a bool"))))

(def string-renderer
  (reify Renderer
    (get-renderer-name [renderer] "string")
    (get-priority [renderer] 2)
    (render [renderer params graph sub]
      (str sub " is a string"))))

(def number-renderer
  (reify Renderer
    (get-renderer-name [renderer] "number")
    (get-priority [renderer] 2)
    (render [renderer params graph sub]
      (str sub " is a number"))))

(def time-renderer
  (reify Renderer
    (get-renderer-name [renderer] "time")
    (get-priority [renderer] 2)
    (render [renderer params graph sub]
      (str sub " is a time"))))

(def enum-renderer
  (reify Renderer
    (get-renderer-name [renderer] "enum")
    (get-priority [renderer] 2)
    (render [renderer params graph sub]
      (str sub " is an enum"))))

(defn invalid-renderer
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
    (render [renderer params graph sub]
      (str sub " exists"))))

(defn get-renderers
  [graph type]
  (case type
    :fn #{fn-renderer}
    :bool #{bool-renderer}
    :string #{string-renderer}
    :number #{number-renderer}
    :time #{time-renderer}
    (let [renderers (read-os graph type "/renderer")]
           (if (empty? renderers)
               #{enum-renderer}
               renderers))))

(defn render-sub
  [params graph sub types]
  (let [renderers (reduce-all result {"default" default-renderer}
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
