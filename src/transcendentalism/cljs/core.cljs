(ns transcendentalism.core
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [reagent.dom :as rd]))

(enable-console-print!)

(defn master-component []
  [:div {:style {:margin "0 auto"
                 :padding-top 100}}
    "User's attention set goes here"])

(rd/render [master-component] (.getElementById js/document "insertion-pt"))
