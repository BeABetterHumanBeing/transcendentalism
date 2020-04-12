(ns transcendentalism.server
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.not-modified :refer :all]
            [ring.middleware.params :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.html :refer :all]))

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
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (div {"style" "text-align:center;"}
           (h1 {"style" "margin:100px auto 0;"} "404")
           ; TODO - Change out 404 image to something cute
           (img {"src" "crown.jpeg"
                 "style" "margin:0 auto;width:200px;height:200px;"})
           (div {"style" "margin:0 auto;"}
             (span {} (str "\"" sub "\" was not found in "))
             (a {"href" "/"} "this universe")))})

(defn- base-sub-handler
  [graph]
  (fn
    [request]
    (let [sub (uri-to-sub (:uri request)),
          types (get-types graph sub)]
      (if (empty? types)
          (page-404 graph sub)
          (render-sub graph sub types)))))

(defn render-sub-handler
  [graph]
  (-> (base-sub-handler graph)
      (wrap-params)
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))
