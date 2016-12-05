(ns zwaailicht-api.server
  (:require [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [ring.middleware.reload :refer [wrap-reload]]))

(defroutes app-routes
           (GET "/" [] "Hello World")
           (route/not-found "Not Found"))

(def http-handler
  (-> app-routes
      (wrap-defaults site-defaults)
      wrap-reload))
