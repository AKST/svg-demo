(ns svg-thing.colour
  (:require [svg-thing.math :as math]))


;; [{:value Num :colour { :r Num :g Num :b Num }}] -> [{ :r [Num] :g [Num] :b [Num] }]
(defn- generate-channels [colours]
  (loop [cs colours acc {:r [] :g [] :b []}]
    (if (empty? cs)
      acc
      (let [{ v :value [r g b] :colour } (first cs)]
        (recur (next cs) { :r (conj (acc :r) [v r])
                           :g (conj (acc :g) [v g])
                           :b (conj (acc :b) [v b]) })))))


;; Num -> String
(defn- to-hex-channel [number]
  (let [trimmed (cond
                  (< number 0)  0
                  (> number 255) 255
                  :else number)]
    (if (>= trimmed 16)
      (.toString trimmed 16)
      (str \0 trimmed))))


;; [{:value Num :colour { :r Num :g Num :b Num }}] -> (Num -> String)
(defn colour-curve [colours]
  (let [channels (generate-channels colours)
        r-chan (math/multi-point-plot (channels :r))
        g-chan (math/multi-point-plot (channels :g))
        b-chan (math/multi-point-plot (channels :b))]
    (fn [i]
      (.log js/console (to-hex-channel (r-chan i)))
      (str "#"
           (to-hex-channel (.floor js/Math (r-chan i)))
           (to-hex-channel (.floor js/Math (g-chan i)))
           (to-hex-channel (.floor js/Math (b-chan i)))))))


