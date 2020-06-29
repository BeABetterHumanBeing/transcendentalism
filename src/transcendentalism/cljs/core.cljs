(ns transcendentalism.core
  (:require [clojure.string :as str]
            [goog.string :as gstring]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [transcendentalism.cljc.encoding :as encoding]
            [transcendentalism.sente :as sente]))

; Note that most CSS is being inlined, except that which cannot, which is being
; supplied via the insertion-pt, and specified in render.clj

(defn anim
  [show]
  {:visibility (if show "visible" "hidden")
   :opacity (if show "1.0" "0")
   :transition "0.5s"})

(defn intercept
  "Wraps an event callback so that it stops propogation"
  [f]
  (fn [e]
    (.stopPropagation e)
    (f e)))

(defn dot [node-open]
  [:div {:class "node-action"
         :style {:display "inline-block"
                 :background "black"
                 :border-style "solid"
                 :border-width 3
                 :border-radius "50%"
                 :width 10
                 :height 10}
         :on-click (intercept #(reset! node-open (not @node-open)))}])

(defn node-expander-action [{:keys [node-open data] :as args}]
  (let [edge-open (r/atom false)]
    (fn []
      [:div {:style (merge (anim @node-open)
                      {:position "relative"
                       :display "inline-block"})}
        [:div {:class "node-action"
               :style {:border-style "solid"
                       :border-width 3
                       :width (if @node-open 10 0)
                       :height 10}
               :on-click (intercept #(reset! edge-open (not @edge-open)))}
          [:span {:style {:position "absolute"
                          :transform "translate(0px, -4px)"}}
            (gstring/unescapeEntities (if @edge-open "&#9663;" "&#9657;"))]]
        (when @edge-open
          [:div {:style {:position "absolute"
                         :transform "translate(0px, 5px)"}}
            (doall (for [pred (keys (:p-os @data {}))]
              (let [objs ((:p-os @data {}) pred)]
                (reduce
                  (fn [result o]
                    (-> result
                        (conj [:div {} ""]) ; Empty div for grid column.
                        (conj [:div {} o])))
                  ^{:key pred} [:div {:style {:display "grid"
                                              :grid-template-columns "min-content min-content"
                                              :grid-column-gap 5}}
                                 [:div {:style {:color "gray"}} pred]
                                 [:div {} (first objs)]]
                  (rest objs)))))])])))

(defn node-sub [sub]
  [:div {:style {:display "inline-block"
                 :margin-left -1
                 :transform "translate(0px, -3px)"}}
    sub])

(defn node-search-action [{:keys [node-open] :as args}]
  [:input {:type "text"
           :style (merge (anim @node-open)
                    {:margin "2px"
                     :border-style "solid"
                     :border-width 1
                     :width (if @node-open 150 0)
                     :height 22
                     :transform "translate(0px, -3px)"})
           :placeholder ":example"}])

(defn node-logout-action [{:keys [node-open] :as args}]
  [:div {:class "node-action"
         :style (merge (anim @node-open)
                  {:display "inline-block"
                   :border-style "solid"
                   :border-width 3
                   :width (if @node-open 10 0)
                   :height 10
                   :top -15})}
    [:a {:href "logout"
         :style {:position "absolute"
                 :top 0
                 :text-decoration "none"
                 :color "black"}
         :title "Logout"}
      (gstring/unescapeEntities "&#215;")]])

(defn node [{:keys [sub x y] :as node-data}]
  (let [node-open (r/atom false)
        data (r/atom {})]
    (sente/chsk-send! [:data/read-sub {:sub sub}] 5000
      (fn [result]
        (when (map? result)
          (reset! data (reduce-kv
                         (fn [result k v]
                           (if (nil? v)
                               result
                               (assoc result k v)))
                         {:v nil :p-os {}} result)))))
    (fn []
      (let [types ((:p-os @data {}) "/type" #{}),
            principle-type (if (empty? types) nil (first types))]
        [:div {:style {:position "absolute"
                       :max-width "max-content"
                       :top y
                       :left x}
               :on-click (intercept #())} ; Don't let clicks fall through.
          [:div {:style {:position "relative"}}
            [dot node-open]
            [node-expander-action {:node-open node-open
                                   :data data}]
            [node-sub (str sub)]
            (when (= principle-type :sovereign-type)
              [node-search-action {:node-open node-open}])
            (when (= principle-type :sovereign-type)
              [node-logout-action {:node-open node-open}])]]))))

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
            [node {:sub sub :x 100 :y 100}])))))

(defn master-component []
  (let [dots (r/atom #{})]
    (fn []
      [:div {:style {:width "100%"
                     :height "100%"}
             :on-click (fn [e]
                         (let [sub (keyword (encoding/gen-key 16))]
                           (reset! dots (conj @dots {:sub sub
                                                     :x (.-pageX e)
                                                     :y (.-pageY e)}))))}
        [human-head-component]
        (for [{:keys [sub] :as dot-data} @dots]
          ^{:key sub} [node dot-data])])))

(defn render-page
  []
  (rd/render [master-component] (.getElementById js/document "insertion-pt")))

(if @sente/cxn-established
  (render-page)
  (add-watch sente/cxn-established :cxn-established
    (fn [_ _ _ _] (render-page))))
