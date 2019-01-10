(ns boodle.services.http
  (:require [boodle.api.routes :as api]
            [boodle.services.configuration :as config]
            [boodle.templates :as templates]
            [compojure.core :as compojure]
            [compojure.route :as route]
            [mount.core :as mount]
            [org.httpkit.server :as server]
            [ring.middleware.reload :as reload]
            [ring.util.http-response :as response]))

(compojure/defroutes app
  (-> (compojure/routes
       (route/resources "/")
       (compojure/GET "/" [] (response/ok (templates/index-html)))
       (compojure/GET "/savings" [] (response/ok (templates/index-html)))
       (compojure/GET "/categories" [] (response/ok (templates/index-html)))
       api/routes)
      reload/wrap-reload))

(defonce server (atom nil))

(defn stop-server!
  []
  (when (nil? @server)
    (reset! server nil)
    (@server :timeout 100)))

(defn start-server!
  []
  (let [port (get-in config/config [:http :port])]
    (reset! server (server/run-server app {:port port}))))

(mount/defstate http-server
  :start (start-server!)
  :stop (stop-server!))
