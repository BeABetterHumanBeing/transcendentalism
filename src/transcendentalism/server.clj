(ns transcendentalism.server
  (:require [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [clojure.edn :as edn]
            [clojure.string :as str]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as http-kit]
            [ring.middleware.anti-forgery :as anti-forgery]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.not-modified :refer :all]
            [ring.middleware.params :refer :all]
            [ring.middleware.session :refer :all]
            [ring.util.response :as resp]
            [transcendentalism.access :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.http :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.sente :refer :all]
            [transcendentalism.xml :refer :all]))

(defn- uri-to-sub
  [uri]
  (if (contains? #{"/" "index" "index.html"} uri)
      :monad
      (let [v (-> uri
                  (str/replace #"%22" "\"")
                  (str/replace #"%20" " "))]
        (try (edn/read-string v)
             (catch Exception e :monad)))))

(defn- page-404
  ([request]
   (page-404 request (:uri request "")))
  ([request sub]
   (error-page request 404
     (str (span {} (str "\"" sub "\" was not found in "))
          (a {"href" "/"} "this universe")))))

(defn- page-200
  [content]
  (status-page content 200))

(defn- sovereign-page
  [graph request]
  (-> (str
        site-icon
        (link* {"rel" "stylesheet",
                "href" (if (mobile-browser? request)
                           "output/mobile_server_styles.css"
                           "output/server_styles.css")})
        ; Reagent DOM inserts into the point below. Must appear before the
        ; JS code that uses it.
        (div {"id" "insertion-pt"})
        ; Include CSRF anti-forgery token.
        (let [csrf-token
          (force anti-forgery/*anti-forgery-token*)]
          (div {"id" "sente-csrf-token",
                "data-csrf-token" csrf-token}))
        (script {"src" "output/cljs-main.js"} ""))
      (status-page 200)))

(defn- base-sub-handler
  [graph sub]
  (fn
    [request]
    (let [types (get-types graph sub)]
      (if (empty? types)
          (page-404 request sub)
          (page-200
            (let [html (render-sub (:params request) graph sub types)]
              (if ((:params request) "html-only" false)
                  html
                  (str
                    (link* {"rel" "stylesheet",
                            "href" (if (mobile-browser? request)
                                       "output/mobile_styles.css"
                                       "output/styles.css")})
                    ; Include JQuery from Google CDN.
                    (script {"src" "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"} "")
                    (script {"src" "output/script.js"} "")
                    site-icon
                    html))))))))

(defn- render-sub-handler
  [graph sub]
  (-> (base-sub-handler graph sub)
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))

(def output-handler
  (-> (fn [request] (status-page "Output Placeholder" 200))
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))

(def not-found-handler
  (-> (fn [request] (page-404 request))
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))

(defn app
  [graph]
  (routes
    ; Sovereign URIs
    (GET "/sovereign" request
      (friend/authorize #{:transcendentalism.access/sovereign}
                        (sovereign-page graph request)))
    (GET "/login" request (login-page request))
    (friend/logout (ANY "/logout" request (resp/redirect "/")))
    ; Sente's routing endpoints
    (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
    (POST "/chsk" req (ring-ajax-post                req))
    ; Public URIs
    (GET "/output/:o" [o] output-handler)
    (GET "/:uri" [uri] (render-sub-handler graph (uri-to-sub uri)))
    (GET "/" [] (render-sub-handler graph :monad))
    (route/not-found not-found-handler)))

(defn secured-app
  [graph]
  (let [users (users-in-graph graph)]
    (-> (app graph)
        (friend/authenticate {:credential-fn (partial creds/bcrypt-credential-fn
                                                      users),
                              :workflows [(workflows/interactive-form)],
                              :unauthorized-handler unauthorized-handler,
                              :default-landing-uri "/sovereign"})
        (anti-forgery/wrap-anti-forgery)
        (wrap-session)
        (wrap-keyword-params)
        (wrap-params))))

(defn launch-server
  [graph]
  (start-sente-router! graph)
  (http-kit/run-server (secured-app graph) {:port (flag :server)}))
