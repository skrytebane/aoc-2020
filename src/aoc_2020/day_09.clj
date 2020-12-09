(ns aoc-2020.day-09
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.math.combinatorics :as comb]))

(defn pairs [numbers]
  (comb/combinations numbers 2))

(defn range-sums [numbers from to]
  (->> (for [index (range from to)]
        (nth numbers index))
       pairs
       (map #(reduce + %))
       set))

(defn solution-a [filename preamble-size]
  (let [numbers (->> filename
                     slurp-lines
                    (map #(Long/parseLong %))
                    vec)]
    (->> (range preamble-size (count numbers))
         (remove #(contains? (range-sums numbers (- % preamble-size) %) (nth numbers %)))
         (map #(nth numbers %))
         first)))

(comment
  (solution-a "sample-09.txt" 5))
