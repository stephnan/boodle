(ns boodle.api.schemas.transaction
  (:require [schema.core :as schema]))

(def Response
  {:id schema/Int
   :id-aim schema/Int
   :type schema/Int
   :item schema/Str
   :amount (schema/pred double?)
   :date schema/Inst})

(schema/defschema Body
  {:id-aim schema/Int
   :type schema/Int
   :item schema/Str
   :amount (schema/pred double?)})
