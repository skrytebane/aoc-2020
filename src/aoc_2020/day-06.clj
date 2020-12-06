(ns aoc-2020.core)

(defn parse-questions [filename]
  (->> filename
       slurp-lines
       (partition-by #(= % ""))
       (remove #(= % '("")))
       (map #(map (fn [s] (str/split s #"")) %))))

(defn solution-day06 [filename]
  (->> filename
       parse-questions
       (map #(set (flatten %)))
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
