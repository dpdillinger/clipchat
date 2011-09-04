(ns clipchat.rooms
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clj-http.client :as client]))

(def api-url "http://api.hipchat.com/v1")

(defn list-rooms [auth-token]
  " Returns a vector of maps defining the rooms available. https://www.hipchat.com/docs/api/method/rooms/list
    'list' means something in clojure, so the name is 'list-rooms' instead."
  (:rooms
   (read-json
    (:body
     (client/get (str api-url "/rooms/list") { :query-params
                                              { "format" "json"
                                                "auth_token" auth-token}})))))

(defn history [auth-token room-id date]
  " Returns the history of messages to the specified room as a vector of maps.
    https://www.hipchat.com/docs/api/method/rooms/history "
  (:messages (read-json
              (:body
               (client/get (str api-url "/rooms/history") { :query-params
                                                           { "format" "json"
                                                             "auth_token" auth-token
                                                             "room_id" room-id
                                                             "date" date }})))))


(defn message [auth-token { room-id :room-id
                                from :from
                                message :message
                                notify :notify
                                color :color :as opts }]
  " XXX: not working yet
    room-id from message notify color"
  (map (fn [v] (if (nil? (get opts v)) (throw (java.lang.Exception. (str "Missing argument: " v))))) [:room-id :from :message])
  (let [{:keys [room-id from message notify color] :or {notify 0 color "yellow"}} opts
        url (str api-url "/rooms/message?" (client/generate-query-string {"format" "json" "auth_token" auth-token}) )
        body (client/generate-query-string { "format" "json"
                                             "auth_token" auth-token
                                            "room_id" room-id
                                             "from" from
                                             "message" message
                                             "notify" notify
                                             "color" color})]
    (:status
     (read-json
      (:body
       (client/post (str api-url "/rooms/message") {:content-type "application/x-www-form-urlencoded"
                                                    :accept "application/json"
                                                    :body body}))))))

(defn message-get [auth-token { room-id :room-id
                                from :from
                                message :message
                                notify :notify
                                color :color :as opts }]
  " room-id from message notify color"
  (map (fn [v] (if (nil? (get opts v)) (throw (java.lang.Exception. (str "Missing argument: " v))))) [:room-id :from :message])
  (let [{:keys [room-id from message notify color] :or {notify 0 color "yellow"}} opts
        url (str api-url "/rooms/message?" (client/generate-query-string {"format" "json" "auth_token" auth-token}) )
        body (client/generate-query-string { "room_id" room-id
                                             "from" from
                                             "message" message
                                             "notify" notify
                                             "color" color})]
    (:status
     (read-json
      (:body
       (client/get (str api-url "/rooms/message") {:query-params {"format" "json"
                                                                  "auth_token" auth-token
                                                                  "room_id" room-id
                                                                  "from" from
                                                                  "message" message
                                                                  "notify" notify
                                                                  "color" color}}))))))

(defn show [auth-token room-id]
  (:room
   (read-json
    (:body
     (client/get (str api-url "/rooms/show") {:query-params {"format" "json"
                                                             "auth_token" auth-token
                                                             "room_id" room-id}})))))

(defn create [auth-token {name :name
                          owner_user_id :owner_user_id
                          privacy :privacy
                          topic :topic
                          guest_access :guest_access :as opts}]
  (let [{:keys [name owner_user_id privacy topic] :or {guest_access 0 topic "" "privacy" "public"}} opts]
    (:room
     (read-json
      (:body
       (client/post url {:content-type "application/x-www-form-urlencoded"
                         :accept "json"
                         :body (client/generate-query-string {"format" "json"
                                                              "auth_token" auth-token
                                                              "name" name
                                                              "owner_user_id" owner_user_id
                                                              "privacy" privacy
                                                              "topic" topic
                                                              "guest_access" guest_access})}))))))

(defn delete [auth-token room-id]
  (:deleted
   (read-json
    (:body
     (client/post (str api-url "/rooms/delete")
                  {:content-type "application/x-www-form-urlencoded"
                   :accept "json"
                   :body (client/generate-query-string {"format" "json"
                                                        "auth_token" auth-token
                                                        "room_id" room-id})})))))



