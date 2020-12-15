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

(comment
  )
