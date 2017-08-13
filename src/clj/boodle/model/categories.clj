(ns boodle.model.categories
  (:require [boodle.services.postgresql :as db]))

(defn select-all
  []
  (db/query ["SELECT * FROM categories"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM categories WHERE id = ?" id]))

(defn select-by-name
  [category-name]
  (db/query ["SELECT * FROM categories WHERE name = ?" category-name]))

(defn insert!
  [params]
  (db/insert! :categories params))

(defn update!
  [id category-name monthly-budget]
  (db/update!
   ["UPDATE categories SET name = ?, monthly_budget = ? WHERE id = ?"
    category-name monthly-budget id]))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM categories WHERE id = ?" id]))
