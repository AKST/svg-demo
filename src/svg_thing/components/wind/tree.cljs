(ns svg-thing.component.wind.tree
  (:require [reagent.core :as reagent]
            [svg-thing.http :as http]
            [dommy.core :as dommy :refer-macros [sel sel1]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn- attach-svg [this]
  "loads the svg from the server and then attaches it to the dom"
  (go (let [props (reagent/props this)
            svg (<! (http/get-svg (props :tree-svg-url)))]
    (dommy/add-class! svg "wind-tree")
    (dommy/prepend! (reagent/dom-node this) svg))))


(defn- render-tree [config]
  [:div.wind-tree-outer
   [:span.main-icon-label (config :label)]])

(def tree
  (reagent/create-class
    { :component-did-mount attach-svg
      :reagent-render render-tree }))

