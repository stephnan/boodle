(ns boodle.templates
  (:require [hiccup.page :as hiccup]))

(defn index-html
  "boodle main index."
  []
  (hiccup/html5
   {:lang "it"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta
     {:name "viewport"
      :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
    [:title "boodle"]
    (hiccup/include-css "/css/boodle.min.css")]
   [:body
    [:div#app]
    (hiccup/include-js "cljs-out/boodle-main.js")
    [:script "boodle.core.init();"]]))
