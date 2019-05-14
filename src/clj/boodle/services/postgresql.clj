(ns boodle.services.postgresql
  (:require
   [boodle.utils.dates :as dates]
   [boodle.utils.exceptions :as exceptions]
   [cheshire.core :as cheshire]
   [clojure.java.jdbc :as jdbc]
   [clojure.string :as s]
   [hikari-cp.core :as hikari]
   [honeysql.core :as sql]
   [honeysql.format :as fmt]
   [java-time.local :as jl]
   [java-time.pre-java8 :as jp]
   [taoensso.timbre :as log])
  (:import
   (clojure.lang IPersistentMap IPersistentVector)
   (java.sql Date Timestamp)
   (org.postgresql.util PGobject)))

(extend-protocol jdbc/IResultSetReadColumn
  Date
  (result-set-read-column [v _ _]
    (-> v
        jl/local-date
        dates/format-date))

  Timestamp
  (result-set-read-column [v _ _]
    (-> v
        jl/local-date
        dates/format-date))

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
  (sql-value [v] (jp/sql-timestamp v))
  java.time.LocalDate
  (sql-value [v] (jp/sql-timestamp v)))

(defn- make-datasource-options
  [config]
  (let [host (get-in config [:postgresql :host])
        db-name (get-in config [:postgresql :db-name])
        user (get-in config [:postgresql :user])
        password (get-in config [:postgresql :password])]
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

(def datasource (atom nil))

(defn connect!
  [config]
  (->> config
       make-datasource-options
       hikari/make-datasource
       (reset! datasource)))

(defn disconnect!
  []
  (hikari/close-datasource @datasource)
  (reset! datasource nil))

(defn snake-case->kebab-case
  [column]
  (when (keyword? column)
    (keyword (s/replace (name column) #"_" "-"))))

(defn format-output-keywords
  "Convert `output` keywords from snake_case to kebab-case."
  [output]
  (reduce-kv
   (fn [m k v]
     (assoc m (snake-case->kebab-case k) v))
   {}
   output))

(defmethod fmt/fn-handler "ilike" [_ col qstr]
  (str (fmt/to-sql col) " ilike " (fmt/to-sql qstr)))

(defmethod fmt/fn-handler "not-ilike" [_ col qstr]
  (str (fmt/to-sql col) " not ilike " (fmt/to-sql qstr)))

(defmethod fmt/fn-handler "is-true" [_ col qstr]
  (str (fmt/to-sql col) " is " (fmt/to-sql qstr) " true"))

(defmethod fmt/fn-handler "not-true" [_ col qstr]
  (str (fmt/to-sql col) " is not " (fmt/to-sql qstr) " true"))

(defn query
  "Run a query using the map in `sqlmap`."
  [sqlmap]
  (let [q (sql/format sqlmap)]
    (try
      (jdbc/with-db-connection [conn {:datasource @datasource}]
        (->> q
             (jdbc/query conn)
             (map format-output-keywords)))
      (catch Exception e
        (log/error (exceptions/get-stacktrace e))
        (throw (ex-info "Exception in query" {:sqlmap sqlmap :query q}))))))

(defn execute!
  "Execute an insert/update/delete query using the map in `sqlmap`."
  [sqlmap]
  (let [q (sql/format sqlmap)]
    (try
      (jdbc/with-db-connection [conn {:datasource @datasource}]
        (jdbc/execute! conn q))
      (catch Exception e
        (log/error (exceptions/get-stacktrace e))
        (throw (ex-info "Exception in execute!" {:sqlmap sqlmap :query q}))))))
