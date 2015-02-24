(ns ms-users.signup-page
  (:require
    [com.stuartsierra.component :as c]
    [de.otto.tesla.stateful.routes :as routes]
    [de.otto.status :as status]
    [de.otto.tesla.stateful.app-status :as app-status]
    [compojure.core :as compojure]
    [clojure.data.json :as json :only [write-str]]))

(defn signup-user [self body]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str body)})



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
