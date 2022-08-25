(ns maxdatom.level-6
  "Pull syntax.
  See https://max-datom.com/#/32466EAD-9673-4D5B-9393-928B57926065
  and https://docs.datomic.com/cloud/query/query-pull.html#reverse-lookup"
  (:require
   [maxdatom.connections :refer [db]]
   [datomic.api :as d]))

;; reverse navigation example with _
(comment
  [:find (pull ?e [:farm/_workers])
   :where [?e :worker/id #uuid "DC122EEA-5D92-45CE-98ED-874AFA648CEE"]]
  .)

  ;; the docs: https://docs.datomic.com/cloud/query/query-pull.html#reverse-lookup


;; Modify the query to return any entities referencing the id in the query as a value
;; for :book/author using the underscore prefix syntax.
(comment
  (d/q '[:find (pull ?e [:book/_author :author/first-name :author/last-name])
         :where [?e :author/id #uuid "14E86ACF-000B-463E-90CB-CEA0927A97DA"]]
       (db))
  ;; => [[{:author/first-name "Napoleon", :author/last-name "Desktop", :book/_author [#:db{:id 87960930222175} #:db{:id 87960930222176}]}]]

  
  .)
