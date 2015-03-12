(ns ms-users.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]))

;; -------------------------
;; Connection to DB
(defonce db (let [uri (get (System/getenv) "MONGOLAB_URI" "mongodb://127.0.0.1/ms_users")
                  {:keys [conn db]} (mg/connect-via-uri uri)]
              db))

;; -------------------------
;; User's functions
(defn create-user [user]
  (mc/insert db "users" user))

(defn get-user [username]
  (mc/find-one-as-map db "users" {:username username}))
