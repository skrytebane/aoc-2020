(ns aoc-2020.core)

(defn parse-numbers [lines]
  (map #(Integer/parseInt %) lines))

(defn find-2020 [n numbers]
  (->> (comb/combinations numbers n)
       (filter #(= 2020 (reduce + %)))))

(defn solution-day01 [filename n]
  (let [result (->> filename
                    slurp-lines
                    parse-numbers
                    (find-2020 n)
                    first)]
    (vector result (reduce * result))))

(comment
  (solution-day01 "sample-01.txt" 2)
  (solution-day01 "sample-01.txt" 3)
  (solution-day01 "input-01.txt" 2)
  (solution-day01 "input-01.txt" 3))
