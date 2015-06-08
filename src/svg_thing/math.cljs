(ns svg-thing.math)


(declare plot)


(defn plot-range [source output]
  "assumes input follows range shape"
  (plot [(source :min) (output :min)]
        [(source :max) (output :max)]))


(defn plot [[xa ya] [xb yb]]
  (let [m (/ (- yb ya) (- xb xa))]
    (fn [x] (+ (* m (- x xa)) ya))))


;; [(Num, Num)] -> [{:min :max :plot}]
(defn- generate-points [plots]
  "Generates points that are made up of a min & max & plot."
  (loop [ps plots acc []]
    (if (or (empty? ps) (empty? (next ps)))
      acc
      (let [left (first ps)
            right (first (next ps))
            eq (plot left right)
            point {:min (left 0) :max (right 0) :plot eq}]
        (recur (next ps) (conj acc point))))))


(defn multi-point-plot [plots]
  "Generates a plot with multiple points."
  (let [min ((plots 0) 0)
        max ((last plots) 0)
        points (generate-points plots)]
    (fn [x]
      (cond
        (and (>= x min) (>= max x))
          (loop [points points]
            (let [head (first points)]
              (if (and (>= x (head :min)) (>= (head :max) x))
                ((head :plot) x)
                (recur (next points)))))
        (<= x min)
          (((first points) :plot) x)
        (>= x max)
          (((last points) :plot) x)))))


