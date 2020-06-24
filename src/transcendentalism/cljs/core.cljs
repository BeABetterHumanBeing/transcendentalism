(ns transcendentalism.core
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.sente :as sente]))

(defn human-head-component []
  (let [username (r/atom "placeholder")]
    (sente/chsk-send! [:test/get-username] 5000
      (fn [cb-reply]
        (reset! username cb-reply)))
    (fn []
      [:div
        [:span "username: " @username]
        [:span "TODO command bar"]
        [:a {:href "logout"} "Logout"]])))

(defn attention-set-component []
  [:div "TODO attention set"])

(defn robot-head-component []
  [:div [:span "TODO current graph"]])

(defn master-component []
  [:div {:style {:margin "0 auto"
                 :padding-top 100}}
    [human-head-component]
    [attention-set-component]
    [robot-head-component]])

(defn render-page
  []
  (rd/render [master-component] (.getElementById js/document "insertion-pt")))

(if @sente/cxn-established
  (render-page)
  (add-watch sente/cxn-established :cxn-established
    (fn [_ _ _ _] (render-page))))
