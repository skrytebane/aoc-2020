(ns aoc-2020.day-10
  (:require [aoc-2020.core :refer [slurp-lines parse-ints]]))

(defn read-jolts [filename]
  (->> filename
       slurp-lines
       parse-ints
       sort))

(defn connect-adapters [adapters]
  (loop [remaining adapters
         current-joltage 0
         connected [0]]
    (let [eligible (->> remaining
                        (filter #(<= (- % current-joltage) 3)))]
      (if (empty? eligible)
        (conj connected (+ current-joltage 3))
        (let [selected (reduce min eligible)]
          (recur (sort (remove #(= % selected) remaining))
                 selected
                 (conj connected selected)))))))

(defn solution-a [filename]
  (as-> (read-jolts filename) v
    (connect-adapters v)
    (partition 2 1 v)
    (map #(reduce - (reverse %)) v)
    (group-by identity v)
    (* (count (get v 1))
       (count (get v 3)))))

(comment
  (solution-a "sample-10-a.txt") ;; 35
  (solution-a "sample-10-b.txt") ;; 220
  (solution-a "input-10.txt") ;; 1980
  )
