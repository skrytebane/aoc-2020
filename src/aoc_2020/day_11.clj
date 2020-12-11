(ns aoc-2020.day-11
  (:require [aoc-2020.core :refer [slurp-lines parse-map]]
            [clojure.string :as str]))

(defn- seating-size [m]
  [(count m)
   (count (nth m 0))])

(defn- pos-get [m x y]
  (let [[sx sy] (seating-size m)]
    (if (and (< -1 x sx)
             (< -1 y sy))
      (nth (nth m y) x)
      nil)))

(defn- pos-set [m x y v]
  (map-indexed (fn [cy line]
                 (if (= cy y)
                   (update line x (constantly v))
                   line))
               m))

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

(defn- step [m]
  (let [changes (->> m
                     seat-positions
                     (filter (fn [[x y]]
                               (and (free? (pos-get m x y))
                                    (free-neighbours? m x y))))
                     (map (fn [[x y]] (vector x y \#))))]
    (loop [nm m
           changes changes]
      (if (empty? changes)
        nm
        (let [[x y v] (first changes)]
          (recur (pos-set nm x y v) (rest changes)))))))

(defn solution-a [filename]
  (-> filename
      slurp-lines
      parse-map))

(defn scratch []
  (solution-a "sample-11.txt"))

(comment
  (solution-a "sample-11.txt")
  )
