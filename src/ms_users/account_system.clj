(ns ms-users.account-system
  (:require [de.otto.tesla.system :as system]
            [ms-users.signup-page :as signup-page]
            [com.stuartsierra.component :as c]
            [environ.core :refer [env]])
  (:gen-class))


(defn account-system [runtime-config]
  (-> (system/empty-system (merge {:name "account-system"} runtime-config))
      (assoc :signup-page
             (c/using (signup-page/new-signup-page) [:routes :app-status]))
      (c/system-using {:server [:signup-page]})))

(defn create-default-properties []
  (def port (or (System/getenv "PORT") "17995"))
  (def text (str " status.url=/status
              health.url=/health

              ### metering
              metering.reporter=console
              console.interval.seconds=100

              ### activate graphite like this:
              #metering.reporter=graphite
              #graphite.host=localhost
              #graphite.port=2003
              #graphite.prefix=my-app-prefix
              #graphite.interval.seconds=60

              server.port=" port))
    (spit "resources/default.properties" text)
  )

(defn -main
  "starts up the production system."
  [& args]
  (create-default-properties)
  (system/start-system (account-system{})))

