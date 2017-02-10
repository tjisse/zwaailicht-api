(ns zwaailicht-api.main
  (:require [org.httpkit.server :refer [run-server]]
            [zwaailicht-api.server :refer [http-handler]]
            [zwaailicht-api.serial :refer :all]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string])
  (:gen-class))

(def cli-options
  [["-s" "--serial SERIAL_PORT" "Serial port id/path" :default "COM3"]
   ["-p" "--port PORT" "HTTP server listen port"
    :default 8080
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 summary)
      errors (exit 1 (string/join \newline errors)))
    (let [port (get options :port) serial (get options :serial)]
      (println (str "Starting Zwaailicht API on http://localhost:" port))
      (connect! serial 57600)
      (run-server http-handler {:port port}))))
