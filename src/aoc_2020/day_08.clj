(ns aoc-2020.day-08
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-instruction [line]
  (let [[_ instruction offset] (re-matches #"([a-zA-Z]+)\s+([+-]\d+)" line)]
    (vector instruction (Long/parseLong offset))))

(defn- parse-program [lines]
  (mapv parse-instruction lines))

(defn- step [program execution]
  (let [ip (:ip execution)
        [instruction offset] (nth program ip)
        [update-ip update-acc] (case instruction
                                 "jmp" [(partial + offset) +]
                                 "nop" [(fnil inc 0) +]
                                 "acc" [(fnil inc 0) (partial + offset)])]
    (-> execution
        (update-in [:counters ip] (fnil inc 0))
        (update :ip update-ip)
        (update :accumulator update-acc))))

(defn- execute-program [program]
  (let [initial-execution {:ip 0
                           :counters {}
                           :accumulator 0}
        final-instruction (dec (count program))]
    (loop [previous-ip (:ip initial-execution)
           execution (step program initial-execution)
           trace [execution]]
      (if (or (> (get (:counters execution) previous-ip 0)
                 1)
              (= (:ip execution) final-instruction))
        trace
        (let [next-step (step program execution)]
          (recur (:ip execution) next-step (conj trace next-step)))))))

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
  (let [program (->> filename slurp-lines parse-program)]
    (->> (range (count program))
         (filter #(contains? #{"jmp" "nop"} (first (nth program %))))
         (map #(twiddle-program program %))
         (map execute-program)
         (map last)
         (filter #(= (:ip %) (dec (count program))))
         first
         :accumulator)))

(comment
  (solution-day08 "sample-08.txt") ;; 5
  (solution-day08 "input-08.txt") ;; 1859
  (solution-day08-b "sample-08.txt") ;; 2
  (solution-day08-b "input-08.txt") ;; 1235
  )
