(ns aoc-2020.day-15)

(def sample [0 3 6])
(def input [13 16 0 12 15 1])

(defn- index [n v s]
  (first
   (for [i (reverse (range s))
         :when (= v (nth n i))]
     i)))

(defn- gen-seq [nums stop]
  (loop [nums nums
         turn (count nums)]
    (let [final (last nums)
          prev-final (index nums final (dec turn))]
      (if (= stop turn)
        final
        (if (nil? prev-final)
          (recur (conj nums 0)
                 (inc turn))
          (recur (conj nums (- (dec turn) prev-final))
                 (inc turn)))))))

(defn solution-a [nums]
  (gen-seq nums 2020))

(defn- ngen-seq [nums stop]
  (loop [turn (count nums)
         prev-indexes (reduce merge {}
                              (map vector
                                   (butlast nums)
                                   (range (dec (count nums)))))
         current (last nums)]
    (if (= turn stop)
      current
      (let [prev-index (get prev-indexes current)]
        (if (nil? prev-index)
          (recur (inc turn)
                 (assoc prev-indexes current (dec turn))
                 0)
          (let [next-num (- (dec turn) prev-index)]
            (recur (inc turn)
                   (assoc prev-indexes current (dec turn))
                   next-num)))))))

(defn solution-b [nums]
  (ngen-seq nums 30000000))

(comment
  (solution-a sample) ; 436
  (solution-a input)  ; 319
  (solution-b sample) ; 175594
  (solution-b input)  ; 2424
  )
