(ns quiz-reagent.database
  (:require [yesql.core :refer [defqueries]]))

(def uri (new java.net.URI (System/getenv "CLEARDB_DATABASE_URL")))

(defn host [uri]
  (.getHost uri))

(defn path [uri]
  (.getPath uri))

(defn user-info [uri]
  (.getUserInfo uri))

(defn user [uri]
  (first (.split (user-info uri) ":")))

(defn password [uri]
  (second (.split (user-info uri) ":")))

; local connection:
#_(def db-spec {:classname "com.mysql.jdbc.Driver"
                :subprotocol "mysql"
                :subname "//localhost:3306/quiz"
                :user "root"
                :password "root"})

(def db-spec {:classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname (str "//" (host uri) (path uri))
              :user (user uri)
              :password (password uri)})

(defqueries "quiz_reagent/queries.sql")

(defn fetch-stats-by-user-id [user-id]
  (first (stats-by-user-id db-spec user-id)))

(defn update-stats! [user-id won lost]
  ; CAUTION: the order of the parameters is significant, not their name!!!
  (update-stats-stmt! db-spec won lost user-id))

(defn- fetch-users []
  (let [users (fetch-users-stmt db-spec)]
    (map (fn [user] (assoc user :username (:user_id user))) users)
    ; TODO: dissoc key :user_id
    ))

(defn auth-map []
  (zipmap (map (fn [user] (:user_id user)) (fetch-users)) (fetch-users)))

(defn insert-user! [user-id password]
  (insert-user-stmt! db-spec user-id password))

; To run the queries from the REPL:
;
; run sql script 'migration/migration.sql' and insert a user with user-id "1"
; lein repl
; (use 'quiz-clj.database :reload)
; (stats-by-user-id db-spec "1")
