(ns zwaailicht-api.main
  (:require
    [org.httpkit.server :refer [run-server]]
    [zwaailicht-api.server :refer [http-handler]]
    [zwaailicht-api.serial :refer :all]
    [clojure.tools.cli :refer [parse-opts]]
    [clojure.string :as string]
    (:gen-class)))

(def cli-options
  [["-p" "--port PORT" "COM port"
    :default "COM3"]
   ["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 summary)
      errors (exit 1 (string/join \newline errors)))
    (let [serial (connect! (get options :port) 57600 1000)]
      (println (bitlash-funs-up-to-date? "def_bitlash_funs.script" serial))
      (run-server http-handler {:port 8080}))))