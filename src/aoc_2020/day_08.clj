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
    (assoc
     (case instruction
       "nop" (assoc execution :ip (inc ip))
       "acc" (assoc execution
                    :ip (inc ip)
                    :accumulator (+ (:accumulator execution) offset))
       "jmp" (assoc execution :ip (+ ip offset)))
     :counters (update-in (:counters execution) [ip] inc))))

(defn execute-program [program]
  (let [execution {:ip 0
                   :counters (vec (take (count program) (repeat 0)))
                   :accumulator 0}
        last-ip (dec (count program))]
    (loop [execution (step program execution)
           trace [execution]]
      (if (or (some #(> % 1) (:counters execution))
              (= (:ip execution) last-ip))
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

(defn swap-instruction [[instruction offset]]
  [(case instruction
     "jmp" "nop"
     "nop" "jmp"
     instruction)
   offset])

(defn twiddle-program [program instruction]
  (update-in program [instruction] swap-instruction))

(defn solution-day08-b [filename]
  (let [program (->> filename slurp-lines parse-program)
        twiddled-programs (->> (range (count program))
                               (filter #(contains? #{"jmp" "nop"} (first (nth program %))))
                               (map #(twiddle-program program %)))]
    (->> twiddled-programs
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
