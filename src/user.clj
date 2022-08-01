(ns user
  (:require [datomic.api :as d]))

;;; experiments with datomic: https://docs.datomic.com/on-prem/getting-started/connect-to-a-database.html
(def db-uri "datomic:dev://localhost:4334/hello")

(comment

  (d/create-database db-uri)

  (def conn (d/connect db-uri))

  ;; try adding a new entity
  @(d/transact conn [{:db/doc "Hello world"}])
  ;; => {:db-before datomic.db.Db@fa2ac065, :db-after datomic.db.Db@82f8dc39, :tx-data [#datom[13194139534318 50 #inst "2022-08-01T05:54:56.725-00:00" 13194139534318 true] #datom[17592186045423 62 "Hello world" 13194139534318 true]], :tempids {-9223301668109598140 17592186045423}}
  .)

;; schema: https://docs.datomic.com/on-prem/getting-started/transact-schema.html
(def movie-schema [{:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}])
(comment
  @(d/transact conn movie-schema)
  .)

;; Transact data: https://docs.datomic.com/on-prem/getting-started/transact-data.html
(def first-movies [{:movie/title "The Goonies"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Commando"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Repo Man"
                    :movie/genre "punk dystopia"
                    :movie/release-year 1984}])
(comment
  @(d/transact conn first-movies)
  ;; => [nREPL-session-ebb02994-02be-4422-8bbc-d1a91a7ffbe3] INFO datomic.peer - {:event :peer/transact, :uuid #uuid "62e77041-c3ea-4f78-bd59-9660c4b37d28", :phase :start, :pid 14589, :tid 18}
  ;;    {:db-before datomic.db.Db@f341dbd3,
  ;;     :db-after datomic.db.Db@46f30ccd,
  ;;     :tx-data
  ;;     [#datom[13194139534349 50 #inst "2022-08-01T06:18:41.903-00:00" 13194139534349 true] #datom[17592186045454 72 "The Goonies" 13194139534349 true] #datom[17592186045454 73 "action/adventure" 13194139534349 true] #datom[17592186045454 74 1985 13194139534349 true] #datom[17592186045455 72 "Commando" 13194139534349 true] #datom[17592186045455 73 "action/adventure" 13194139534349 true] #datom[17592186045455 74 1985 13194139534349 true] #datom[17592186045456 72 "Repo Man" 13194139534349 true] #datom[17592186045456 73 "punk dystopia" 13194139534349 true] #datom[17592186045456 74 1984 13194139534349 true]],
  ;;     :tempids
  ;;     {-9223301668109598115 17592186045454,
  ;;      -9223301668109598114 17592186045455,
  ;;      -9223301668109598113 17592186045456}}
  .)


;; Query the data: https://docs.datomic.com/on-prem/getting-started/query-the-data.html
;; A movie is anything that has an associated :movie/title attribute
(def all-movies-q '[:find ?e
                    :where [?e :movie/title]])
(comment
  (def db (d/db conn))
  (take 5 (d/q all-movies-q db))
  ;; => ([17592186045442] [17592186045443] [17592186045444] [17592186045446] [17592186045447])
  (count (d/q all-movies-q db))
  ;; => 24

  ;; try to enter the same movies again:
  @(d/transact conn first-movies)
  (count (d/q all-movies-q db))
  ;; => 24
  ;; nothing changed?
  ;; ... but try with a fresh db
  (def db (d/db conn))
  (count (d/q all-movies-q db))
  ;; => 27

  ;; find all the titles
  (def all-titles-q '[:find ?movie-title
                      ;; underscore means we are not interested in entity IDs
                      :where [_ :movie/title ?movie-title]])
  (d/q all-titles-q db)
  ;; => #{["Commando"] ["The Goonies"] ["Repo Man"]}

  ;; find all the titles releases in 1985
  (def titles-from-1985 '[:find ?title
                          :where [?e :movie/title ?title]
                                 [?e :movie/release-year 1985]])
  (d/q titles-from-1985 db)
  ;; => #{["Commando"] ["The Goonies"]}

  ;; ... and get all the attributes for 1985 movies
  (def all-data-from-1985 '[:find ?title ?year ?genre
                            :where [?e :movie/title ?title]
                                   [?e :movie/release-year ?year]
                                   [?e :movie/genre ?genre]
                                   [?e :movie/release-year 1985]])
  (d/q all-data-from-1985 db)
  ;; => #{["The Goonies" 1985 "action/adventure"] ["Commando" 1985 "action/adventure"]}

  .)

