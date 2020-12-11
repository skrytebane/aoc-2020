(ns aoc-2020.day-03
  (:require [aoc-2020.core :refer [slurp-lines parse-map]]))

(defn map-size [m]
  "Returns [width height]"
  (vector (count (nth m 0))
          (count m)))

(defn map-get [m x y]
  (nth (nth m y) x))

(defn move [m x y move-x move-y]
  (let [[width _] (map-size m)
        new-y (+ y move-y)
        new-x (mod (+ x move-x) width)]
    (vector new-x new-y)))

(defn ride [m right down]
  (let [[_ height] (map-size m)]
    (loop [x 0
           y 0
           trail []]
      (if (>= y (dec height))
        trail
        (let [[new-x new-y] (move m x y right down)]
          (recur new-x new-y (conj trail (map-get m new-x new-y))))))))

(defn read-map [filename]
  (->> filename slurp-lines parse-map))

(defn count-ride [m right down]
  (->> (ride m right down)
       (filter #(= % \#))
       count))

(defn solution-day03 [filename]
  (-> filename
      read-map
      (count-ride 3 1)))

(def alternatives [[1 1]
                   [3 1]
                   [5 1]
                   [7 1]
                   [1 2]])

(defn solution-day03b [filename]
  (let [m (read-map filename)]
    (->> (map #(apply count-ride m %) alternatives)
         (reduce *))))

(comment
  (solution-day03 "sample-03.txt")
  (solution-day03 "input-03.txt")
  (solution-day03b "sample-03.txt")
  (solution-day03b "input-03.txt")
  )
