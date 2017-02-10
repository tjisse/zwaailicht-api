(defproject zwaailicht-api "2016.12.11-0"
  :description "Tiny RESTful API for controlling an alarm light connected to a serial port"
  :url "https://github.com/tjisse/zwaailicht-api"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-devel "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [http-kit "2.2.0"]
                 [clj-serial "2.0.3"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.omcljs/om "1.0.0-alpha47"]
                 [binaryage/devtools "0.6.1"]
                 [bootstrap-cljs "0.29.2.0"]
                 [cljs-ajax "0.5.8"]]

  :plugins [[lein-cljsbuild "1.1.1"]]

  :main zwaailicht-api.main

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :cljsbuild {:builds
                [{:id "app"
                  :source-paths ["src/cljs"]
                  :figwheel     true
                  :compiler     {:main                 zwaailicht-fe.main
                                 :asset-path           "js/compiled/out"
                                 :output-to            "resources/public/js/compiled/zwaailicht.js"
                                 :output-dir           "resources/public/js/compiled/out"
                                 :source-map-timestamp true}}
                 {:id "test"
                  :source-paths ["src/cljs" "test/cljs"]
                  :compiler {:output-to "resources/public/js/compiled/testable.js"
                             :main zwaailicht-fe.test-runner :optimizations :none}}
                 {:id "min"
                  :source-paths ["src/cljs"]
                  :jar true
                  :compiler {:main zwaailicht-fe.main
                             :output-to "resources/public/js/compiled/zwaailicht.js"
                             :output-dir "target"
                             :source-map-timestamp true
                             :optimizations :advanced
                             :pretty-print false}}]}

  :figwheel {;; :http-server-root "public"                  ;; serve static assets from resources/public/
             ;; :server-port 3449                           ;; default
             ;; :server-ip "127.0.0.1"                      ;; default
             :css-dirs       ["resources/public/css"]       ;; watch and update CSS
             :ring-handler   user/http-handler
             :server-logfile "log/figwheel.log"}

  :profiles
    {:dev
       {:dependencies [[javax.servlet/servlet-api "2.5"]
                       [ring/ring-mock "0.3.0"]
                       [org.clojure/clojurescript "1.9.89"]
                       [figwheel "0.5.2"]
                       [figwheel-sidecar "0.5.2"]
                       [com.cemerick/piggieback "0.2.1"]
                       [org.clojure/tools.nrepl "0.2.12"]]
        :plugins      [[lein-figwheel "0.5.2"]
                       [lein-doo "0.1.6"]]
        :source-paths ["dev"]
        :repl-options {:init-ns user :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}

     :provided
       {:dependencies [[org.clojure/clojurescript "1.9.89"]]}

     :uberjar
       {:source-paths ^:replace ["src/clj" "src/cljc"]
        :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
        :hooks [leiningen.cljsbuild]
        :omit-source true
        :aot :all}})
