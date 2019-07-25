(ns boodle.services.http
  (:require
   [boodle.api.routes :as api]
   [boodle.templates :as templates]
   [compojure.core :as compojure]
   [compojure.route :as route]
   [org.httpkit.server :as httpkit]
   [ring.middleware.reload :as reload]
   [ring.util.http-response :as response]))

(defn add-datasource
  [handler datasource]
  (fn [request]
    (handler (assoc request :datasource datasource))))

(defn make-routes
  [datasource]
  (-> (compojure/routes
       (route/resources "/")
       (compojure/GET "/" [] (response/ok (templates/index-html)))
       (compojure/GET "/savings" [] (response/ok (templates/index-html)))
       (compojure/GET "/categories" [] (response/ok (templates/index-html)))
       api/routes)
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
