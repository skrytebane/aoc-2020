(ns aoc-2020.core)

(defn read-numbers [filename]
  (->> filename
       io/resource
       io/file
       slurp
       str/trim
       str/split-lines
       (map #(Integer/parseInt %))))

(defn find-2020 [filename n]
  (->> (comb/combinations (read-numbers filename) n)
       (filter #(= 2020 (reduce + %)))
       first))

(defn solution-day01 [filename n]
  (let [result (find-2020 filename n)]
    (vector result (reduce * result))))

(comment
  (solution-day01 "sample-01.txt" 2)
  (solution-day01 "sample-01.txt" 3)
  (solution-day01 "input-01.txt" 2)
  (solution-day01 "input-01.txt" 3))
