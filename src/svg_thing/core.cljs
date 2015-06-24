(ns ^:figwheel-always svg-thing.core
  (:require [svg-thing.component.temperature :as temp]
            [svg-thing.component.light :as light]
            [svg-thing.component.main :as main]

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
             :range { :max 100 :min 0 }}
    :temp { :current 0
            :range { :max 60 :min 10 }}}))


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

;; render application
(mount-app)


