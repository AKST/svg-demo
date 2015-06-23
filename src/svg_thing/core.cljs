(ns ^:figwheel-always svg-thing.core
  (:require [svg-thing.component.temperature :as temp]
            [dommy.core :as dommy :refer-macros [sel1]]
            #_[secretary.core :as secretary :refer-macros [defroute]]
            [reagent.core :as reagent]
            [reagent.ratom :as ratom]))


(enable-console-print!)


(defonce app-state (ratom/atom
  {:temp { :current 0
           :range { :max 60 :min 10 }}}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                      APPLICATION                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn page []
  (temp/page (@app-state :temp)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                          MAIN                              ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn ^:export main []
  (reagent/render [page] (sel1 :#app)))

(main)


