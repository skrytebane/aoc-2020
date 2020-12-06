(ns aoc-2020.day-07
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.set :as set]))

(defn solution-day07 [filename]
  (->> filename
       slurp-lines))

(defn solution-day07-b [filename]
  (->> filename
       slurp-lines))

(comment
  (solution-day07 "sample-07.txt")
  (solution-day07 "input-07.txt")
  (solution-day07-b "sample-07.txt")
  (solution-day07-b "input-07.txt")
  )
