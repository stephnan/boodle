(ns boodle.api.schemas.expense
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :date schema/Inst
   :category schema/Int
   :item schema/Str
   :amount (schema/pred double?)})

(schema/defschema Body
  {:date schema/Inst
   :category schema/Int
   :item schema/Str
   :amount (schema/pred double?)})
