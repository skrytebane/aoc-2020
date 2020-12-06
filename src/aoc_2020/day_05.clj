(ns aoc-2020.day-05
  (:require [aoc-2020.core :refer [slurp-lines]]))

;; This solution is very roundabout. Rows and cols aren't really
;; needed, and the codes are much simpler to compute than what
;; I've done here.

(def sample-inputs
  {"FBFBBFFRLR" {:row 44 :col 5 :id 357}
   "BFFFBBFRRR" {:row 70 :col 7 :id 567}
   "FFFBBBFRRR" {:row 14 :col 7 :id 119}
   "BBFFBBFRLL" {:row 102 :col 4 :id 820}})

(defn floor
  ([n div]
   (int (Math/floor (/ n div))))
  ([n] (floor n 1)))

(defn new-range [direction rng]
  (let [[from to] rng
        diff (- to from)
        half (floor diff 2)]
    (case direction
      (\F \L) (vector from (+ from half))
      (\B \R) (vector (+ from half 1) to))))

(defn parse-bsp [directions from to]
  (loop [remaining directions
         rng (vector from to)]
    (if (empty? remaining)
      (peek rng)
      (recur (rest remaining)
             (new-range (first remaining) rng)))))

(defn make-seat [row col]
  {:row row :col col :id (+ (* row 8) col)})

(defn parse-seating [s]
  (let [[_ row-string col-string] (re-matches #"^([FB]{7})([LR]{3})" s)
        row (parse-bsp row-string 0 127)
        col (parse-bsp col-string 0 7)]
    (make-seat row col)))

(defn check-sample [k]
  (let [expected (get sample-inputs k)
        actual (parse-seating k)]
    (= expected actual)))

(defn check-samples []
  (->> sample-inputs
       keys
       (map check-sample)))

(defn solution-day05-samples []
  (-> (apply max-key :id
             (->> sample-inputs keys (map parse-seating)))
      :id))

(defn solution-day05 [filename]
  (->> filename
       slurp-lines
       (map parse-seating)
       (apply max-key :id)
       :id))

(defn solution-day05-b3 [filename]
  (->> filename
       slurp-lines
       (map parse-seating)
       (map :id)
       sort
       (partition 2 1)
       (filter (fn [[a b]]
                 (= (- b a) 2)))
       ffirst
       inc))

(comment
  (solution-day05 "input-05.txt")
  (solution-day05-b3 "input-05.txt")
  )
