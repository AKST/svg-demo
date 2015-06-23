(ns svg-thing.component.temperature.background
  (:require [svg-thing.math :as math]
            [svg-thing.colour :as colour]
            [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [dommy.core :as dommy :refer-macros [sel sel1]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;              TEMPERATURE BACKGROUND GAUGE                  ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; @props
;;    :range { :min NUMBER :max NUMBER }
;;    :current NUMBER

(defn- colour-curve [this]
  "The colour curve for the background"
  (let [temp-range ((reagent/props this) :range)
        s-temp (temp-range :min)
        l-temp (temp-range :max)
        m-temp (/ (- l-temp s-temp) 2)]
    (colour/colour-curve
      [{ :value s-temp :colour [81  89 214] }
       { :value m-temp :colour [194 96 162] }
       { :value l-temp :colour [241 90 104] }])))


(defn- adjust-colour [this]
  "Moves svg components to represent the temperature"
  (let [bg (reagent/dom-node this)
        temperature ((reagent/props this) :current)
        bg-colour ((colour-curve this) temperature)]
    (dommy/set-style! bg :background-color bg-colour)))


(defn- render [this]
  [:div.background])


(def background
  (reagent/create-class
    { :component-did-update adjust-colour
      :reagent-render render }))
