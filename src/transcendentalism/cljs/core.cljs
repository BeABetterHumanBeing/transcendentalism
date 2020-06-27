(ns transcendentalism.core
  (:require [clojure.string :as str]
            [goog.string :as gstring]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.sente :as sente]))

; Note that most CSS is being inlined, except that which cannot, which is being
; supplied via the insertion-pt, and specified in render.clj

(defn dot [name]
  (let [target (r/atom {:btn 0
                        :search 0
                        :opacity "0"
                        :visibility "hidden"})
        ]
    (fn []
      (let [t @target,
            anim {:visibility (:visibility t)
                  :opacity (:opacity t)
                  :transition "0.5s"}]
        [:div {:style {:position "relative"}}
          ; The node itself.
          [:div {:class "dot-action"
                 :style {:display "inline-block"
                         :background "black"
                         :border-style "solid"
                         :border-width 3
                         :border-radius "50%"
                         :width 10
                         :height 10}
                 :on-click #(if (= (:btn t) 0)
                             (reset! target {:btn 10
                                             :search 150
                                             :visibility "visible"
                                             :opacity "1.0"})
                             (reset! target {:btn 0
                                             :search 0
                                             :visibility "hidden"
                                             :opacity "0"}))}]
          ; Triangle to expand the node's edges.
          [:div {:class "dot-action"
                 :style (merge anim
                          {:display "inline-block"
                           :border-style "solid"
                           :border-width 3
                           :width (:btn t)
                           :height 10})}
            [:a {:style {:position "absolute"
                         :top 0
                         :text-decoration "none"
                         :color "black"}}
              (gstring/unescapeEntities "&#9657;")]]
          ; Sub name.
          [:div {:style {:display "inline-block"
                         :margin-left -1
                         :transform "translate(0px, -3px)"}}
            name]
          ; Search field.
          [:input {:type "text"
                   :style (merge anim
                            {:margin "2px"
                             :border-style "solid"
                             :border-width 1
                             :width (:search t)
                             :height 22
                             :transform "translate(0px, -3px)"})
                   :placeholder ":example"}]
          ; Button to logout.
          [:div {:class "dot-action"
                 :style (merge anim
                          {:display "inline-block"
                           :border-style "solid"
                           :border-width 3
                           :width (:btn t)
                           :height 10
                           :top -15})}
            [:a {:href "logout"
                 :style {:position "absolute"
                         :top 0
                         :text-decoration "none"
                         :color "black"}
                 :title "Logout"}
              (gstring/unescapeEntities "&#215;")]]]))))

(defn human-head-component []
  (let [user-sub (r/atom nil)
        user-data (r/atom {})]
    (sente/chsk-send! [:sovereign/get-user-sub] 5000
      (fn [sub]
        (when (keyword? sub)
          (reset! user-sub sub)
          (sente/chsk-send! [:data/read-sub {:sub sub}] 5000
            (fn [data]
              (reset! user-data data))))))
    (fn []
      (let [sub @user-sub]
        (if (nil? sub)
            [:div "You are logged out. " [:a {:href "login"} "Login"]]
            [dot (str sub)])))))

(defn master-component []
  [:div {:style {:margin "100 auto auto 100"}}
    [human-head-component]])

(defn render-page
  []
  (rd/render [master-component] (.getElementById js/document "insertion-pt")))

(if @sente/cxn-established
  (render-page)
  (add-watch sente/cxn-established :cxn-established
    (fn [_ _ _ _] (render-page))))
