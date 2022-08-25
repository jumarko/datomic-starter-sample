(ns maxdatom.level-11
  "https://max-datom.com/#/03C8F2C7-89AD-4066-A9E4-665460B45003
  Not Clauses: https://docs.datomic.com/cloud/query/query-data-reference.html#not-clauses
  "
  (:require
   [datomic.api :as d]
   [maxdatom.connections :refer [db]]))


;; the original query
(comment
  (d/q '[:find  ?user-name
         :where [?user :user/first+last-name ?user-name]]
       (db))
  ;; => #{[["Miguel" "Dvd Rom"]] [["J. R." "Token"]] [["E. L." "Mainframe"]] [["Charles" "Diskens"]] [["Napoleon" "Desktop"]] [["Segfault" "Larsson"]]}
  .)

;; modify the query to return the count of user posts and the post author first+last-name
;; but only if a post does not have any `:post/dislikes`
(comment
  (d/q '[:find (count ?post) ?user-name 
         :where
         [?user :user/first+last-name ?user-name]
         [?post :post/author ?user]
         (not [?post :post/dislikes])
         ]
       (db))
  ;; => [[1 ["E. L." "Mainframe"]]]
  .)
