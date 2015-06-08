(ns ^:figwheel-always svg-thing.core
  (:require [svg-thing.http :as http]
            [svg-thing.component.gauge :refer [temperature]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [reagent.core :as reagent]
            [reagent.ratom :as ratom]))

(enable-console-print!)

(defonce app-state (ratom/atom
  {:temp { :current 0
           :max 60
           :min 10 }}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      APPLICATION                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn page []
  (let [temp (@app-state :temp)]
    [:div
     [:div.background]
     [:div.hud
      [:p  "Temperature"
       [:br]
       [:span.value (temp :current) "°"]
       [temperature { :svg-url "/svg/tempature.svg"
                      :temp (temp :current)
                      :range { :min (temp :min)
                               :max (temp :max) }}]]]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                          MAIN                              ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn ^:export main []
  (reagent/render [page] (sel1 :#app)))

(main)


