(ns transcendentalism.core
  (:require [clojure.string :as str]
            [goog.string :as gstring]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.sente :as sente]))

; Note that most CSS is being inlined, except that which cannot, which is being
; supplied via the insertion-pt, and specified in render.clj

(defn dot [name]
  (let [mode (r/atom :closed)]
    (fn []
      [:div {:style {:position "relative"
                     :display "inline-block"}}
        [:div {:class "dot-action"
               :style {:display "inline-block"
                       :background "black"
                       :border-style "solid"
                       :border-width 3
                       :border-radius "50%"
                       :width 10
                       :height 10}
               :on-click #(reset! mode (if (= @mode :closed) :open :closed))}]
        [:span {:style {:position "absolute"
                        :top -2}}
          name]
        (when (= @mode :open)
          [:div {:class "dot-action"
                 :style {:position "absolute"
                         :left -16
                         :top 0
                         :border-style "solid"
                         :border-width 3
                         :width 10
                         :height 10}}
            [:a {:href "logout"
                 :style {:position "absolute"
                         :top -5
                         :text-decoration "none"
                         :color "black"}
                 :title "Logout"}
              (gstring/unescapeEntities "&#215;")]])])))

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
            [:div
              [dot (str sub)]
              ; [:span "TODO command bar"]
              ])))))

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
