(ns zwaailicht-api.serial
  (:require
    [serial.core :as serial]))

(def state (atom {:conn nil :state "off"}))

(defn connect! [port-id baud-rate]
  (->>
    (serial/open port-id :baud-rate baud-rate)
    (swap! state assoc :conn))
  (println "Serial port connected"))

(defn disconnect! [serial-conn]
  (serial/close! serial-conn)
  (swap! state assoc :conn nil)
  nil)

(defn- execute-command! [command serial-conn]
  (serial/write serial-conn (.getBytes (str command \newline)))
  nil)

(defn set-state! [new-state]
  (let [serial-conn (get @state :conn)]
    (if (.contains ["on" "off"] new-state)
      (do (execute-command! new-state serial-conn)
          (swap! state assoc :state new-state)
          nil)
      ::invalid-state)))
