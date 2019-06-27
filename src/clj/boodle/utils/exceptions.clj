(ns boodle.utils.exceptions
  (:require [clojure.repl :as repl]))

(defmacro with-err-str
  "Evaluates exprs in a context in which *err* is bound to a fresh
   StringWriter.  Returns the string created by any nested printing
   calls."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*err* s#]
       ~@body
       (str s#))))

(defn stacktrace
  [e]
  (with-err-str (repl/pst e 36)))
