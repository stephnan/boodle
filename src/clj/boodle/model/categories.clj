(ns boodle.model.categories
  (:require [boodle.services.postgresql :as db]))

(defn select-all
  []
  (db/query ["SELECT * FROM categories"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM categories WHERE id = cast(? as integer)" id]))

(defn select-by-name
  [category-name]
  (db/query ["SELECT * FROM categories WHERE name = ?" category-name]))

(defn insert!
  [category]
  (let [{:keys [name monthly-budget]} category]
    (db/update! ["INSERT INTO categories(name, monthly_budget)
                  VALUES( ?, cast(? as double precision))"
                 name monthly-budget])))

(defn update!
  [category]
  (let [{:keys [id name monthly-budget]} category]
    (db/update! ["UPDATE categories SET name = ?, monthly_budget = ?
                  WHERE id = cast(? as integer)"
                 name monthly-budget id])))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM categories WHERE id = cast(? as integer)" id]))
