(defproject boodle "1.0.0"
  :description "boodle: a simple accounting SPA"
  :url "http://github.com/manuel-uberti/boodle"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [;; Clojure
   [cheshire "5.8.0"]
   [clojure.java-time "0.3.1"]
   [com.taoensso/encore "2.93.0"]
   [com.taoensso/timbre "4.10.0" :exclusions [com.taoennso/encore]]
   [dire "0.5.4"]
   [hiccup "1.0.5"]
   [hikari-cp "2.0.1"]
   [http-kit "2.2.0"]
   [metosin/compojure-api "1.1.11"]
   [metosin/ring-http-response "0.9.0"]
   [mount "0.1.11"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/java.jdbc "0.7.5"]
   [org.clojure/test.check "0.9.0"]
   [org.clojure/tools.reader "1.1.2"]
   [org.postgresql/postgresql "42.2.0"]
   [prone "1.2.0"]
   [ring "1.6.3"]
   [ring/ring-defaults "0.3.1"]
   [ring/ring-json "0.4.0"]
   [ring/ring-mock "0.3.2"]
   [ring-middleware-format "0.7.2"]

   ;; ClojureScript
   [bidi "2.1.3"]
   [cljs-ajax "0.7.3"]
   [day8.re-frame/http-fx "0.1.4"]
   [kibu/pushy "0.3.8"]
   [org.clojure/clojurescript "1.9.946"]
   [re-frame "0.10.2"]
   [reagent "0.7.0"]
   [reagent-utils "0.2.1"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-eftest "0.4.1"]]
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
   {:dependencies [[binaryage/devtools "0.9.9"]
                   [com.cemerick/piggieback "0.2.2"]
                   [figwheel-sidecar "0.5.14"]]
    :plugins [[lein-figwheel "0.5.14"]]}}

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
