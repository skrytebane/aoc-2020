(ns aoc-2020.core)

(defn parse-map [lines]
  "[y,x] 2D-vector"
  (->> lines
       (map #(into [] %))
       (into [])))

(defn map-size [m]
  "Returns [width height]"
  (vector (count (nth m 0))
          (count m)))

(defn show-map [m]
  (let [[width height] (map-size m)]
    (dotimes [y height]
      (println (apply str (nth m y))))))

(defn map-get [m x y]
  (nth (nth m y) x))

(defn move
  ([m x y] (move m x y 3 1))
  ([m x y move-x move-y]
   (let [[width _] (map-size m)
         new-y (+ y move-y)
         line (nth m new-y)
         new-x (mod (+ x move-x) width)]
     (vector new-x new-y))))

(defn ride [m]
  (let [[_ height] (map-size m)]
    (loop [x 0
           y 0
           trail []]
      (if (= y (dec height))
        trail
        (let [[new-x new-y] (move m x y)]
          (recur new-x new-y (conj trail (map-get m new-x new-y))))))))

(defn solution-day03 [filename]
  (->> filename
       slurp-lines
       parse-map
       ride
       (filter #(= % \#))
       count))

(comment
  (solution-day03 "sample-03.txt")
  (solution-day03 "input-03.txt")
  )
