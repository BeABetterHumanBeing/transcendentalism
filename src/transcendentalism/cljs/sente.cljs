(ns transcendentalism.sente
  (:require [reagent.core :as r]
            [taoensso.encore :as encore :refer-macros (have)]
            [taoensso.sente  :as sente]))

(enable-console-print!)

(def cxn-established (r/atom false))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

; Sente's client-side handlers.
(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client! "/chsk" ?csrf-token
       {:type :auto,
        :packer :edn})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))  ; Watchable, read-only atom

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event]}]
  (println "Unhandled event:" event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (when (:first-open? new-state-map)
      (reset! cxn-established true))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (println "Push event from server:" ?data))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (println "Handshake:" ?data)))

; TODO - other event-msg-handlers go here.

; Sente's event router.
(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
    (sente/start-client-chsk-router!
      ch-chsk event-msg-handler)))

; Start the Sente router
(defonce _start-once (start-router!))
