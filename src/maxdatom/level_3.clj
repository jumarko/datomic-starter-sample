(ns maxdatom.level-3
  "See https://max-datom.com/#/D33F1C24-7FE1-417D-880B-71EE4450917E"
  (:require
   [max-datom.connections :refer [db]]
   [datomic.api :as d]))

(comment

  ;; return entity id too
  (d/q '[:find ?e ?v
         :where [?e :author/first+last-name ?v]]
       (db))
  ;; => #{[17592186045432 ["Charles" "Diskens"]] [17592186045431 ["E. L." "Mainframe"]] [17592186045435 ["Napoleon" "Desktop"]] [17592186045436 ["Segfault" "Larsson"]] [17592186045437 ["Kim" "K"]] [17592186045430 ["J. R." "Token"]] [17592186045428 ["Miguel" "Dvd Rom"]] [17592186045433 ["Perry" "Farrell"]] [17592186045434 ["Miles" "Dyson"]]}
  .)
