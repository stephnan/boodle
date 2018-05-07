(defproject boodle "1.0.0"
  :description "boodle: a simple accounting SPA"
  :url "http://github.com/manuel-uberti/boodle"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [;; Clojure
   [cheshire "5.8.0"]
   [clojure.java-time "0.3.2"]
   [com.taoensso/encore "2.96.0"]
   [com.taoensso/timbre "4.10.0" :exclusions [com.taoennso/encore]]
   [com.walmartlabs/test-reporting "0.1.0"]
   [dire "0.5.4" :exclusions [org.clojure/core.incubator]]
   [hiccup "1.0.5"]
   [hikari-cp "2.4.0"]
   [http-kit "2.3.0"]
   [metosin/compojure-api "1.1.12"]
   [metosin/ring-http-response "0.9.0"]
   [mount "0.1.12"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/core.incubator "0.1.4"]
   [org.clojure/java.jdbc "0.7.6"]
   [org.clojure/tools.reader "1.2.2"]
   [org.postgresql/postgresql "42.2.2"]
   [prone "1.5.2"]
   [ring "1.6.3"]
   [ring/ring-defaults "0.3.1"]
   [ring/ring-json "0.4.0"]
   [ring/ring-mock "0.3.2"]
   [ring-middleware-format "0.7.2"]

   ;; ClojureScript
   [bidi "2.1.3"]
   [camel-snake-kebab "0.4.0" :exclusions [org.clojure/clojure]]
   [cljs-ajax "0.7.3"]
   [cljsjs/moment "2.22.0-0"]
   [cljsjs/pikaday "1.5.1-2"]
   [com.andrewmcveigh/cljs-time "0.5.2"]
   [day8.re-frame/http-fx "0.1.6"]
   [kibu/pushy "0.3.8"]
   [org.clojure/clojurescript "1.9.946"]
   [re-frame "0.10.5"]
   [reagent "0.8.0"]
   [reagent-utils "0.3.1"]
   [tongue "0.2.4"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-eftest "0.5.1"]]
  :eftest {:multithread? false}
  :figwheel {:css-dirs ["resources/public/css"]}

  :source-paths ["src/clj"]
  :test-paths ["test/clj" "test/cljs"]
  :resource-paths ["resources"]
  :target-path "target/%s"

  :clean-targets ^{:protect false} [:target-path "resources/public/js"]

  :main ^:skip-aot boodle.core

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [cider/piggieback "0.3.2"]
                   [day8.re-frame/re-frame-10x "0.3.3"]
                   [day8.re-frame/tracing "0.5.1"]
                   [figwheel-sidecar "0.5.16"]]
    :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
    :plugins [[lein-figwheel "0.5.16"]]}
   :min
   {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}

  :cljsbuild
  {:builds
   {:dev
    {:source-paths ["src/cljs"]
     :figwheel true
     :compiler
     {:main boodle.core
      :asset-path "js/out"
      :output-to "resources/public/js/main.js"
      :output-dir "resources/public/js/out"
      :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true
                        "day8.re_frame.tracing.trace_enabled_QMARK_" true}
      :preloads [day8.re-frame-10x.preload devtools.preload]
      :external-config {:devtools/config
                        {:features-to-install :all}}}}
    :min
    {:source-paths ["src/cljs"]
     :compiler {:main boodle.core
                :output-to "resources/public/js/main.js"
                :asset-path "js/out"
                :optimizations :simple
                :pretty-print false}}}})
