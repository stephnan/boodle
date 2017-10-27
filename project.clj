(defproject boodle "0.1.0-SNAPSHOT"
  :description "boodle: a simple accounting SPA"
  :url "http://github.com/manuel-uberti/boodle"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [;; Clojure
   [cheshire "5.7.1"]
   [clojure.java-time "0.3.0"]
   [com.taoensso/encore "2.87.0"]
   [com.taoensso/timbre "4.10.0" :exclusions [com.taoennso/encore]]
   [dire "0.5.4"]
   [expound "0.1.0"]
   [hiccup "1.0.5"]
   [hikari-cp "1.7.6"]
   [http-kit "2.2.0"]
   [metosin/compojure-api "1.1.10"]
   [metosin/ring-http-response "0.9.0"]
   [mount "0.1.11"]
   [org.clojure/clojure "1.9.0-alpha17"]
   [org.clojure/java.jdbc "0.7.0-beta5"]
   [org.clojure/test.check "0.9.0"]
   [org.postgresql/postgresql "9.4.1212"]
   [prone "1.1.4"]
   [ring "1.6.1"]
   [ring/ring-defaults "0.3.0"]
   [ring/ring-json "0.4.0"]
   [ring/ring-mock "0.3.1"]
   [ring-middleware-format "0.7.2"]

   ;; ClojureScript
   [bidi "2.1.1"]
   [cljs-ajax "0.6.0"]
   [day8.re-frame/http-fx "0.1.4"]
   [kibu/pushy "0.3.7"]
   [org.clojure/clojurescript "1.9.521"]
   [re-frame "0.9.4"]
   [reagent "0.7.0"]
   [reagent-utils "0.2.1"]]

  :plugins [[lein-cljsbuild "1.1.6"]]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :figwheel {:css-dirs ["resources/public/css"]}

  :source-paths ["src/clj"]
  :test-paths ["test/clj" "test/cljs"]
  :resource-paths ["resources"]
  :target-path "target/%s"

  :clean-targets ^{:protect false} [:target-path "resources/public/js"]

  :main ^:skip-aot boodle.core

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [com.cemerick/piggieback "0.2.2"]
                   [figwheel-sidecar "0.5.11"]]
    :plugins [[lein-figwheel "0.5.11"]]}}

  :cljsbuild
  {:builds
   {:dev
    {:source-paths ["src/cljs"]
     :figwheel true
     :compiler {:main boodle.core
                :asset-path "js/out"
                :output-to "resources/public/js/main.js"
                :output-dir "resources/public/js/out"
                :preloads [devtools.preload]
                :external-config {:devtools/config
                                  {:features-to-install :all}}}}}})
