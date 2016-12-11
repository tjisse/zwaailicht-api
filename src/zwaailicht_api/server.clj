(ns zwaailicht-api.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [zwaailicht-api.serial :as serial]))

(defn handle-state-post [new-state]
  (serial/set-state! new-state)
  "")

(defroutes app-routes
           (GET "/" [] (get @serial/state :state))
           (POST "/" {new-state :body} (handle-state-post (slurp new-state)))
           (route/not-found "Not Found"))

(def http-handler
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      wrap-reload))
