(ns svg-thing.component.main.icons
  (:require [reagent.core :as reagent]
            [svg-thing.http :as http]
            [dommy.core :as dommy :refer-macros [sel sel1]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn glpyh-iconset [config]
  (let [icon (config :icon)]
    [:div { :className (str "main-icon " (config :className)) }
     [:svg
      { :width 200
        :className (str "main-icon-icon si-glyph-" icon)
        :dangerouslySetInnerHTML
        { :__html (str "<use xlink:href=\"/vendor/glyph-iconset/sprite.svg#si-glyph-" icon "\" />")}}]
     [:span.main-icon-label (config :label)]]))


(defn- attach-svg [this]
  "loads the svg from the server and then attaches it to the dom"
  (go (let [props (reagent/props this)
            icon-name (props :icon)
            svg (<! (http/get-svg (str "/svg/" icon-name ".svg")))]
    (dommy/set-attr! svg "width" 200)
    (dommy/add-class! svg (str "main-icon-icon own-icon-" icon-name))
    (dommy/remove! (sel1 (reagent/dom-node this) :svg))
    (dommy/prepend! (reagent/dom-node this) svg))))


(defn- render-own-icon [config]
  (let [icon (config :icon)]
    [:div { :className (str "main-icon " (config :className)) }
     [:svg {:width 200 :className (str "main-icon-icon own-icon-" icon) }]
     [:span.main-icon-label (config :label)]]))

(def own-icon
  (reagent/create-class
    { :component-did-mount attach-svg
      :reagent-render render-own-icon }))
