(ns transcendentalism.server
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [ring.adapter.jetty :as ring]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.not-modified :refer :all]
            [ring.middleware.params :refer :all]
            [transcendentalism.constraint :refer :all]
            [transcendentalism.flags :refer :all]
            [transcendentalism.html :refer :all]
            [transcendentalism.render :refer :all]
            [transcendentalism.xml :refer :all]))

; Courtesy Assen Kolov
(defn- mobile-browser? [req]
  (if-let [agent (get-in req [:headers "user-agent"])]
    (let [agent (.toLowerCase agent)]
      (or
        (re-matches
          (java.util.regex.Pattern/compile
            (str "(?i).*((android|bb\\\\d+|meego)"
                 ".+mobile|avantgo|bada\\\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)"
                 "|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)"
                 "\\\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*"))
           agent)
         (re-matches
           (java.util.regex.Pattern/compile
             (str "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\\\-)|ai(ko|rn)"
                  "|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\\\-m"
                  "|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\\\-(n|u)|c55\\\\/|capi|ccwa|"
                  "cdm\\\\-|cell|chtm|cldc|cmd\\\\-|co(mp|nd)|craw|da(it|ll|ng)|"
                  "dbte|dc\\\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\\\-d)|el(49|ai)|em(l2|ul)|"
                  "er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\\\-|_)|g1 u|g560|gene|"
                  "gf\\\\-5|g\\\\-mo|go(\\\\.w|od)|gr(ad|un)|haie|hcit|hd\\\\-(m|p|t)|hei\\\\-|"
                  "hi(pt|ta)|hp( i|ip)|hs\\\\-c|ht(c(\\\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\\\-(20|go|ma)|i230|iac( |\\\\-|\\\\/)|ibro|"
                  "idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\\\/)|klon|kpt |"
                  "kwc\\\\-|kyo(c|k)|le(no|xi)|lg( g|\\\\/(k|l|u)|50|54|\\\\-[a-w])|libw|lynx|m1\\\\-w|m3ga|m50\\\\/|ma(te|ui|xo)"
                  "|mc(01|21|ca)|m\\\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|"
                  "n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|"
                  "owg1|p800|pan(a|d|t)|pdxg|pg(13|\\\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\\\-2|po(ck|rt|se)|prox|psio|"
                  "pt\\\\-g|qa\\\\-a|qc(07|12|21|32|60|\\\\-[2-7]|i\\\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|"
                  "s55\\\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\\\-|oo|p\\\\-)|sdk\\\\/|se(c(\\\\-|0|1)"
                  "|47|mc|nd|ri)|sgh\\\\-|shar|sie(\\\\-|m)|sk\\\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|"
                  "h\\\\-|v\\\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\\\-|tdg\\\\-|"
                  "tel(i|m)|tim\\\\-|t\\\\-mo|to(pl|sh)|ts(70|m\\\\-|m3|m5)|tx\\\\-9|up(\\\\.b|g1|si)|utst|v400"
                  "|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)"
                  "|w3c(\\\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\\\-|your|zeto|zte\\\\-"))
           (subs agent 0 4))))))

(defn- uri-to-sub
  [uri]
  (if (contains? #{"/" "/index" "/index.html"} uri)
      :monad
      (let [v (-> uri
                  (str/replace #"%22" "\"")
                  (str/replace #"%20" " "))]
        (try (edn/read-string (subs v 1))
             (catch Exception e :monad)))))

(defn- page-404
  [graph sub]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (div {"style" "text-align:center;padding-top:100px;"}
           (h1 {"style" "margin:0 auto;"} "404")
           (img {"src" "void.png"
                 "style" "margin:0 auto;width:200px;height:200px;"})
           (div {"style" "margin:0 auto;"}
             (span {} (str "\"" sub "\" was not found in "))
             (a {"href" "/"} "this universe")))})

(defn- page-200
  [content]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body content})

(defn- base-sub-handler
  [graph]
  (fn
    [request]
    (let [sub (uri-to-sub (:uri request)),
          types (get-types graph sub)]
      (if (empty? types)
          (page-404 graph sub)
          (page-200
            (let [html (render-sub (:params request) graph sub types)]
              (if ((:params request) "html-only" false)
                  html
                  (str
                    (xml-open "link" {"rel" "stylesheet",
                                      "href" (if (mobile-browser? request)
                                                 "output/mobile_styles.css"
                                                 "output/styles.css")})
                    ; Include JQuery from Google CDN.
                    (xml-tag "script" {"src" "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"} "")
                    (xml-tag "script" {"src" "output/script.js"} "")
                    (xml-open "link" {"rel" "icon",
                                      "type" "image/png",
                                      "href" "monad_icon_small.png"})
                    html))))))))

(defn- render-sub-handler
  [graph]
  (-> (base-sub-handler graph)
      (wrap-params)
      (wrap-file "resources")
      (wrap-content-type)
      (wrap-not-modified)))

(defn launch-server
  [graph]
  (ring/run-jetty (render-sub-handler graph) {:port (flag :server)}))
