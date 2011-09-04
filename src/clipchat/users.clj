(ns clipchat.rooms
  (:use [clipchat.core])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clj-http.client :as client]))

(defn create [auth-token {email :email
                          name :name
                          title :title
                          is_group_admin :is_group_admin
                          password :password
                          timezone :timezone :as opts}]
  (let [{:keys [room-id from message notify color] :or {is_group_admin 0 timezone "UTC" password ""}} opts
        url (str api-url "/users/create")
        body (client/generate-query-string opts)]
    (client/post url (setup-call-body body))))

(defn delete [auth-token {user_id :user_id :as opts}]
  (let [url (str api-url "/users/delete")
        query (assoc opts :auth_token auth-token)]
    (client/post url (setup-call-body body))))

(defn list [auth-token])

(defn show [auth-token])

(defn update [auth-token])

