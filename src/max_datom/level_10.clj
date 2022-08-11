(ns maxdatom.level-10
  "https://max-datom.com/#/DB612D03-9AF7-49B7-98B5-4C77ADE029D2
  Aggregate functions: https://docs.datomic.com/cloud/query/query-data-reference.html#aggregates"
  (:require
   [clojure.edn :as edn]
   [datomic.api :as d]
   [max-datom.connections :refer [conn db]]))

;; first, load the new schema and data - they are now using 'user' entities
(comment
  ;; transact schema
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/schema-users.edn")))

  ;;; get the data via queries and store them in data-users.edn

  ;; users
  (d/q '[:find (pull ?e [*])
         :where [?e :user/id]]
       (db))
  ;;=>
  [[{:db/id 96757023244375,
   :user/id #uuid "1b341635-be22-4acc-ae5b-d81d8b1b7678",
   :user/first-name "E. L.",
   :user/last-name "Mainframe",
   :user/first+last-name ["E. L." "Mainframe"]}]
 [{:db/id 96757023244376,
   :user/id #uuid "0955edf7-ff8f-4ec2-afb2-380e7e5d48d7",
   :user/first-name "Segfault",
   :user/last-name "Larsson",
   :user/first+last-name ["Segfault" "Larsson"]}]
 [{:db/id 96757023244377,
   :user/id #uuid "14e86acf-000b-463e-90cb-cea0927a97da",
   :user/first-name "Napoleon",
   :user/last-name "Desktop",
   :user/first+last-name ["Napoleon" "Desktop"]}]
 [{:db/id 96757023244378,
   :user/id #uuid "bbc10120-7ff8-472a-9715-976ea3cba9af",
   :user/first-name "J. R.",
   :user/last-name "Token",
   :user/first+last-name ["J. R." "Token"]}]
 [{:db/id 96757023244379,
   :user/id #uuid "35636b79-ee46-4447-8aa7-3f0fb351c45c",
   :user/first-name "Charles",
   :user/last-name "Diskens",
   :user/first+last-name ["Charles" "Diskens"]}]
 [{:db/id 96757023244380,
   :user/id #uuid "b7761785-79f9-49fa-97af-13b4f5c2bcc2",
   :user/first-name "Miguel",
   :user/last-name "Dvd Rom",
   :user/first+last-name ["Miguel" "Dvd Rom"]}]]

  ;; groups
  (d/q '[:find (pull ?e [*])
         :where [?e :group/id]]
       (db))
  ;;=>
  [[{:db/id 96757023244385,
     :group/id #uuid "42ffdd27-d88a-4361-a444-4af46b598b23",
     :group/name "Group1",
     :group/posts [{:db/id 96757023244381} {:db/id 96757023244382}]}]
   [{:db/id 96757023244386,
     :group/id #uuid "3c3d62dc-9dc1-462d-966c-0d69b8b38d96",
     :group/name "Group2",
     :group/posts [{:db/id 96757023244383} {:db/id 96757023244384}]}]]

  ;; posts
  (d/q '[:find (pull ?e [*])
         :where [?e :post/id]]
       (db))
  ;;=>
  [[{:db/id 96757023244381,
   :post/id #uuid "2fd9f72e-5cab-4a63-91e9-2cc3fbfc5f91",
   :post/author {:db/id 96757023244375},
   :post/comments
   [{:db/id 96757023244375}
    {:db/id 96757023244376}
    {:db/id 96757023244377}],
   :post/dislikes [{:db/id 96757023244376}],
   :post/message-uri
   "https://s3.amazonaws.com/computer-conscience.com/top-trends/post78.txt"}]
 [{:db/id 96757023244382,
   :post/id #uuid "d15fbe75-0069-4395-b1b1-595b501e97d3",
   :post/author {:db/id 96757023244375},
   :post/comments [{:db/id 96757023244375} {:db/id 96757023244376}],
   :post/dislikes [{:db/id 96757023244376}],
   :post/likes [{:db/id 96757023244377}],
   :post/message-uri
   "https://s3.amazonaws.com/computer-conscience.com/top-trends/post79.txt"}]
 [{:db/id 96757023244383,
   :post/id #uuid "0c4bdbb9-a200-428a-a0c0-cb171a3fe436",
   :post/author {:db/id 96757023244375},
   :post/likes [{:db/id 96757023244377}],
   :post/message-uri
   "https://s3.amazonaws.com/computer-conscience.com/top-trends/post80.txt"}]
 [{:db/id 96757023244384,
   :post/id #uuid "79550bdc-52ad-4133-a80d-97dd7f0d9e01",
   :post/author {:db/id 96757023244376},
   :post/comments [{:db/id 96757023244375} {:db/id 96757023244376}],
   :post/dislikes [{:db/id 96757023244376}],
   :post/likes [{:db/id 96757023244377}],
   :post/message-uri
   "https://s3.amazonaws.com/computer-conscience.com/top-trends/post81.txt"}]]

  ;;; add seed data
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/data-users.edn")))

  .)


;; the original query
(comment
  (d/q '[:find  ?user-name
         :where [?user :user/id #uuid "1B341635-BE22-4ACC-AE5B-D81D8B1B7678"]
         [?user :user/first+last-name ?user-name]]
       (db))
  ;; => #{[["E. L." "Mainframe"]]}

  .)

;; modify the query to return first+last-name and count of :post/author
(comment
  (d/q '[:find  ?user-name (count ?post-author)
         :where
         [?user :user/id #uuid "1B341635-BE22-4ACC-AE5B-D81D8B1B7678"]
         [?user :user/first+last-name ?user-name]
         [?post :post/author ?user]
         [?post :post/author ?post-author]
         ]
       (db))
;; => [[["E. L." "Mainframe"] 1]]

  ;; debugging - find all the posts for given author:
  (d/q '[:find  ?post-id
         :where
         [?user :user/id #uuid "1B341635-BE22-4ACC-AE5B-D81D8B1B7678"]
         [?post :post/author ?user]
         [?post :post/id ?post-id]]
       (db))
;; =>
  #{[#uuid "0c4bdbb9-a200-428a-a0c0-cb171a3fe436"]
    [#uuid "2fd9f72e-5cab-4a63-91e9-2cc3fbfc5f91"]
    [#uuid "d15fbe75-0069-4395-b1b1-595b501e97d3"]}

  ;; should I simply count posts instead of author??
  (d/q '[:find  ?user-name (count ?post)
         :where
         [?user :user/id #uuid "1B341635-BE22-4ACC-AE5B-D81D8B1B7678"]
         [?user :user/first+last-name ?user-name]
         [?post :post/author ?user]]
       (db))
    ;; => [[["E. L." "Mainframe"] 3]]

  .)
