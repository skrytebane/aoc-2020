(ns aoc-2020.day-01
  (:require [clojure.math.combinatorics :as comb]
            [aoc-2020.core :refer [slurp-lines parse-ints]]))

(defn find-2020 [n numbers]
  (->> (comb/combinations numbers n)
       (filter #(= 2020 (reduce + %)))))

(defn solution-day01 [filename n]
  (let [result (->> filename
                    slurp-lines
                    parse-ints
                    (find-2020 n)
                    first)]
    (reduce * result)))

(comment
  (solution-day01 "sample-01.txt" 2)
  (solution-day01 "sample-01.txt" 3)
  (solution-day01 "input-01.txt" 2)
  (solution-day01 "input-01.txt" 3))
