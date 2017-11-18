(ns boodle.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]))

(def routes
  ["/" {"" :expenses-home
        "aims" :aims-home
        "report" :report-home}])

(defn- parse-url []
  (fn [url]
    (bidi/match-route routes url)))

(defn- keywordize-route [route]
  (keyword (str route "-panel")))

(defn- dispatch-route [matched-route]
  (let [route (name (:handler matched-route))
        panel-name (keywordize-route route)]
    (rf/dispatch [:set-active-panel panel-name])))

(defn app-routes []
  (pushy/start! (pushy/pushy dispatch-route (parse-url))))
