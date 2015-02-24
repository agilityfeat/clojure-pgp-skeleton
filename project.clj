(defproject de.otto/tesla-microservice "0.1.6"
            :description "basic microservice."
            :url "https://github.com/otto-de/tesla-microservice"
            :license { :name "Apache License 2.0"
                       :url "http://www.apache.org/license/LICENSE-2.0.html"}
            :scm { :name "git"
 	           :url "https://github.com/otto-de/tesla-microservice"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [com.stuartsierra/component "0.2.2"]
                           [environ "1.0.0"]
                           [clj-time "0.8.0"]
                           [de.otto/tesla-microservice "0.1.6"]



                           ;; testing
                           [ring-mock "0.1.5"]
                           [http-kit.fake "0.2.1"]]


            :plugins [[lein-marginalia "0.8.0"]
                      [lein-environ "1.0.0"]]
            :main ^:skip-aot ms-users.account-system
            :aot [de.otto.tesla.util.escapingmessageconverter]
            :clean-targets [:target-path :compile-path "target"]
            :source-paths ["src" "example/src"]
            :java-source-paths ["src/java"]
            :test-selectors {:default     (constantly true)
                             :integration :integration
                             :unit        :unit
                             :all         (constantly true)}
            :profiles {:test {:aot [de.otto.tesla.util.escapingmessageconverter]
                              :env {:metering-reporter "console"
                                    :import-products   "false"
                                    :server-port       "9991"
                                    :cache-dir         "/tmp"}}
                       :meta {:env {:app-name :tesla-meta}}
                       :uberjar {:aot :all}}
            :test-paths ["test" "test-resources"]

            :min-lein-version "2.0.0")
