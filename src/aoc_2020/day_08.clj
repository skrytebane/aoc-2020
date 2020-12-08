(ns aoc-2020.day-08
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-instruction [line]
  (let [[_ instruction offset] (re-matches #"([a-zA-Z]+)\s+([+-]\d+)" line)]
    (vector instruction (Long/parseLong offset))))

(defn- parse-program [lines]
  (mapv parse-instruction lines))

(defn- step [program execution]
  (let [ip (:ip execution)
        [instruction offset] (nth program ip)]
    (-> (case instruction
          "jmp" (update execution :ip (partial + offset))
          "nop" (update execution :ip (fnil inc 0))
          "acc" (-> execution
                    (update :ip (fnil inc 0))
                    (update :accumulator (partial + offset))))
        (update-in [:counters ip] (fnil inc 0)))))

(defn- halted? [program execution previous-ip]
  (let [final-instruction (dec (count program))]
    (or (>= (:ip execution) final-instruction)
        (> (get (:counters execution) previous-ip 0)
           1))))

(defn- execute-program [program]
  (let [initial-execution {:ip 0, :counters {}, :accumulator 0}]
    (loop [execution (step program initial-execution)
           trace [execution]
           previous-ip (:ip execution)]
      (if (halted? program execution previous-ip)
        trace
        (let [next-step (step program execution)]
          (recur next-step (conj trace next-step) (:ip execution)))))))

(defn solution-day08 [filename]
  (->> filename
       slurp-lines
       parse-program
       execute-program
       butlast
       last
       :accumulator))

(defn- swap-instruction [[instruction offset]]
  [(case instruction
     "jmp" "nop"
     "nop" "jmp"
     instruction)
   offset])

(defn- twiddle-program [program instruction]
  (update program instruction swap-instruction))

(defn solution-day08-b [filename]
  (let [program (->> filename slurp-lines parse-program)
        final-instruction (dec (count program))]
    (->> (range (count program))
         (filter #(contains? #{"jmp" "nop"} (first (nth program %))))
         (map #(twiddle-program program %))
         (map execute-program)
         (map last)
         (filter #(= (:ip %) final-instruction))
         first
         :accumulator)))

(comment
  (solution-day08 "sample-08.txt") ;; 5
  (solution-day08 "input-08.txt") ;; 1859
  (solution-day08-b "sample-08.txt") ;; 2
  (solution-day08-b "input-08.txt") ;; 1235
  )
