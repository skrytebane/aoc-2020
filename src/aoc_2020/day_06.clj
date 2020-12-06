(ns aoc-2020.day-06
  (:require [aoc-2020.core :refer [slurp-groups]]
            [clojure.set :as set]))

(defn parse-questions [filename]
  (->> filename
       slurp-groups
       (map #(map vec %))))

(defn solution-day06 [filename]
  (->> filename
       parse-questions
       (map (comp set flatten))
       (map count)
       (reduce +)))

(defn solution-day06-b [filename]
  (->> filename
       parse-questions
       (map #(reduce set/intersection (map set %)))
       (map count)
       (reduce +)))

(comment
  (solution-day06 "sample-06.txt")
  (solution-day06 "input-06.txt")
  (solution-day06-b "sample-06.txt")
  (solution-day06-b "input-06.txt")
  )
