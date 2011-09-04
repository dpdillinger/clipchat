(ns clipchat.rooms
  (:use [clipchat.core :only [api-url urlencoded]])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clj-http.client :as client]))

(defn create [auth-token {email :email
                          name :name
                          title :title
                          is_group_admin :is_group_admin
                          password :password
                          timezone :timezone}]
  (let [{:keys [room-id from message notify color] :or {is_group_admin 0 timezone "UTC" password ""}} opts
        url (str api-url "/users/create")
        body (client/generate-query-string )]
    (client/post url {:content-type urlencoded
                      :accept "json"
                      :body body
                      })))

(defn delete [auth-token])

(defn list [auth-token])

(defn show [auth-token])

(defn update [auth-token])

