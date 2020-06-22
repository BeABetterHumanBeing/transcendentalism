(ns transcendentalism.access
  (:require (cemerick.friend [credentials :as creds])
            [transcendentalism.flags :refer :all]
            [transcendentalism.html :refer :all]))

; While the feature is being developed, there is a single test account hard-coded
; within the server.
; TODO - Replace with a system that looks for users within the graph, and
; populates the list with what they find there.
(def sovereigns {"test" {:username "test"
                         :password (creds/hash-bcrypt "test123")
                         :roles #{::sovereign}}})

(def login-page
  (if (flag :enable-sovereigns)
    {:status 200,
     :headers {"Content-Type" "text/html"}
     :body (div {}
             (form {"action" "login",
                    "method" "post"}
               (div {}
                 "Name"
                 (input {"type" "text",
                         "name" "username"}))
               (div {}
                 "Password"
                 (input {"type" "password",
                         "name" "password"}))
               (div {}
                 (input {"type" "submit",
                         "value" "Login"}))))}
     {:status 501, ; Not supported
      :headers {"Content-Type" "text/html"}
      :body (div {"style" "text-align:center;padding-top:100px;"}
              (h1 {"style" "margin:0 auto;"} "501")
              ; TODO - swap out this image for a more appropriate one
              (img {"src" "/void.png"
                    "style" "margin:0 auto;width:200px;height:200px;"})
              (div {"style" "margin:0 auto;"}
                (span {} (str "Sovereign access is not enabled in "))
                (a {"href" "/"} "this universe")))}))

(def unauthorized-handler
  (-> (fn [request]
        {:status 401,
         :headers {"Content-Type" "text/html"},
         :body "Unauthorized page."})))
