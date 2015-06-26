(ns svg-thing.component.light.svg
  (:require [reagent.core :as reagent]
            [svg-thing.math :as math]
            [svg-thing.http :as http]
            [dommy.core :as dommy :refer-macros [sel sel1]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(def ^{:private true} consts
  { :sizes { :min 25 :max 50 }
    :start { :W-circle  { :cx 25   :cy 50   }
             :NW-circle { :cx 32.3 :cy 32.3 }
             :N-circle  { :cx 50   :cy 25   }
             :NE-circle { :cx 67.7 :cy 32.3 }
             :E-circle  { :cx 75   :cy 50   }
             :SE-circle { :cx 67.7 :cy 67.7 }
             :S-circle  { :cx 50   :cy 75   }
             :SW-circle { :cx 32.3 :cy 67.7 }}})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                    LIGHT GAUGE                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn- percent [num]
  (str num "%"))


(defn- between [val smallest largest]
  (max (min val largest) smallest))


(defn- get-current [light]
  (let [darkest  (get-in light [:range :min])
        lightest (get-in light [:range :max])]
   (between (or (light :explict) (light :current)) darkest lightest)))


(defn- position-curve [light-props circle-id]
  "The colour curve for the background"
  (let [light (light-props :light)
        circle   (get-in consts [:start circle-id])
        sizes    (consts :sizes)
        darkest  (get-in light [:range :min])
        lightest (get-in light [:range :max])

        current  (get-current light)

        goes-down  (< (circle :cy) 50)
        goes-right (< (circle :cx) 50)

        r-plot (math/plot [lightest (sizes :min)] [darkest (sizes :max)])
        cy-plot (math/plot [lightest (circle :cy)] [darkest 50] )
        cx-plot (math/plot [lightest (circle :cx)] [darkest 50] )]
    { :r  (percent (r-plot current))
      :cy (percent (cy-plot current))
      :cx (percent (cx-plot current)) }))


(defn- rotation [props]
  (let [deg (* (get-current (props :light)) 3.6)]
    (str "rotate(" deg "deg)")))


(defn- render-multiply [this]
  [:div.light-svg-outer 
   [:span.light-percent (percent (get-current (this :light)))]
   [:div.light-rotator  { :style { :transform (rotation this) } }
   [:svg.light-svg { :viewBox "0 0 100 100" :width 600 :height 600 }
    (for [[id { start-x :cx start-y :cy }] (consts :start)]
      [:circle (merge { :id id :key id } (position-curve this id))])]]])


(def multiply
  (reagent/create-class
    { :reagent-render render-multiply }))

