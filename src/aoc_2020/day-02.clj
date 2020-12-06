(ns aoc-2020.day-02
  (:require [aoc-2020.core :refer [parse-numbers slurp-lines]]
            [clojure.string :as str]))

(defn parse-line [line]
  (let [[reps letter password] (str/split line #":?\s")
        [from to] (parse-numbers (str/split reps #"-"))]
    (vector from to letter password)))

(defn valid-password [entry]
  (let [[from to letter password] entry
        matches (count (re-seq (re-pattern letter) password))]
    (<= from matches to)))

(defn solution-day02 [filename]
  (->> filename
       slurp-lines
       (map parse-line)
       (filter valid-password)
       count))

(defn valid-password-b [entry]
  (let [[pos-1 pos-2 letter password] entry]
    (= 1
       (count (re-seq (re-pattern letter)
                      (str (nth password (dec pos-1))
                           (nth password (dec pos-2))))))))

(defn solution-day02-b [filename]
  (->> filename
       slurp-lines
       (map parse-line)
       (filter valid-password-b)
       count))

(comment
  (solution-day02 "sample-02.txt")
  (solution-day02 "input-02.txt")
  (solution-day02-b "sample-02.txt")
  (solution-day02-b "input-02.txt")
  )
