(ns maxdatom.level-1
  "See https://max-datom.com/#/9295D862-CAE9-455C-9485-67DBC8018BDF"
  (:require
   [datomic.api :as d]
   [max-datom.connections :refer [db]]))


(comment
  ;; this will fail if we don't have any data
  (d/q '[:find ?v
         :where [_ :author/first+last-name ?v]]
       (db))
  ;; => #{[["Miguel" "Dvd Rom"]] [["J. R." "Token"]] [["E. L." "Mainframe"]] [["Charles" "Diskens"]] [["Perry" "Farrell"]] [["Miles" "Dyson"]] [["Napoleon" "Desktop"]] [["Segfault" "Larsson"]] [["Kim" "K"]]}
  .)


