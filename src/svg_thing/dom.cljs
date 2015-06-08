(ns svg-thing.dom
    (:require [goog.dom :as dom]))

(extend-type js/HTMLCollection
  ISeqable
  (-seq [array] (array-seq array 0)))

(defn parse-svg [content]
  (let [helper   (dom/DomHelper.)
        document (.parseFromString (js/DOMParser.) content "image/svg+xml")]
    (aget (.getChildren helper document) 0)))

(defn get-id [id]
  (.getElementById js/document id))

(defn append [parent child]
  (.appendChild parent child))

(defn push-class [element class-name]
  (aset element "className" (str (aget element "className") " " class-name)))

(defn remove-children [element]
  (aset element "innerHTML" ""))

