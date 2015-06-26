(ns svg-thing.component.temperature.gauge
  (:require [svg-thing.http :as http]
            [svg-thing.math :as math]
            [svg-thing.colour :as colour]
            [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [cljs.core.async :as async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


;; TODO make part of the prop interface
(def ^{:private true} consts
  { :svg-pt { :range { :min 330.7
                       :max 50 }}})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                    TEMPERATURE GAUGE                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; @props
;;    :svg-url url to the svg temp-o-stat
;;    :range { :min NUMBER :max NUMBER }
;;    :current NUMBER



(defn- attach-svg [this]
  "loads the svg from the server and then attaches it to the dom"
  (go (let [svg (<! (http/get-svg ((reagent/props this) :svg-url)))
            dot (sel1 svg :#temperature-top)
            bar (sel1 svg :#temperature-mid)
            state (reagent/state-atom this)]
    (dommy/add-class! svg "temperature-svg")
    (dommy/append! (reagent/dom-node this) svg))))


(defn- adjust-graphic [this]
  "Moves svg components to represent the temperature"
  (let [dom (reagent/dom-node this)
        dot (sel1 dom :#temperature-top)
        bar (sel1 dom :#temperature-mid)
        props (reagent/props this)

        temperature (or (props :explict) (props :current))
        y-position (math/plot-range (props :range) ((consts :svg-pt) :range))
        ;; height cannot go below zero
        bar-height (max 0 (- (get-in consts [:svg-pt :range :min]) (y-position temperature)))]

    (dommy/set-attr! dot :cy (y-position temperature))
    (dommy/set-attr! bar :y (y-position temperature))
    (dommy/set-attr! bar :height bar-height)))


(defn on-load [this]
  (go (<! (attach-svg this))
      (adjust-graphic this)))


(defn- render [this]
  [:div.temperature-wrapper (merge this
    { :current 25 :range { :min 10 :max 60 }})])


(def gauge
  (reagent/create-class
    { :component-did-mount on-load
      :component-did-update adjust-graphic
      :reagent-render render }))


