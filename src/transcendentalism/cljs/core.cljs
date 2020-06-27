(ns transcendentalism.core
  (:require [clojure.string :as str]
            [goog.string :as gstring]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.sente :as sente]))

; Note that most CSS is being inlined, except that which cannot, which is being
; supplied via the insertion-pt, and specified in render.clj

(defn dot [is-collapsed]
  [:div {:class "node-action"
         :style {:display "inline-block"
                 :background "black"
                 :border-style "solid"
                 :border-width 3
                 :border-radius "50%"
                 :width 10
                 :height 10}
         :on-click #(reset! is-collapsed (not @is-collapsed))}])

(defn node-expander-action [{:keys [show anim] :as data}]
  [:div {:class "node-action"
         :style (merge anim
                  {:display "inline-block"
                   :border-style "solid"
                   :border-width 3
                   :width (if show 10 0)
                   :height 10})}
    [:a {:style {:position "absolute"
                 :top 0
                 :text-decoration "none"
                 :color "black"}}
      (gstring/unescapeEntities "&#9657;")]])

(defn node-sub [sub]
  [:div {:style {:display "inline-block"
                 :margin-left -1
                 :transform "translate(0px, -3px)"}}
    sub])

(defn node-search-action [{:keys [show anim] :as data}]
  [:input {:type "text"
           :style (merge anim
                    {:margin "2px"
                     :border-style "solid"
                     :border-width 1
                     :width (if show 150 0)
                     :height 22
                     :transform "translate(0px, -3px)"})
           :placeholder ":example"}])

(defn node-logout-action [{:keys [show anim] :as data}]
  [:div {:class "node-action"
         :style (merge anim
                  {:display "inline-block"
                   :border-style "solid"
                   :border-width 3
                   :width (if show 10 0)
                   :height 10
                   :top -15})}
    [:a {:href "logout"
         :style {:position "absolute"
                 :top 0
                 :text-decoration "none"
                 :color "black"}
         :title "Logout"}
      (gstring/unescapeEntities "&#215;")]])

(defn node [sub]
  (let [is-collapsed (r/atom true)
        data (r/atom {})]
    (sente/chsk-send! [:data/read-sub {:sub sub}] 5000
      (fn [result]
        (reset! data result)))
    (fn []
      (let [show (not @is-collapsed)
            anim {:visibility (if show "visible" "hidden")
                  :opacity (if show "1.0" "0")
                  :transition "0.5s"}
            types ((:p-os @data {}) "/type" #{}),
            principle-type (if (empty? types) nil (first types))]
        [:div {:style {:position "relative"}}
          [dot is-collapsed]
          [node-expander-action {:show show :anim anim}]
          [node-sub (str sub)]
          (when (= principle-type :sovereign-type)
            [node-search-action {:show show :anim anim}])
          (when (= principle-type :sovereign-type)
            [node-logout-action {:show show :anim anim}])]))))

(defn human-head-component []
  (let [user-sub (r/atom nil)]
    (sente/chsk-send! [:sovereign/get-user-sub] 5000
      (fn [sub]
        (when (keyword? sub)
          (reset! user-sub sub))))
    (fn []
      (let [sub @user-sub]
        (if (nil? sub)
            [:div "You are logged out. " [:a {:href "login"} "Login"]]
            [node sub])))))

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
