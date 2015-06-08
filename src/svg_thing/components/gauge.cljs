(ns svg-thing.component.gauge
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
                       :max 50 }}
    :colour { :cold "#3472d4"
              :hot  "#f15a68" }})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                    TEMPERATURE GAUGE                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; @props
;;    :svg-url url to the svg temp-o-stat
;;    :range { :min NUMBER :max NUMBER }
;;    :temp NUMBER



(defn- attach-svg [this]
  "loads the svg from the server and then attaches it to the dom"
  (go (let [svg (<! (http/get-svg ((reagent/props this) :svg-url)))
            dot (sel1 svg :#temperature-top)
            bar (sel1 svg :#temperature-mid)
            state (reagent/state-atom this)]
    (dommy/add-class! svg "temperature-svg")
    (dommy/append! (reagent/dom-node this) svg))))


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


(defn- adjust-graphic [this]
  "Moves svg components to represent the temperature"
  (let [dom (reagent/dom-node this)
        dot (sel1 dom :#temperature-top)
        bar (sel1 dom :#temperature-mid)
        bg  (sel1 :.background)

        temperature ((reagent/props this) :temp)
        y-position (math/plot-range ((reagent/props this) :range) ((consts :svg-pt) :range))
        bar-height (- (get-in consts [:svg-pt :range :min]) (y-position temperature))
        bg-colour ((colour-curve this) temperature)]

    (dommy/set-attr! dot :cy (y-position temperature))
    (dommy/set-attr! bar :y (y-position temperature))
    (dommy/set-attr! bar :height bar-height)
    ;;
    ;; todo refactor bg into it's own component
    ;;
    (dommy/set-style! bg :background-color bg-colour)))


(defn on-load [this]
  (go (<! (attach-svg this))
      (adjust-graphic this)))


(defn- render [this]
  [:div.temperature-wrapper (merge this
    { :temp 25
      :range { :min 10 :max 60 }})])


(def temperature
  (reagent/create-class
    { :component-did-mount on-load
      :component-did-update adjust-graphic
      :reagent-render render }))


