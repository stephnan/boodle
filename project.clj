(defproject boodle "1.0.0"
  :description "boodle: a simple accounting SPA"
  :url "http://github.com/manuel-uberti/boodle"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [;; Clojure
   [cheshire "5.8.0"]
   [clojure.java-time "0.3.2"]
   [com.taoensso/timbre "4.10.0"]
   [com.walmartlabs/test-reporting "0.1.0"]
   [compojure "1.6.1"]
   [hiccup "1.0.5"]
   [hikari-cp "2.6.0"]
   [honeysql "0.9.3"]
   [http-kit "2.3.0"]
   [metosin/ring-http-response "0.9.0"]
   [mount "0.1.13"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/core.incubator "0.1.4"]
   [org.clojure/java.jdbc "0.7.8"]
   [org.clojure/tools.reader "1.3.0"]
   [org.postgresql/postgresql "42.2.5"]
   [ring "1.6.3"]
   [ring/ring-defaults "0.3.2"]
   [ring/ring-json "0.4.0"]
   [ring/ring-mock "0.3.2"]
   [ring-middleware-format "0.7.2"]

   ;; ClojureScript
   [bidi "2.1.4"]
   [camel-snake-kebab "0.4.0" :exclusions [org.clojure/clojure]]
   [cljs-ajax "0.7.4"]
   [cljsjs/moment "2.22.2-0"]
   [cljsjs/pikaday "1.5.1-2"]
   [com.andrewmcveigh/cljs-time "0.5.2"]
   [day8.re-frame/http-fx "0.1.6"]
   [kibu/pushy "0.3.8"]
   [org.clojure/clojurescript "1.10.312"]
   [re-frame "0.10.6"]
   [reagent "0.8.1"]
   [reagent-utils "0.3.1"]
   [tongue "0.2.4"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-eftest "0.5.3"]]
  :eftest {:multithread? false}

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj" "test/cljs"]
  :resource-paths ["target" "resources"]
  :target-path "target/%s"

  :aliases
  {"fig" ["trampoline" "run" "-m" "figwheel.main"]
   "build" ["trampoline" "run" "-m" "figwheel.main" "-b" "boodle"]}

  :clean-targets ^{:protect false} [:target-path "resources/public/js"]

  :main ^:skip-aot boodle.core

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [cider/piggieback "0.3.9"]
                   [com.bhauman/figwheel-main "0.1.8"]
                   [com.bhauman/rebel-readline-cljs "0.1.4"]
                   [figwheel-sidecar "0.5.16"]]
    :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}}

  :cljsbuild
  {:builds
   {:min
    {:source-paths ["src/cljs"]
     :compiler {:main boodle.core
                :output-to "target/public/cljs-out/boodle-main.js"
                :asset-path "target/public/cljs-out/boodle"
                :optimizations :advanced
                :pretty-print false}}}})
