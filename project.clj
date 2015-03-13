(defproject ms-users "1.0.0-SNAPSHOT"
            :description "basic microservice."
            :url "https://github.com/ggoldens/ms_users"
            :license { :name "Apache License 2.0"
                       :url "http://www.apache.org/license/LICENSE-2.0.html"}
            :scm { :name "git"
 	          :url "https://github.com/ggoldens/ms_users"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [com.stuartsierra/component "0.2.2"]
                           [environ "1.0.0"]
                           [de.otto/tesla-microservice "0.1.7"]
                           [ring/ring-codec "1.0.0"]
                           [mvxcvi/clj-pgp "0.8.0"]
                           [thi.ng/crypto "0.1.0-SNAPSHOT"]
                           [com.novemberain/monger "2.0.0"]]


            :plugins [[lein-environ "1.0.0"]]
            :main ms-users.user-system
            :clean-targets [:target-path :compile-path "target"]
            :source-paths ["src"]
            :uberjar-name "ms-users.jar"                 
            :test-paths ["test" "test-resources"]
            :min-lein-version "2.0.0")
