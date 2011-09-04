(ns clipchat.rooms
  (:use [clipchat.core :only [api-url urlencoded]])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clj-http.client :as client]))

(defn list-rooms [auth-token]
  " Returns a vector of maps defining the rooms available. https://www.hipchat.com/docs/api/method/rooms/list
    'list' means something in clojure, so the name is 'list-rooms' instead."
  (let [query {:format "json" :auth_token auth-token}]
    (:rooms
     (read-json
      (:body
       (client/get (str api-url "/rooms/list") {:query-params query}))))))

(defn history [auth-token {room_id :room_id date :date :as opts}]
  " Returns the history of messages to the specified room as a vector of maps.
    https://www.hipchat.com/docs/api/method/rooms/history "
  (let [query (assoc opts :auth_token auth-token :format "json")]
    (:messages
     (read-json
      (:body
       (client/get (str api-url "/rooms/history") {:query-params query}))))))


(defn message [auth-token {room-id :room-id
                           from :from
                           message :message
                           notify :notify
                           color :color :as opts }]
  " XXX: not working yet
    room-id from message notify color"
  (map (fn [v] (if (nil? (get opts v)) (throw (java.lang.Exception. (str "Missing argument: " v))))) [:room-id :from :message])
  (let [{:keys [room-id from message notify color] :or {notify 0 color "yellow"}} opts
        url (str api-url "/rooms/message?" (client/generate-query-string {"format" "json" "auth_token" auth-token}) )
        body (client/generate-query-string (assoc opts :auth_token auth-token :format "json"))]
    (:status
     (read-json
      (:body
       (client/post (str api-url "/rooms/message") {:content-type urlencoded
                                                    :accept "application/json"
                                                    :body body}))))))

(defn message-get [auth-token {room-id :room-id
                               from :from
                               message :message
                               notify :notify
                               color :color :as opts }]
  " room-id from message notify color"
  (map (fn [v] (if (nil? (get opts v)) (throw (java.lang.Exception. (str "Missing argument: " v))))) [:room-id :from :message])
  (let [{:keys [room-id from message notify color] :or {notify 0 color "yellow"}} opts
        url (str api-url "/rooms/message")
        body (client/generate-query-string {"room_id" room-id
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

(defn show [auth-token {room_id :room_id :as opts}]
  (let [query (assoc opts :auth_token auth-token :format "json")]
    (:room
     (read-json
      (:body
       (client/get (str api-url "/rooms/show") {:query-params query}))))))

(defn create [auth-token {name :name
                          owner_user_id :owner_user_id
                          privacy :privacy
                          topic :topic
                          guest_access :guest_access :as opts }]
  (let [{:keys [name owner_user_id privacy topic] :or { guest_access 0 topic "" "privacy" "public" }} opts
        url (str api-url "/rooms/create")
        query (assoc opts :auth_token auth-token :format "json")]
    (:room
     (read-json
      (:body
       (client/post url {:content-type urlencoded
                         :accept "json"
                         :body (client/generate-query-string query)}))))))

(defn delete [auth-token {room_id :room_id :as opts}]
  (let [{:keys [room_id]} opts
        query  (assoc opts :auth_token auth-token :format "json")]
    (:deleted
     (read-json
      (:body
       (client/post (str api-url "/rooms/delete")
                    {:content-type urlencoded
                     :accept "json"
                     :body (client/generate-query-string query)}))))))



(defn testqs [{auth_token :auth_token
               name :name
               owner_user_id :owner_user_id
               privacy :privacy
               topic :topic
               guest_access :guest_access :as opts }]
  (let [{:keys [auth_token name owner_user_id privacy topic] :or { guest_access 0 topic "" "privacy" "public" }} opts]
    (println (client/generate-query-string opts))))
