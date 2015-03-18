(ns ms-users.utils
  (:require
    [ring.util.codec :as codec]
    [clojure.walk :as walk]
    [clj-pgp.message :as pgp-msg]
    [clojure.string :as str]
    [thi.ng.crypto.core :refer :all]
    [clojure.java.io :as io]))


(defn getmap 
  "converts form-data in a map"
  [form-params]
  (walk/keywordize-keys
    (codec/form-decode form-params)))

(defn format-public-key
  "Receive a Public Key in ASCII format, and return an encoded public key format"
  [pbk]
   (def tmp_file (str "pbk" (rand-int 30000) ".tmp")) 
   (spit tmp_file pbk)
   (def public_key (public-key tmp_file))
   (io/delete-file tmp_file)
   public_key)

(defn get-encrypted-message 
  "Returns a PGP message encrypted using a public key"
  [message public_key]
  	(def formatted-public-key (format-public-key public_key))
    (pgp-msg/encrypt
              message formatted-public-key
              :format :utf8
              :cipher :aes-256
              :compress :zip
              :armor true))
