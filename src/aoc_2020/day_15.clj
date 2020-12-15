(ns aoc-2020.day-15)

(def sample [0 3 6])
(def input [13 16 0 12 15 1])

(defn- generate-steps [init stop]
  (loop [turn (count init)
         prev (transient
               (reduce merge {}
                       (map vector
                            (butlast init)
                            (range (dec (count init))))))
         current (last init)]
    (if (>= turn stop)
      current
      (let [prev-turn (dec turn)
            next-num (if-let [prev-index (get prev current)]
                       (- prev-turn prev-index)
                       0)]
        (recur (inc turn)
               (assoc! prev current prev-turn)
               next-num)))))

;; This function isn't used, just wanted to see how it would look
;; like as a lazy sequence.
(defn- gen-lazy-steps
  ([init]
   (gen-lazy-steps init
                   (count init)
                   (transient
                    (reduce
                     merge
                     {}
                     (map vector (butlast init)
                          (range (dec (count init))))))
                   (last init)))
  ([init turn prev current]
   (lazy-seq
    (concat
     (butlast init)
     (cons current
           (let [prev-turn (dec turn)
                 next-num (if-let [prev-index (get prev current)]
                            (- prev-turn prev-index)
                            0)]
             (gen-lazy-steps nil
                             (inc turn)
                             (assoc! prev current prev-turn)
                             next-num)))))))

(defn solution-a [init]
  (generate-steps init 2020))

(defn solution-b [init]
  (generate-steps init 30000000))

(comment
  (solution-a sample) ; 436
  (solution-a input)  ; 319
  (solution-b sample) ; 175594
  (solution-b input)  ; 2424
  )
