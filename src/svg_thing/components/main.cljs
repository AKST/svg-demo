(ns svg-thing.component.main
  (:require [reagent.core :as reagent]
            [svg-thing.component.main.icons :as icons]
            [dommy.core :as dommy :refer-macros [sel sel1]]))

(defn render []
  [:div.main-page
   [:div.blur-wrapper
    [:div.mask-wrapper
     [:h1.main-title "SVG "]]]

   [:ul.main-links
    [:li.main-link-it
      [:a.main-link { :href "#/temp" }
       [icons/own-icon
        { :icon "tempature"
          :label "Temperature"
          :className "temperature-icon" }]]]
    [:li.main-link-it
     [:a.main-link { :href "#/light" }
       [icons/glpyh-iconset
        { :icon "light-bulb"
          :label "Light"
          :className "light-icon" }]]]
    [:li.main-link-it
     [:a.main-link { :href "#/wind" }
       [icons/glpyh-iconset
        { :icon "light-bulb"
          :label "Wind"
          :className "wind-icon" }]]]]])



(def page
  (reagent/create-class
    { :reagent-render render }))
