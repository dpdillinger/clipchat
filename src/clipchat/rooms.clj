(ns clipchat.rooms
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clipchat.core :refer :all]))

(defn list-rooms [auth-token]
  "Returns a vector of maps defining the rooms available.
  https://www.hipchat.com/docs/api/method/rooms/list
  'list' means something in clojure, so the name is 'list-rooms' instead."
  (let [query {:format "json" :auth_token auth-token}]
    (:rooms
     (json/read-str
      (:body
       (client/get (str api-url "/rooms/list") {:query-params query}))
      :key-fn keyword))))

(defn history [auth-token {room_id :room_id date :date :as opts}]
  " Returns the history of messages to the specified room as a vector of maps.
    https://www.hipchat.com/docs/api/method/rooms/history "
  (let [query (assoc opts :auth_token auth-token :format "json")]
    (:messages
     (json/read-str
      (:body
       (client/get (str api-url "/rooms/history") {:query-params query}))
      :key-fn keyword))))


(defn message [auth-token {room_id :room_id
                           from :from
                           message :message
                           notify :notify
                           color :color :as opts }]
  (doall
   (map (fn [v]
          (if (nil? (get opts v))
            (throw (java.lang.Exception. (str "Missing argument: " v)))))
        [:room_id :from :message]))
  (let [{:keys [room_id from message notify color]
         :or {notify 0 color "yellow"}} opts
         url (str api-url
                  "/rooms/message?"
                  (client/generate-query-string
                   {"format" "json" "auth_token" auth-token}))
         body (client/generate-query-string
               (assoc opts :auth_token auth-token :format "json"))]
    (:status
     (json/read-str
      (:body
       (client/post (str api-url "/rooms/message") (setup-call-body body)))
      :key-fn keyword))))

(defn show [auth-token {room_id :room_id :as opts}]
  (let [query (assoc opts :auth_token auth-token :format "json")]
    (:room
     (json/read-str
      (:body
       (client/get (str api-url "/rooms/show") {:query-params query}))
      :key-fn keyword))))

(defn create [auth-token {name :name
                          owner_user_id :owner_user_id
                          privacy :privacy
                          topic :topic
                          guest_access :guest_access :as opts}]
  (let [{:keys [name owner_user_id privacy topic guest_access]} opts
        url (str api-url "/rooms/create")
        query (conj
               {:auth_token auth-token
                :format "json"
                :guest_access 0
                :topic ""
                :privacy "public"} opts)]
    (:room
     (json/read-str
      (:body
       (client/post url
                    (setup-call-body
                     (client/generate-query-string query))))
      :key-fn keyword))))

(defn delete [auth-token {room_id :room_id :as opts}]
  (let [{:keys [room_id]} opts
        url (str api-url "/rooms/delete")
        query  (assoc opts :auth_token auth-token :format "json")]
    (:deleted
     (json/read-str
      (:body
       (client/post url
                    (setup-call-body
                     (client/generate-query-string query))))
      :key-fn keyword))))
