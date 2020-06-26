(ns transcendentalism.core
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.sente :as sente]))

; Note that most CSS is being inlined, except that which cannot, which is being
; supplied via the insertion-pt, and specified in render.clj

(defn dot []
  [:div {:class "dot"
         :style {:display "inline-block"
                 :background "black"
                 :border-style "solid"
                 :border-width 3
                 :border-radius "50%"
                 :width 10
                 :height 10}}])

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
      [:div {:style {:position "relative"}}
        [:div {:style {:position "absolute"
                       :left -20}} "X"]
        [dot] ":" @user-sub]
      ; [:div
      ;   [:span @user-sub ": " @user-data]
      ;   [:span "TODO command bar"]
      ;   [:a {:href "logout"} "Logout"]]
        )))

(defn master-component []
  [:div {:style {:margin "100 auto auto 100"
                 ; :padding-top 100
                 }}
    [human-head-component]])

(defn render-page
  []
  (rd/render [master-component] (.getElementById js/document "insertion-pt")))

(if @sente/cxn-established
  (render-page)
  (add-watch sente/cxn-established :cxn-established
    (fn [_ _ _ _] (render-page))))
