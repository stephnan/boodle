(ns boodle.services.postgresql
  (:require
   [boodle.services.configuration :as config]
   [boodle.utils.exceptions :as ex]
   [cheshire.core :as cheshire]
   [clojure.java.jdbc :as jdbc]
   [dire.core :as dire]
   [hikari-cp.core :as hikari]
   [mount.core :as mount]
   [taoensso.timbre :as log])
  (:import org.postgresql.util.PGobject
           clojure.lang.IPersistentMap
           clojure.lang.IPersistentVector
           [java.sql
            BatchUpdateException
            Date
            Timestamp
            PreparedStatement]))

(defn format-date [v]
  (.format (java.text.SimpleDateFormat. "dd/MM/yyyy") v))

(extend-protocol jdbc/IResultSetReadColumn
  Date
  (result-set-read-column [v _ _]
    (format-date v))

  Timestamp
  (result-set-read-column [v _ _]
    (format-date v))

  PGobject
  (result-set-read-column [pgobj _metadata _index]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (cheshire/parse-string value true)
        "jsonb" (cheshire/parse-string value true)
        "citext" (str value)
        value))))

(defn to-pg-json [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (cheshire/generate-string value))))

(extend-protocol jdbc/ISQLValue
  IPersistentMap
  (sql-value [value] (to-pg-json value))
  IPersistentVector
  (sql-value [value] (to-pg-json value)))

(defn- make-datasource-options []
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

(defn connect! []
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
  "Execute a SELECT statement using the SQL in `query-sql`."
  [query-sql]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (map format-output-keywords (jdbc/query conn query-sql))))

(dire/with-handler! #'query
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in query"))))

(defn insert!
  "Insert `values` into `table`.
   `table` is a keyword (i.e.: :categories).
   `values` is a map of columns and values (i.e.: {:name \"Test\"})."
  [table values]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (let [input-values (format-input-keywords values)]
      (jdbc/insert! conn table input-values))))

(dire/with-handler! #'insert!
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in insert!"))))

(defn update!
  "Execute an update using the SQL in `query-sql`.
   `query-sql` is a vector with the SQL statement and the necessary parameters."
  [query-sql]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (jdbc/execute! conn query-sql)))

(dire/with-handler! #'update!
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in update!"))))

(defn delete!
  "Tiny wrapper around `update!`, just syntactic sugar."
  [query-sql]
  (update! query-sql))

(dire/with-handler! #'delete!
  java.lang.Throwable
  (fn [e & args]
    (log/debugf "Exception: %s" (ex/get-stacktrace e))
    (throw (Exception. "Errore in delete!"))))
