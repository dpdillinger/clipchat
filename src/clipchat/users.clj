(ns clipchat.rooms
  (:require [clj-http.client :as client]
            [clipchat.core :refer :all]))

(defn create [auth-token {email :email
                          name :name
                          title :title
                          is_group_admin :is_group_admin
                          password :password
                          timezone :timezone :as opts}]
  (let [{:keys [email name title is_group_admin password timezone]
         :or {is_group_admin 0 timezone "UTC" password ""}} opts
        url (str api-url "/users/create")
        body (client/generate-query-string
              (conj opts {:format "json" :auth_token auth-token}))]
    (client/post url (setup-call-body body))))

(defn update [auth-token {user_id :user_id
                           email :email
                           name :name
                           title :title
                           is_group_admin :is_group_admin
                           password :password
                           timezone :timezone :as opts}]
  (let [{:keys [user_id email name title is_group_admin password timezone]
         :or {is_group_admin 0 timezone "UTC"}} opts
        url (str api-url "/users/update")
        body (client/generate-query-string
              (conj opts {:format "json" :auth_token auth-token}))]
    (client/post url (setup-call-body body))))

(defn delete [auth-token {user_id :user_id :as opts}]
  (let [url (str api-url "/users/delete")
        body (assoc opts :auth_token auth-token)]
    (client/post url (setup-call-body body))))

(defn list [auth-token]
  (let [url (str api-url "/users/list")
        query {:format "json" :auth_token auth-token}]
    (client/get url {:query-params query})))

(defn show [auth-token {user_id :user_id :as opts}]
  (let [url (str api-url "/users/list")
        query (conj opts {:format "json" :auth_token auth-token})]
    (client/get url {:query-params query})))



