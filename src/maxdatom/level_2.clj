(ns maxdatom.level-2
  "See https://max-datom.com/#/E3875311-58D1-497C-9AA7-94CEF884561E."
  (:require
   [max-datom.connections :refer [db]]
   [datomic.api :as d]))

(comment

  ;; this fails because if we don't have any data in the db yet (see data.edn)
  (d/q '[:find ?v
         :where [_ :author/first+last-name ?v]]
       (db))
  .)


