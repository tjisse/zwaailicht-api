(defproject zwaailicht-api "0.1.0-SNAPSHOT"
  :description "Tiny RESTful API for controlling an alarm light connected to a serial port"
  :url "https://github.com/tjisse/zwaailicht-api"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.395"]
                 [compojure "1.5.1"]
                 [ring/ring-devel "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [http-kit "2.2.0"]
                 [clj-serial "2.0.3"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main zwaailicht-api.main
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
