(ns aoc-2020.day-25
  (:require [aoc-2020.core :refer [slurp-lines parse-ints]]))

(defn- find-loop-size [target]
  (loop [v 1
         loop-size 1]
    (if (= v target)
      (dec loop-size)
      (recur (mod (* v 7) 20201227)
             (inc loop-size)))))

(defn- transform-subject [[subject-number loop-size]]
  (loop [v 1
         i loop-size]
    (if (zero? i)
      v
      (recur (mod (* v subject-number) 20201227)
             (dec i)))))

(defn- find-encryption-key [pubkeys]
  (->> pubkeys
       (map find-loop-size)
       (map hash-map (reverse pubkeys))
       (reduce merge {})
       (map transform-subject)))

(defn solution-a [filename]
  (as-> (slurp-lines filename) v
    (parse-ints v)
    (find-encryption-key v)))

(comment
  (solution-a "sample-25.txt")
  (solution-a "input-25.txt")
  )
