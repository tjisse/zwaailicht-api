(ns zwaailicht-api.serial
  (:require
    [serial.core :as serial]))

(def state (atom {:conn nil :state "off"}))

(defn connect! [port-id baud-rate]
  (->>
    (serial/open port-id :baud-rate baud-rate)
    (println "Serial port connected")
    (swap! state assoc :conn)))

(defn- execute-command! [command serial-conn]
  (serial/write serial-conn (.getBytes (str command \newline)))
  nil)

(defn set-state! [new-state]
  (let [serial-conn (get @state :conn)]
    (if (contains? ["on" "off"] state)
      ((execute-command! new-state serial-conn)
       (reset! state new-state))
      (:invalid-state))))
