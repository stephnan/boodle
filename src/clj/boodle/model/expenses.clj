(ns boodle.model.expenses
  (:require
   [boodle.services.postgresql :as db]
   [taoensso.timbre :as log]))

(defn select-all
  []
  (db/query ["SELECT id, date, category, item, amount FROM (
                SELECT e.id, TO_CHAR(e.date, 'dd/mm/yyyy') AS date,
                e.date as temp_date,
                e.id_category as category,
                e.item, e.amount FROM expenses e
                ORDER BY temp_date DESC
                LIMIT 20) t"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM expenses WHERE id = cast(? as integer)" id]))

(defn select-by-item
  [item]
  (db/query ["SELECT * FROM expenses WHERE item = ?" item]))

(defn insert!
  [expense]
  (let [{:keys [date category item amount]} expense]
    (db/update! ["INSERT INTO expenses(date, id_category, item, amount)
                  VALUES(
                    TO_DATE(?, 'DD/MM/YYYY'),
                    cast(? as integer),
                    ?,
                    cast(? as double precision)
                  )"
                 date category item amount])))

(defn update!
  [expense]
  (let [{:keys [id date category item amount]} expense]
    (db/update! ["UPDATE expenses SET date = TO_DATE(?, 'DD/MM/YYYY'),
                 id_category = cast(? as integer),
                 item = ?,
                 amount = cast(? as double precision)
                 WHERE id = ?"
                 date category item amount id])))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM expenses WHERE id = cast(? as integer)" id]))
