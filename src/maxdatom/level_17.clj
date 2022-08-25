(ns maxdatom.level-17
  "https://max-datom.com/#/6B32A937-961F-4D83-B192-EAD8CFD97E64
  `d/as-of` allows us to query the database at a specific point in time.
  See https://docs.datomic.com/cloud/tutorial/history.html#as-of-query"
  (:require
   [datomic.api :as d]
   [maxdatom.connections :refer [db]]))

;; (def transfer-id #uuid "59B9C791-74CE-4C51-A4BC-EF6D06BEE2DB")
(def transfer-id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db")


;; Tip: from the as-of docs, you can get list of recent transactions:
;; - https://docs.datomic.com/cloud/tutorial/history.html#as-of-query
(comment
  (d/q '[:find (max 3 ?tx)
        :where [?tx :db/txInstant _]]
      (db))
  ;; => [[[13194139534319 13194139534318 13194139534317]]]

  ;; ... this is funny - their transaction ID is _almost_ the same
  ;; their: 13194139533319
  ;; mine:  13194139534319
  ;; - the only difference is the fourth to last digit (3 vs 4)

  .)



;; original query:
(comment
  (d/q '[:find  ?transfer
         :where [?transfer :transfer/id _]] db)
  .)


;; Task: Pull the all the data including the :account/owner
;; for :transfer/id #uuid "59b9c791-74ce-4c51-a4bc-ef6d06bee2db"
;; as-of transaction id 13194139533319.
(comment
  ;; Note: this is an interesting usage of `@` to get the value de-referenced ;; - this evaluates to the DB, not the var.
  ;; Also notice that my transaction id is a bit different: 13194139534319
  @(def db-as-of (d/as-of (db) 13194139534319))

  (d/q '[:find (pull ?transfer [* {:transfer/to [*] :transfer/from [*]}])
         :in $ ?transfer-id
         :where [?transfer :transfer/id ?transfer-id]]
       db-as-of transfer-id)
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


  ;; expected response
  [[{:db/id 92358976733270,
     :account/balance 57500,
     :account/id #uuid "b7d83fb0-7ec8-4e7b-a4d8-02478f2dd5d6",
     :account/owner
     {:db/id 92358976733267,
      :user/first-name "Spammy",
      :user/id #uuid "632c815c-1591-45a9-b974-95e39605f7d8",
      :user/last-name "The Bull"}}]]

  ;; based on the expected response, it seems they want user data too
  ;; but only for `:transfer/to` attribute
  (d/q '[:find (pull ?account [* {:account/owner [*]}])
         :in $ ?transfer-id
         :where [?transfer :transfer/id ?transfer-id]
         [?transfer :transfer/to ?account]]
       db-as-of transfer-id)
  ;; =>
 [[{:db/id 92358976733271,
     :account/id #uuid "d381dc80-c582-45eb-89e9-f6e188a71a29",
     :account/balance 2300,
     :account/owner
     {:db/id 92358976733268,
      :user/id #uuid "bfe00de4-bc19-4395-ba3b-2384ecf1a569",
      :user/first-name "Muhammad",
      :user/last-name "CD",
      :user/first+last-name ["Muhammad" "CD"]}}]]
    .)

