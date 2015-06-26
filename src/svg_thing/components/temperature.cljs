(ns svg-thing.component.temperature
  (:require [svg-thing.component.temperature.gauge :refer [gauge]]
            [svg-thing.component.temperature.background :refer [background]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                     TEMPERATURE PAGE                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; @props
;;    :range { :min NUMBER :max NUMBER }
;;    :current NUMBER


(defn page [temp-data]
  [:div.temp-page
   [background temp-data]
   [:div.temp-hud
    [:span.temp-heading "Temperature"]
    [:br]
    [:span.temp-value (or (temp-data :explict) (temp-data :current)) "°"]]
   [gauge (assoc temp-data :svg-url "/svg/tempature.svg")]])

