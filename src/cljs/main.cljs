(ns zwaailicht-fe.main
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [devtools.core :as devtools]
            [bootstrap-cljs.core :as bs :include-macros true]
            [ajax.core :refer [GET POST]]))

(enable-console-print!)
(devtools/install!)

(def app-state (atom {:state false}))

(defn read [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

(defn mutate [{:keys [state] :as env} key params]
  (cond
    (= 'switch-on key) {:action #(swap! app-state assoc :state true)}
    (= 'switch-off key) {:action #(swap! app-state assoc :state false)}
    :else {:value :not-found}))

(def reconciler
  (om/reconciler
    {:state app-state
     :parser (om/parser {:read read :mutate mutate})}))

(defn handle-on-press []
  (POST "/state" {:body "on"
                  :handler #(om/transact! reconciler '[(switch-on)])
                  :headers {:content-type "text/plain; charset=utf-8"}}))

(defn handle-off-press []
  (POST "/state" {:body "off"
                  :handler #(om/transact! reconciler '[(switch-off)])
                  :headers {:content-type "text/plain; charset=utf-8"}}))

(defui Buttons
  static om/IQuery
  (query [this]
    [:state])
  Object
  (render [this]
    (let [{:keys [state]} (om/props this)]
      (dom/div nil
               (bs/button {:bs-style "success" :on-click handle-on-press :disabled state} "on")
               (bs/button {:bs-style "danger" :on-click handle-off-press :disabled (not state)} "off")))))

(om/add-root! reconciler Buttons (gdom/getElement "app"))
