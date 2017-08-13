(ns boodle.model.report
  (:require
   [boodle.services.postgresql :as db]
   [taoensso.timbre :as log]))

(defn categories->in
  [l]
  (when-not (empty? l)
    (str
     "AND e.id_category IN ("
     (->> (map #(str "cast(" % " as integer)") l)
          (interpose ", ")
          (apply str))
     ")")))

(defn get-data
  [from to categories]
  (db/query
   [(str
     "SELECT id, date, category, item, amount FROM (
       SELECT e.id, TO_CHAR(e.date, 'dd/mm/yyyy') AS date,
       e.date as temp_date, c.name as category, e.item, e.amount
       FROM expenses e
       INNER JOIN categories c on e.id_category = c.id
       WHERE e.date >= TO_DATE(?, 'DD/MM/YYYY')
       AND e.date <= TO_DATE(?, 'DD/MM/YYYY') "
     (categories->in categories)
     " ORDER BY temp_date DESC) t")
    from to]))
