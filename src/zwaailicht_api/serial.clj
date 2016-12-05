(ns zwaailicht-api.serial
  (:require
    [clojure.string :refer [split-lines]]
    [clojure.core.async :as async :refer [chan mult tap go go-loop >! >!! <! alt!! close! timeout]]
    [serial.core :as serial]
    [clojure.java.io :as io]))

(defn- mkfn-on-data-available [rx-chan]
  (fn [inputstream]
    (with-open [rdr (io/reader inputstream)]
      (->>
        (line-seq rdr)
        (map #(>!! rx-chan %))
        (doall)))))

(defn connect! [port-id baud-rate rx-timeout]
  (let [rx-chan (chan)
        port (serial/open port-id :baud-rate baud-rate)]
    (serial/listen! port (mkfn-on-data-available rx-chan))
    {:rx-chan-mult (mult rx-chan) :rx-timeout rx-timeout :port port}))

(defn- execute-command! [command port]
  (serial/write port (.getBytes (str command \newline)))
  nil)

(defn- execute-command-with-response [command response-transform-fn {:keys [rx-chan-mult port rx-timeout]}]
  (let [chan-tap (tap rx-chan-mult (chan))]
    (execute-command! command port)
    (alt!!
      (timeout rx-timeout) [nil :error-no_or_incorrect_response]
      (response-transform-fn chan-tap) ([val] [val nil]))))

(defn- read-bitlash-funs-from-file [filename]
  (->>
    (io/resource filename)
    (slurp)
    (split-lines)))

(defn bitlash-funs-up-to-date? [filename serial]
  (let [funs-from-file (read-bitlash-funs-from-file filename)
        response-transform-fn
          #(->>
             (map
               (fn [e] (let [pred-chan (chan)]
                         (go-loop []
                           (if (= (<! %) e)
                             ((>! pred-chan e)
                              (close! pred-chan))
                             (recur)))))
               funs-from-file)
             (async/merge))]
    (execute-command-with-response "ls" response-transform-fn serial)))
