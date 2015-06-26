(ns svg-thing.component.wind
  (:require [svg-thing.component.wind.tree :refer [tree]]))

(defn page []
  [:div.wind-page
   [:h2.wind-title "Wind Demo"]
   [tree { :tree-svg-url "/svg/tree.svg" }]])
