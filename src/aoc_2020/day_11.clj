(ns aoc-2020.day-11
  (:require [aoc-2020.core :refer [slurp-lines parse-map]]
            [clojure.string :as str]))

(defn- seating-size [m]
  [(count (nth m 0))
   (count m)])

(defn- pos-get [m x y]
  (let [[sx sy] (seating-size m)]
    (if (and (< -1 x sx)
             (< -1 y sy))
      (nth (nth m y) x)
      nil)))

(defn- pos-set [m x y v]
  (update m y #(assoc % x v)))

(defn- show-map [m]
  (do (->> m
           (map #(str/join "" %))
           (map println)
           count)
      nil))

(defn- neighbours [x y]
  (for [ny (range (dec y) (+ 2 y))
        nx (range (dec x) (+ 2 x))
        :when (not (= [ny nx] [y x]))]
    [nx ny]))

(defn- seat-positions [m]
  (let [[sx sy] (seating-size m)]
    (for [x (range sx)
          y (range sy)]
      [x y])))

(defn- occupied? [seat]
  (= seat \#))

(defn- free? [seat]
  (= seat \L))

(defn- free-neighbours? [m x y]
  (->> (neighbours x y)
       (filter (fn [[nx ny]]
                 (occupied? (pos-get m nx ny))))
       empty?))

(defn- seat-step [m [x y]]
  (->> (case (pos-get m x y)
         ;; Empty
         \L (if (free-neighbours? m x y)
              \#
              \L)
         ;; Occupied
         \# (if (>= (->> (neighbours x y)
                         (filter (fn [[nx ny]]
                                   (occupied? (pos-get m nx ny))))
                         count)
                    4)
              \L
              \#)
         ;; Floor
s         \. \.)
       (vector x y)))

(defn- step [m]
  (let [changes (->> m
                     seat-positions
                     (map #(seat-step m %)))]
    (loop [nm m
           changes changes]
      (if (empty? changes)
        nm
        (let [[x y v] (first changes)]
          (recur (pos-set nm x y v) (rest changes)))))))

(defn- steps [m]
  (loop [m m]
    (let [next-step (step m)]
      (if (= m next-step)
        m
        (recur next-step)))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       parse-map
       steps
       flatten
       (filter #(= % \#))
       count))

(defn scratch []
  (solution-a "sample-11.txt"))

(comment
  (solution-a "sample-11.txt")
  (solution-a "input-11.txt")
  )
