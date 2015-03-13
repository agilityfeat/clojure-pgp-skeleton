(ns ms-users.user-component
  (:require
    [com.stuartsierra.component :as c]
    [de.otto.tesla.stateful.routes :as routes]
    [de.otto.status :as status]
    [compojure.core :as compojure]
    [clojure.string :as str]
    [ms-users.utils :as utils]
    [thi.ng.crypto.core :refer :all]
    [clojure.java.io :as io]
    [clojure.data.json :as json :only [write-str]]
    [ms-users.db.core :as db]))

;; -------------------------
;; DB operations
(defn register-user 
  "Validate if an username exists. If not, then proceed to register the user."
  [form]
      (if-not (db/get-user (:username form))
        (do 
            (db/create-user {:username (:username form) :public_key (:public_key form)})
            true)
        false))

(defn login-user 
  "Validate if an username exists. If not, then proceed to register the user."
  [form]
      (db/get-user (:username form)))


; This function returns the welcome page encrypted
(defn encrypt-message 
    ([form]
      (def message (str "Welcome " (:username form) "! Your registration was successful!"))
      (utils/get-encrypted-message message (:public_key form)))
  
    ([form-login public-key-db]
      (def message (str "Welcome " (:username form-login) "! Your login was successful!"))
      (utils/get-encrypted-message message public-key-db)))

(defn welcome-page-signup 
   "This functions register an user. If the username is valid, then return the welcome page encrypted"
   [body]
   (def form (utils/getmap body))
   (if (register-user form)
      ;Register ok
      (encrypt-message form)
      ;User exists
      "user-exists"
   )
)

(defn welcome-page-login 
   "This functions do the login process. If the username is valid, then return the welcome page encrypted"
   [body]
   (def form (utils/getmap body))
   (def loged-user (login-user form))
   (println (str "public key -->" (:public_key loged-user)))
   (if-not (nil? loged-user)
      ;Register ok
      (encrypt-message form (:public_key loged-user))
      ;User not exists
      "user-not-exists"
   )
)

;; -------------------------
;; Response functions
(defn signup-user 
    "json response in a sign up request"
    [self body]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str (welcome-page-signup body) )})

(defn signin-user 
    "json response in a sign in request"
    [self body]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str (welcome-page-login body) )})

;; -------------------------
;; Define the AccountManager component
(defrecord UserComponent []
  c/Lifecycle
  (start [self]
    (routes/register-routes (:routes self)
                            [(compojure/POST "/signup" {body :body} (signup-user self (slurp body)))
                             (compojure/POST "/login" {body :body} (signin-user self (slurp body)))])
    self)
  (stop [self]
    self))

(defn new-user-component [] (map->UserComponent {}))
