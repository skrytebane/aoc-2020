(ns aoc-2020.day-08
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-program [lines]
  (mapv (fn [line]
          (let [[_ instruction offset] (re-matches #"([a-zA-Z]+)\s+([+-]\d+)" line)]
            (vector instruction (Long/parseLong offset))))
        lines))

(defn- step [program execution]
  (let [[instruction offset] (nth program (:ip execution))
        ip (:ip execution)
        counters (update-in (:counters execution) [ip] inc)]
    (case instruction
      "nop" (assoc execution
                   :ip (inc ip)
                   :counters counters)
      "acc" (assoc execution
                   :ip (inc ip)
                   :accumulator (+ (:accumulator execution) offset)
                   :counters counters)
      "jmp" (assoc execution
                   :ip (+ ip offset)
                   :counters counters))))

(defn execute-program [program]
  (let [execution {:ip 0
                   :counters (vec (take (count program) (repeat 0)))
                   :accumulator 0}]
    (loop [execution (step program execution)
           trace [execution]]
      (if (some #(> % 1) (:counters execution))
        trace
        (let [next-step (step program execution)]
          (recur next-step (conj trace next-step)))))))

(defn solution-day08 [filename]
  (->> filename
       slurp-lines
       parse-program
       execute-program
       butlast
       last
       :accumulator))

(defn scratch []
  (solution-day08 "input-08.txt"))

(comment
  (solution-day08 "sample-08.txt") ;; 5
  (solution-day08 "input-08.txt") ;; 1859
  )
