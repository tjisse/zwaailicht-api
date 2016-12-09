(ns zwaailicht-api.serial
  (:require
    [serial.core :as serial]))

(def state (atom "off"))

(defn connect! [port-id baud-rate]
  (serial/open port-id :baud-rate baud-rate))

(defn- execute-command! [command port]
  (serial/write port (.getBytes (str command \newline)))
  nil)

(defn set-state! [new-state port]
  (if (contains? ["on" "off"] state)
    ((execute-command! new-state port)
     (reset! state new-state)
     nil)
    (:invalid-state)))
