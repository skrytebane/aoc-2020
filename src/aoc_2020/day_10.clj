(ns aoc-2020.day-10
  (:require [aoc-2020.core :refer [slurp-lines parse-ints]]))

(defn- read-jolts [filename]
  (->> filename
       slurp-lines
       parse-ints
       sort))

(defn- connect-adapters [adapters]
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

(defn- is [x]
  (fn [y] (= x y)))

(def valid-connections
  (memoize
   (fn [adapters joltage]
     (letfn [(valid-joltage? [x]
               (<= 1 (- x joltage) 3))]
       (if (empty? adapters)
         1
         (let [remaining (drop-while (complement valid-joltage?) adapters)
               eligible (take-while valid-joltage? remaining)]
           (if (seq eligible)
             (reduce + (map #(valid-connections remaining %)
                            eligible))
             1)))))))

(defn solution-b [filename]
  (as-> (read-jolts filename) v
    (valid-connections (sort v) 0)))

(defn scratch []
  (solution-b "sample-10-a.txt"))

(comment
  (solution-a "sample-10-a.txt") ;; 35
  (solution-a "sample-10-b.txt") ;; 220
  (solution-a "input-10.txt") ;; 1980

  (solution-b "sample-10-a.txt") ;; 8
  (solution-b "sample-10-b.txt") ;; 19208
  (solution-b "input-10.txt") ;; 4628074479616
  )
