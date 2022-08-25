(ns maxdatom.level-16
  "https://max-datom.com/#/76F1C863-DF1A-4B3A-9FE3-AE3A7CD80064.

  By leveraging a history database you can query all assertions and retractions regardless of when they occurred.
  See https://docs.datomic.com/cloud/tutorial/history.html#history-query"
  (:require
   [datomic.api :as d]
   [maxdatom.connections :refer [db]]))

;; again, this is invalid UUID - it's 1 char longer than expected
;; (def transfer-id #uuid "59B9C791-74CE-4C51-A4BC-EF6D06BEE2DBA")

(def transfer-id #uuid "59B9C791-74CE-4C51-A4BC-EF6D06BEE2DB")

(def history-db (d/history (db)))


;; original query:
(comment
  (d/q '[:find  ?transfer
         :where [?transfer :transfer/id _]] history-db)
  .)


;; Task: Use the history db to query e, v, t and op for the :transfer/to attribute
;; associated with :transfer/id #uuid "59B9C791-74CE-4C51-A4BC-EF6D06BEE2DB"
(comment

  (d/q '[:find  ?transfer ?v ?tx ?op
         :in $ ?transfer-id
         :where [?transfer :transfer/id ?transfer-id]
         [?transfer :transfer/to ?v ?tx ?op]]
       history-db transfer-id)
  ;; => #{[92358976733273 92358976733271 13194139534318 true]}

  ;; In the tutorial, they say my query is correct but present this result:
  [[92358976733273 92358976733270 13194139533319 true]
   [92358976733273 92358976733271 13194139533320 true]
   [92358976733273 92358976733270 13194139533320 false]]
  .)
