(ns transcendentalism.sente
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [transcendentalism.access :refer :all]
            [transcendentalism.graph :refer :all]))

; Sente's server-side handlers.
(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket-server! (get-sch-adapter) {:packer :edn})]

  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids)) ; Watchable, read-only atom

; Add a watcher on connected-uids, for debugging purposes.
(add-watch connected-uids :connected-uids
  (fn [_ _ old new]
    (when (not= old new)
      (println "Connected uids changed:" new))))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [graph]
  (fn [{:as ev-msg :keys [id ?data event]}]
    (-event-msg-handler ev-msg graph)))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]} graph]
  (let [event-id (first event)]
    ; Log interesting unhandled events.
    (when (not (contains? #{:chsk/ws-ping} ; Websocket ping.
                          event-id))
      (println "Unhandled event:" event)))
  (when ?reply-fn
    (?reply-fn {:umatched-event-as-echoed-from-server event})))

(defmethod -event-msg-handler :sovereign/get-user-sub
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]} graph]
  (when ?reply-fn
    (?reply-fn (@username-to-sub (get-in ring-req [:session
                                                   :cemerick.friend/identity
                                                   :current])))))

(defmethod -event-msg-handler :data/read-sub
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]} graph]
  (when ?reply-fn
    (let [graphlet (get-graphlet graph (:sub ?data))]
      (?reply-fn {:v (:v graphlet),
                  :p-os (:p-os graphlet)}))))

; TODO - other event-msg-handlers go here.

; Sente's event router.
(defonce router_ (atom nil))
(defn  stop-sente-router! [] (when-let [stop-fn @router_] (stop-fn)))
(defn start-sente-router! [graph]
  (stop-sente-router!)
  (reset! router_
    (sente/start-server-chsk-router!
      ch-chsk (event-msg-handler graph))))
