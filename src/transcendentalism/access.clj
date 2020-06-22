(ns transcendentalism.access
  (:require (cemerick.friend [credentials :as creds])
            [transcendentalism.flags :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.http :refer :all]))

; While the feature is being developed, there is a single test account hard-coded
; within the server.
; TODO - Replace with a system that looks for users within the graph, and
; populates the list with what they find there.
(def sovereigns {"test" {:username "test"
                         :password (creds/hash-bcrypt "test123")
                         :roles #{::sovereign}}})

(defn login-page
  [request]
  (if (flag :enable-sovereigns)
      (-> (str site-icon
            (div {}
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
                          "value" "Login"})))))
          (status-page 200))
      (error-page request 501
        (str (span {} (str "Sovereign access is not enabled in "))
             (a {"href" "/"} "this universe")))))

(def unauthorized-handler
  (-> (fn [request] (status-page "Unauthorized access" 401))))
