(ns maxdatom.level-15
  "https://max-datom.com/#/40A6D16E-2FB4-4F8E-898F-33BF6F9CC4E0"
  (:require
   [clojure.edn :as edn]
   [datomic.api :as d]
   [max-datom.connections :refer [conn db]]))

;; TODO: this is invalid UUID - it's 1 char longer than expected
;; (def transfer-id #uuid "59B9C791-74CE-4C51-A4BC-EF6D06BEE2DBA")
;;=>   java.lang.IllegalArgumentException: UUID string too large


;; Try again to load the new schema and data (accounts and transfers)
;; (it wasn't possible in the lesson 14)
(comment
  ;; transact schema
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/schema-accounts.edn")))

  ;;; get the data via queries and store them in data-users.edn
  ;; accounts - I had to pass transfer-id as input arg otherwise the web ui complained:
(d/q '[:find (pull ?e [*])
       :in $ ?transfer-id
         :where [?e :account/id]] db transfer-id)
;;=>
[[{:db/id 92358976733270,
   :account/balance 57500,
   :account/id #uuid "b7d83fb0-7ec8-4e7b-a4d8-02478f2dd5d6",
   :account/owner {:db/id 92358976733267}}]
 [{:db/id 92358976733271,
   :account/balance 2300,
   :account/id #uuid "d381dc80-c582-45eb-89e9-f6e188a71a29",
   :account/owner {:db/id 92358976733268}}]
 [{:db/id 92358976733272,
   :account/balance 8900,
   :account/id #uuid "5164b8da-2fe4-41da-a5fd-1a697be1d2dd",
   :account/owner {:db/id 92358976733269}}]]

  ;; transfers
  (d/q '[:find (pull ?e [*])
         :in $ ?transfer-id
         :where [?e :transfer/id]] db transfer-id)
  ;;=>
  [[{:db/id 92358976733273,
     :transfer/from {:db/id 92358976733272},
     ;; This is the UUID I'm looking for - notice in the `(def transfer-id ...)` there's extra "A" at the end
     :transfer/id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db",
     :transfer/to {:db/id 92358976733271},
     :transfer/amount 1000}]
   [{:db/id 92358976733274,
     :transfer/from {:db/id 92358976733272},
     :transfer/id #uuid "02125764-a1a3-4cb9-89da-7f5ef63816cd",
     :transfer/to {:db/id 92358976733271},
     :transfer/amount 850}]
   [{:db/id 92358976733275,
     :transfer/from {:db/id 92358976733272},
     :transfer/id #uuid "52636794-d94a-44b8-8012-c719d7886e17",
     :transfer/to {:db/id 92358976733271}, 
     :transfer/amount 1200}]]

  ;;; add seed data
  @(d/transact @conn (edn/read-string (slurp "src/max_datom/data-accounts.edn")))

.)




;; original query:
(comment
  (d/q '[:find (pull ?e [*])
         :in $ ?transfer-id
         :where [?e :transfer/id]] db transfer-id)
  .)


;; Pull all :transfer/from and :transfer/to data including :account/owner for a recently reported transfer
;; :transfer-id "59b9c791-74ce-4c51-a4bc-ef6d06bee2db"
(def transfer-id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db")
(comment
  (d/q '[:find (pull ?transfer [*]
                     #_[:db/id
                                :transfer/id
                                :transfer/amount
                                :transfer/to
                                :transfer/from])
  
         :in $ ?transfer-id
         :where [?transfer :transfer/id ?transfer-id]]
       (db)
       transfer-id)
    ;; => [[{:db/id 92358976733273,
  ;;       :transfer/id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db",
  ;;       :transfer/from #:db{:id 92358976733272},
  ;;       :transfer/to #:db{:id 92358976733271},
  ;;       :transfer/amount 1000}]]
  .)


(comment
  (d/q '[:find (pull ?transfer [*
                                {:transfer/from [*]
                                 :transfer/to [*]}])
         :in $ ?transfer-id
         :where [?transfer :transfer/id ?transfer-id]
                [?transfer :transfer/to ?to-account]
                [?transfer :transfer/from ?from-account]]
       (db) transfer-id)
    ;;=>
  [[{:db/id 92358976733273,
     :transfer/id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db",
     :transfer/from
     {:db/id 92358976733272,
      :account/id #uuid "5164b8da-2fe4-41da-a5fd-1a697be1d2dd",
      :account/balance 8900,
      :account/owner #:db{:id 92358976733269}},
     :transfer/to
     {:db/id 92358976733271,
      :account/id #uuid "d381dc80-c582-45eb-89e9-f6e188a71a29",
      :account/balance 2300,
      :account/owner #:db{:id 92358976733268}},
     :transfer/amount 1000}]]

  ;; ... the tutorial accepts the query but it shows this result instead:
  ;; - notice how `:account/owner` is expanded including `:user/...` attributes
  ;; => that is probably because I have incomplete data in my local DB??
  [[{:db/id 92358976733273,
     :transfer/from
     {:db/id 92358976733272,
      :account/balance 8900,
      :account/id #uuid "5164b8da-2fe4-41da-a5fd-1a697be1d2dd",
      :account/owner
      {:db/id 92358976733269,
       :user/first-name "Sonny",
       :user/id #uuid "afb83133-3a2e-40ce-91f8-2de4f61361de",
       :user/last-name "Diskon"}},
     :transfer/id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db",
     :transfer/to
     {:db/id 92358976733271,
      :account/balance 2300,
      :account/id #uuid "d381dc80-c582-45eb-89e9-f6e188a71a29",
      :account/owner
      {:db/id 92358976733268,
       :user/first-name "Muhammad",
       :user/id #uuid "bfe00de4-bc19-4395-ba3b-2384ecf1a569",
       :user/last-name "CD"}}, 
     :transfer/amount 1000}]]

  .)
