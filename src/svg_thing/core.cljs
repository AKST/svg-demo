(ns ^:figwheel-always svg-thing.core
  (:require [svg-thing.component.temperature :as temp]
            [svg-thing.component.light :as light]
            [svg-thing.component.main :as main]
            [svg-thing.component.wind :as wind]
            [svg-thing.http :as http]

            [dommy.core :as dommy :refer-macros [sel1]]

            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]

            [reagent.session :as session]
            [reagent.core :as reagent]
            [reagent.ratom :as ratom])
  (:import goog.History))


(enable-console-print!)


(defonce app-state (ratom/atom
  { :light { :current 0
             :explict nil
             :range { :max 100 :min 0 }}
    :temp { :current 0
            :explict nil
            :range { :max 60 :min 10 }}
    :noise { :current 0
             :explict nil
             :range { :max 100 :min 0 }}}))

(defn convert [object]
  (let [keys (.keys js/Object object)]
    (println keys)
    (reduce (fn [obj key] 
              (assoc obj (keyword key) object))
            {}
            keys)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      APPLICATION                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn render []
  (reagent/render [(session/get :current-page)] (sel1 :#app)))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page (fn [] [main/page]))
  (render))

(secretary/defroute "/temp" []
  (session/put! :current-page (fn [] [temp/page (@app-state :temp)]))
  (render))

(secretary/defroute "/light" []
  (session/put! :current-page (fn [] [light/page (@app-state :light)]))
  (render))

(secretary/defroute "/wind" []
  (session/put! :current-page (fn [] [wind/page (@app-state :noise)]))
  (render))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                        REFRESH                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn ^:export mount-app []
  ;; render the current page
  (render))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                          MAIN                              ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; determine the current page
(doto (History.)
  (events/listen
   EventType/NAVIGATE
   (fn [event]
     (secretary/dispatch! (.-token event))))
  (.setEnabled true))

(defn round-decimal [number decimal-count]
  (js/parseFloat (.toFixed number decimal-count) 10))

(http/socket "ws://localhost:1234" (fn [evt]
  (let [data (js->clj (.parse js/JSON (.-data evt)) :keywordize-keys true)]
    (swap! app-state assoc-in [:light :current] 
           (.round js/Math (* 150 (get-in data [:ambience :light]))))
    (swap! app-state assoc-in [:temp :current] 
           (.round js/Math (get-in data [:climate :temperature])))
    )))

;; render application
(mount-app)



