(ns transcendentalism.access
  (:require (cemerick.friend [credentials :as creds])
            [clojure.string :as str]
            [ring.util.anti-forgery :as anti-forgery]
            [ring.util.request :as req]
            [transcendentalism.color :refer :all]
            [transcendentalism.css :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.graph :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.http :refer :all]
            [transcendentalism.tablet :refer :all]
            [transcendentalism.tablet-v2 :refer :all]))

(defn login-page
  [request]
  (if (flag :enable-sovereigns)
      (let [login-failed ((:params request) "login_failed" false)]
        ; TODO - set focus automatically to username field
        (-> (str site-icon
              (link* {"rel" "stylesheet",
                      "href" (if (mobile-browser? request)
                                 "output/mobile_server_styles.css"
                                 "output/server_styles.css")})
              (div {"class" "login-box"}
                (h1 {"class" "login-title"} "Sovereign Access")
                (img {"src" "crown.jpeg",
                      "class" "login-img"})
                (form {"action" "login",
                       "method" "post"}
                  (anti-forgery/anti-forgery-field)
                  (let [username-input {"autofocus" "true",
                                        "type" "text",
                                        "name" "username",
                                        "class" "login-input"}]
                    (input (if login-failed
                               (assoc username-input
                                      "value" ((:params request) "username"))
                               (assoc username-input
                                      "placeholder" "Username"))))
                  (let [password-input {"type" "password",
                                        "name" "password",
                                        "placeholder" "Password",
                                        "class" "login-input"}]
                    (input (if login-failed
                               (assoc password-input
                                      "style" (str "border-color: "
                                                   (to-css-color red)))
                               password-input)))
                  (input {"type" "submit",
                          "value" "Login",
                          "class" "login-button"}))))
            (status-page 200)))
      (error-page request 501
        (str (span {} (str "Sovereign access is not enabled in "))
             (a {"href" "/"} "this universe")))))

(def unauthorized-handler
  (-> (fn [request] (status-page "Unauthorized access" 401))))

(defn access-css
  [is-mobile]
  (str/join "\n" [
    (css "div" {"class" "login-box"}
      (text-align "center")
      (padding-top (if is-mobile "200px" "100px"))
      (font-size (if is-mobile "2em" "1em")))
    (css "h1" {"class" "login-title"}
      (margin "0" "auto"))
    (css "img" {"class" "login-img"}
      (margin "0" "auto")
      (width (if is-mobile "400px" "200px"))
      (height (if is-mobile "400px" "200px")))
    (css "input" {"class" "login-input"}
      (margin (if is-mobile "20px" "10px") "auto")
      (padding (if is-mobile "10px" "5px"))
      (display "block")
      (width (if is-mobile "400px" "200px"))
      (font-size (if is-mobile "1.5em" "1em"))
      (border-width (if is-mobile "2px" "1px")))
    (css "input" {"class" "login-button"}
      (margin (if is-mobile "20px" "10px"))
      (font-size (if is-mobile "1.5em" "1em")))]))

; While the feature is being developed, there is a single test account hard-coded
; within the server.
(defn sovereign-data
  []
  (create-tablet-v2
    (write-path (create-graph) :dan {}
      {"/type" :sovereign-type,
       "/sovereign/username" "dan",
       "/sovereign/salted_password" (creds/hash-bcrypt "test123"),
       "/sovereign/fullname" "Daniel Gierl"})))

(def username-to-sub (atom {}))

(defn users-in-graph
  [graph]
  (reduce
    (fn [result sub]
      (if (contains? (read-os graph sub "/type") :sovereign-type)
          (let [username (unique-or-nil graph sub "/sovereign/username")]
            (reset! username-to-sub (assoc @username-to-sub username sub))
            (assoc result
                   username {:username username
                             :password (unique-or-nil graph sub "/sovereign/salted_password")
                             :roles #{::sovereign}}))
          result))
    {} (read-ss graph)))
