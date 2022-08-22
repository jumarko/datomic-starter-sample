(ns maxdatom.level-5
  "Pull syntax.
  See https://max-datom.com/#/FE78C28B-3759-4AA1-94A0-CC8057D2255A"
  (:require
   [max-datom.connections :refer [db]]
   [datomic.api :as d]))

;; list all the books and authors in the db to include them in data.edn
(comment
  (d/q '[:find (pull ?e [*])
         :where [?e :book/author]]
       (db))

  (d/q '[:find (pull ?e [*])
         :where [?e :author/first-name]]
       (db))

  .)

