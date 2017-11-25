(ns boodle.api.schemas.aim
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :name schema/Str
   :target schema/Num
   :achieved schema/Bool})

(def Transactions
  {:aim schema/Str
   :target schema/Num
   :saved schema/Num
   :amount schema/Num})

(schema/defschema Body
  {:name schema/Str
   :target (schema/pred double?)
   :achieved schema/Bool})
