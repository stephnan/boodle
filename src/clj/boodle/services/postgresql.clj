(ns boodle.services.postgresql
  (:require [boodle.services.configuration :as config]
            [boodle.utils.dates :as ud]
            [boodle.utils.exceptions :as ex]
            [cheshire.core :as cheshire]
            [clojure.java.jdbc :as jdbc]
            [dire.core :as dire]
            [hikari-cp.core :as hikari]
            [honeysql.core :as sql]
            [java-time :as jt]
            [mount.core :as mount]
            [taoensso.timbre :as log])
  (:import [clojure.lang IPersistentMap IPersistentVector]
           [java.sql Date Timestamp]
           org.postgresql.util.PGobject))

(extend-protocol jdbc/IResultSetReadColumn
  Date
  (result-set-read-column [v _ _]
    (-> v
        jt/local-date
        ud/format-date))

  Timestamp
  (result-set-read-column [v _ _]
    (-> v
        jt/local-date
        ud/format-date))

  PGobject
  (result-set-read-column [pgobj _metadata _index]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (cheshire/parse-string value true)
        "jsonb" (cheshire/parse-string value true)
        "citext" (str value)
        value))))

(defn to-pg-json
  [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (cheshire/generate-string value))))

(extend-protocol jdbc/ISQLValue
  IPersistentMap
  (sql-value [value] (to-pg-json value))
  IPersistentVector
  (sql-value [value] (to-pg-json value)))

(extend-protocol jdbc/ISQLValue
  java.time.LocalDateTime
  (sql-value [v] (jt/sql-timestamp v))
  java.time.LocalDate
  (sql-value [v] (jt/sql-timestamp v)))

(defn- make-datasource-options
  []
  (let [host (get-in config/config [:postgresql :host])
        db-name (get-in config/config [:postgresql :db-name])
        user (get-in config/config [:postgresql :user])
        password (get-in config/config [:postgresql :password])]
    {:auto-commit        true
     :read-only          false
     :connection-timeout 30000
     :validation-timeout 5000
     :idle-timeout       600000
     :max-lifetime       1800000
     :minimum-idle       10
     :maximum-pool-size  10
     :pool-name          "db-pool"
     :adapter            "postgresql"
     :username           user
     :password           password
     :database-name      db-name
     :server-name        host
     :port-number        5432
     :register-mbeans    false}))

(defn connect!
  []
  (hikari/make-datasource (make-datasource-options)))

(defn disconnect!
  "Close the datasource."
  [datasource]
  (hikari/close-datasource datasource))

(mount/defstate datasource
  :start (connect!)
  :stop (disconnect! datasource))

(defn snake-case->kebab-case
  [column]
  (when (keyword? column)
    (keyword (clojure.string/replace (name column) #"_" "-"))))

(defn kebab-case->snake-case
  [column]
  (when (keyword? column)
    (keyword (clojure.string/replace (name column) #"-" "_"))))

(defn format-input-keywords
  "Convert `input` keywords from kebab-case to snake_case."
  [input]
  (reduce-kv
   (fn [m k v]
     (assoc m (kebab-case->snake-case k) v))
   {}
   input))

(defn format-output-keywords
  "Convert `output` keywords from snake_case to kebab-case."
  [output]
  (reduce-kv
   (fn [m k v]
     (assoc m (snake-case->kebab-case k) v))
   {}
   output))

(defn query
  "Run a query using the map in `sqlmap`."
  [sqlmap]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (->> sqlmap
         sql/format
         (jdbc/query conn)
         (map format-output-keywords))))

(dire/with-handler! #'query
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in query"))))

(defn execute!
  "Execute an insert/update/delete query using the map in `sqlmap`."
  [sqlmap]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (->> sqlmap
         format-input-keywords
         sql/format
         (jdbc/execute! conn))))

(dire/with-handler! #'execute!
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in execute!"))))
