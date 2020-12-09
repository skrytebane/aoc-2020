(ns aoc-2020.day-09
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.math.combinatorics :as comb]))

(defn- pairs [numbers]
  (comb/combinations numbers 2))

(defn- range-sums [numbers from to]
  (->> (for [index (range from to)]
        (nth numbers index))
       pairs
       (map #(reduce + %))
       set))

(defn- read-numbers [filename]
  (->> filename
       slurp-lines
       (map #(Long/parseLong %))
       vec))

(defn solution-a [filename preamble-size]
  (let [numbers (read-numbers filename)]
    (->> (range preamble-size (count numbers))
         (remove #(contains? (range-sums numbers (- % preamble-size) %) (nth numbers %)))
         (map #(nth numbers %))
         first)))

(defn solution-b [filename preamble-size]
  (let [numbers (read-numbers filename)
        invalid-number (solution-a filename preamble-size)
        index (.indexOf numbers invalid-number)
        test-range (subvec numbers 0 index)
        window-sizes (range 2 (count test-range))]
    (->> window-sizes
         (map #(partition % 1 test-range))
         (map #(filter (fn [x]
                         (= (reduce + x) invalid-number))
                       %))
         (filter seq)
         ffirst
         ((juxt (partial reduce min) (partial reduce max)))
         (reduce +))))

(comment
  (solution-a "sample-09.txt" 5) ;; 127
  (solution-a "input-09.txt" 25) ;; 1492208709
  (solution-b "sample-09.txt" 5) ;; 62
  (solution-b "input-09.txt" 25) ;; 238243506
  )
