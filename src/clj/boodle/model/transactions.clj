(ns boodle.model.transactions
  (:require [boodle.services.postgresql :as db]))

(defn select-all
  []
  (db/query ["SELECT * FROM transactions"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM transactions WHERE id = cast(? as integer)" id]))

(defn select-by-item
  [transaction-item]
  (db/query ["SELECT * FROM transactions WHERE item = ?" transaction-item]))

(defn select-by-aim
  [id-aim]
  (db/query ["SELECT t.id, t.id_aim, a.name as aim, a.target, t.item,
              t.amount, t.date FROM transactions t
              INNER JOIN aims a ON t.id_aim = a.id
              WHERE t.id_aim = cast(? as integer)
              ORDER BY date DESC"
             id-aim]))

(defn insert!
  [transaction]
  (let [{:keys [id-aim item amount]} transaction
        today (db/format-date (java.util.Date.))]
    (db/update! ["INSERT INTO transactions(id_aim, item, amount, date)
                  VALUES(?, ?, ?, cast(? as double precision),
                         TO_DATE(?, 'DD/MM/YYYY'))"
                 id-aim item amount today])))

(defn update!
  [transaction]
  (let [{:keys [id id-aim item amount]} transaction]
    (db/update! ["UPDATE transactions SET id_aim = ?, item = ?,
                         amount = cast(? as double precision)
                  WHERE id = cast(? as integer)"
                 id-aim item amount])))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM transactions WHERE id = cast(? as integer)" id]))
