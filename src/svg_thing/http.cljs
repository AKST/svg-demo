(ns svg-thing.http
  (:require [goog.net.XhrIo :as xhr]
            [svg-thing.dom :as dom]
            [cljs.core.async :as async]))


(defn get-svg [url]
  (let [result (async/chan 1)]
    (xhr/send url (fn [event]
      (async/put! result (-> event 
                             .-target 
                             .getResponseText
                             dom/parse-svg))
      (async/close! result)))
    result))

(defn GET [url]
  (let [result (async/chan 1)]
    (xhr/send url (fn [event]
      (async/put! result (-> event .-target .getResponseText))
      (async/close! result)))
    result))

(defn socket [url on-message]
  (let [socket (js/WebSocket. url)]
    (aset socket "onmessage" on-message)))

