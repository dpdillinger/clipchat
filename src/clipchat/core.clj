(ns clipchat.core)

(def api-url "http://api.hipchat.com/v1")
(def urlencoded "application/x-www-form-urlencoded")

(defn setup-call-body [body]
  {:content-type urlencoded
   :accept "json"
   :body body})
