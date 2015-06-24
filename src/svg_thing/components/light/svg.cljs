(ns svg-thing.component.light.svg
  (:require [reagent.core :as reagent]
            [svg-thing.math :as math]
            [svg-thing.http :as http]
            [dommy.core :as dommy :refer-macros [sel sel1]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(def ^{:private true} consts
  { :sizes { :min 25 :max 40 }
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


(defn- position-curve [light-props circle-id]
  "The colour curve for the background"
  (let [light (light-props :light)
        circle   (get-in consts [:start circle-id])
        sizes    (consts :sizes)
        darkest  (get-in light [:range :min])
        lightest (get-in light [:range :max])

        current  (max (min (light :current) lightest) darkest)

        goes-down  (< (circle :cy) 50)
        goes-right (< (circle :cx) 50)

        r-plot (math/plot [lightest (sizes :min)] [darkest (sizes :max)])
        cy-plot (math/plot [lightest (circle :cy)] [darkest 50] )
        cx-plot (math/plot [lightest (circle :cx)] [darkest 50] )]
    { :r  (percent (r-plot current))
      :cy (percent (cy-plot current))
      :cx (percent (cx-plot current)) }))


(defn- render-multiply [this]
  [:div.light-svg-outer
   [:span.light-percent (percent (get-in this [:light :current]))]
   [:svg.light-svg { :viewBox "0 0 100 100" :width 100 :height 100 }
    (for [[id { start-x :cx start-y :cy }] (consts :start)]
      [:circle (merge { :id id :key id } (position-curve this id))])]])


(def multiply
  (reagent/create-class
    { :reagent-render render-multiply }))

