(ns ms-users.signup-page
  (:require
    [com.stuartsierra.component :as c]
    [de.otto.tesla.stateful.routes :as routes]
    [de.otto.status :as status]
    [de.otto.tesla.stateful.app-status :as app-status]
    [compojure.core :as compojure]
    [ring.util.codec :as codec]
    [clojure.walk :as walk]
    [clj-pgp.core :as pgp]
    [clj-pgp.message :as pgp-msg]
    [clojure.string :as str]
    [thi.ng.crypto.core :refer :all]
    [clojure.data.json :as json :only [write-str]]))

(defn getmap 
  "converts form-data in a map"
  [form-params]
  (walk/keywordize-keys
    (codec/form-decode form-params)))

(defn welcome-page 
   "return the welcome page encrypted with PGP"
   [body]
   (def form (getmap body))
   (def message (str "Welcome " (:username form) "! Your registration was successful!"))
   (spit "public_key.temp" (:public_key form))
   (def public_key (public-key "public_key.temp"))
   (def encrypted-message
    (pgp-msg/encrypt
      message public_key
      :format :utf8
      :cipher :aes-256
      :compress :zip
      :armor true))
   encrypted-message)

(defn signup-user [self body]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str (welcome-page body) )})



(defrecord SignupPage []
  c/Lifecycle
  (start [self]
    (routes/register-routes (:routes self)
                            [(compojure/POST "/signup" {body :body} (signup-user self (slurp body)))])
    (app-status/register-status-fun (:app-status self)
      (fn [] (status/status-detail :example-page :ok "page is always fine")))
    self)
  (stop [self]
    self))

(defn new-signup-page [] (map->SignupPage {}))
