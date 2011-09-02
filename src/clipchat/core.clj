(ns clipchat.core
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clj-http.client :as client]))


(defn list-rooms [auth-token]
  " Returns a vector of maps defining the rooms available. https://www.hipchat.com/docs/api/method/rooms/list "
  (:rooms
   (read-json
    (:body
     (client/get "http://api.hipchat.com/v1/rooms/list" { :query-params
                                                         {"format" "json"
                                                          "auth_token" auth-token}})))))

