(ns boodle.model.savings
  (:require [boodle.services.postgresql :as db]
            [boodle.utils.dates :as ud]))

(defn select-all
  []
  (db/query ["SELECT * FROM savings ORDER BY date DESC"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM savings WHERE id = cast(? as integer)" id]))

(defn select-by-item
  [savings-item]
  (db/query ["SELECT * FROM savings WHERE item = ?" savings-item]))

(defn insert!
  [saving]
  (let [{:keys [item amount]} saving
        today (ud/format-date (java.util.Date.))]
    (db/update! ["INSERT INTO savings(item, amount, date)
                  VALUES(?, cast(? as double precision),
                         TO_DATE(?, 'DD/MM/YYYY'))"
                 item amount today])))

(defn update!
  [saving]
  (let [{:keys [id item amount]} saving]
    (db/update! ["UPDATE savings SET item = ?,
                         amount = cast(? as double precision)
                  WHERE id = cast(? as integer)"
                 item amount id])))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM savings WHERE id = cast(? as integer)" id]))
