(ns transcendentalism.server
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.not-modified :refer :all]
            [ring.middleware.params :refer :all]
            [transcendentalism.constraint :refer :all]))

(defn- uri-to-sub
  [uri]
  (if (or (= uri "/index") (= uri "/") (= uri "/index.html"))
      :monad
      (let [v (-> uri
                  (str/replace #"%22" "\"")
                  (str/replace #"%20" " "))]
        (edn/read-string (subs v 1)))))

(defn- render-sub
  [graph sub types]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str sub " /type " types)})

(defn- page-404
  [graph sub]
  (let [types (get-types graph :404)]
    (println "checking 404 page" sub types (= types #{:404}))
    (if (= types #{:404})
        {:status 404
         :headers {"Content-Type" "text/html"}
         :body (str sub " does not exist")}
        (render-sub graph :404 types))))

(defn- base-sub-handler
  [graph]
  (fn
    [request]
    (let [sub (uri-to-sub (:uri request)),
          types (get-types graph sub)]
      (if (or (empty? types) (= types #{:404}))
          (page-404 graph sub)
          (render-sub graph sub types)))))

(defn render-sub-handler
  [graph]
  (-> (base-sub-handler graph)
      (wrap-params)
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))
