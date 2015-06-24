(ns svg-thing.component.light
  (:require [svg-thing.component.light.svg :as svg]))

(defn page [light]
  [:div.light-page
   [:h2.light-title "Light Demo"]
   [svg/multiply { :svg-url "/svg/colour-pallet.svg"
                   :light light }]])
