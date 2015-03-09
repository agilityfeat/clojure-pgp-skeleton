(ns ms-users.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]))

;; Tries to get the Mongo URI from the environment variable
;; MONGOLAB_URI, otherwise default it to localhost
(defonce db (let [uri (get (System/getenv) "MONGOLAB_URI" "mongodb://127.0.0.1/ms_users")
                  {:keys [conn db]} (mg/connect-via-uri uri)]
              db))

(defn create-user [user]
  (mc/insert db "users" user))

(defn update-user [username public_key]
  (mc/update db "users" {:username username}
             {$set {:public_key public_key}}))

(defn get-user [username]
  (mc/find-one-as-map db "users" {:username username}))
