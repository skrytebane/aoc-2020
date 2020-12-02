(ns aoc-2020.core)

(defn parse-line [line]
  (let [[reps letter password] (str/split line #":?\s")
        [from to] (parse-numbers (str/split reps #"-"))]
    (vector from to letter password)))

(defn valid-password [entry]
  (let [[from to letter password] entry
        matches (count (re-seq (re-pattern letter) password))]
    (and (>= matches from)
         (<= matches to))))

(defn solution-day02 [filename]
  (->> filename
       slurp-lines
       (map parse-line)
       (filter valid-password)))

(comment
  (solution-day02 "sample-02.txt")
  )
