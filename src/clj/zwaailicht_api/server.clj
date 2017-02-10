(ns zwaailicht-api.server
  (:require [compojure.core :refer :all]
            [compojure.route :refer [resources]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [zwaailicht-api.serial :as serial]
            [clojure.java.io :as io]))

(defn handle-state-post [new-state]
  (serial/set-state! new-state)
  "")

(defroutes app-routes
           (GET "/" _
             {:status  200
              :headers {"Content-Type" "text/html; charset=utf-8"}
              :body    (io/input-stream (io/resource "public/index.html"))})
           (GET "/state" _ (get @serial/state :state))
           (POST "/state" {new-state :body} (handle-state-post (slurp new-state)))
           (resources "/")
           (route/not-found "Not Found"))

(def http-handler
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      wrap-reload))
