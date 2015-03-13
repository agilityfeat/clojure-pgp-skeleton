(ns ms-users.user-system
  (:require [de.otto.tesla.system :as system]
            [ms-users.user-component :as user-component]
            [com.stuartsierra.component :as c]
            [environ.core :refer [env]])
  (:gen-class))

;; -------------------------
;; System component definition
(defn user-system [runtime-config]
  (-> (system/empty-system (merge {:name "user-system" :server-port (or (System/getenv "PORT") 8080)} runtime-config))
      (assoc :user-component
             (c/using (user-component/new-user-component) [:routes :app-status]))
      (c/system-using {:server [:user-component]})))

;; -------------------------
;; Main function
(defn -main
  "starts up the production system."
  [& args]
  (system/start-system (user-system{})))

