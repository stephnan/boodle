(ns boodle.services.http
  (:require
   [boodle.api.aim :as aim]
   [boodle.api.category :as category]
   [boodle.api.expense :as expense]
   [boodle.api.fund :as fund]
   [boodle.api.saving :as saving]
   [boodle.api.transaction :as transaction]
   [compojure.core :as compojure]
   [compojure.route :as route]
   [hiccup.page :as hiccup]
   [org.httpkit.server :as httpkit]
   [ring.middleware.format :as restful]
   [ring.middleware.reload :as reload]
   [ring.util.http-response :as response]))

(defn index-html
  "boodle main index page."
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
    (hiccup/include-js "/js/main.js")
    [:script "boodle.core.init();"]]))

(compojure/defroutes api-routes
  (compojure/wrap-routes aim/routes restful/wrap-restful-format)
  (compojure/wrap-routes category/routes restful/wrap-restful-format)
  (compojure/wrap-routes expense/routes restful/wrap-restful-format)
  (compojure/wrap-routes fund/routes restful/wrap-restful-format)
  (compojure/wrap-routes saving/routes restful/wrap-restful-format)
  (compojure/wrap-routes transaction/routes restful/wrap-restful-format))

(defn add-datasource
  [handler datasource]
  (fn [request]
    (handler (assoc request :datasource datasource))))

(defn make-routes
  [datasource]
  (-> (compojure/routes
       (route/resources "/")
       (compojure/GET "/" [] (response/ok (index-html)))
       (compojure/GET "/savings" [] (response/ok (index-html)))
       (compojure/GET "/categories" [] (response/ok (index-html)))
       api-routes)
      (add-datasource datasource)
      reload/wrap-reload))

(defonce server (atom nil))

(defn stop-server!
  []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn start-server!
  [datasource port]
  (let [app-routes (compojure/routes (make-routes datasource))]
    (reset! server (httpkit/run-server app-routes {:port port}))))
