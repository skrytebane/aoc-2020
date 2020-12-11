(ns aoc-2020.day-11
  (:require [aoc-2020.core :refer [slurp-lines parse-map]]
            [clojure.string :as str]))

(defn- seating-size
  "Returns [width height] of given map."
  [m]
  [(count (nth m 0))
   (count m)])

(defn- pos-get [m x y]
  (let [[sx sy] (seating-size m)]
    (if (and (< -1 x sx)
             (< -1 y sy))
      (nth (nth m y) x)
      nil)))

(defn- pos-set [m x y v]
  (update-in m [y x] (constantly v)))

(defn- neighbours [x y]
  (for [ny (range (dec y) (+ 2 y))
        nx (range (dec x) (+ 2 x))
        :when (not (= [ny nx] [y x]))]
    [nx ny]))

(defn- seat-positions [m]
  (let [[sx sy] (seating-size m)]
    (for [y (range sy)
          x (range sx)]
      [x y])))

(defn- occupied? [seat]
  (= seat \#))

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
         \. \.)
       (vector x y)))

(defn- los-seats [m x y]
  (let [[sx sy] (seating-size m)
        n (mapv vector (repeat x) (range (dec y) -1 -1))
        s (mapv vector (repeat x) (range (inc y) sy))
        w (mapv vector (range (dec x) -1 -1) (repeat y))
        e (mapv vector (range (inc x) sx) (repeat y))
        nw (mapv vector (range (dec x) -1 -1) (range (dec y) -1 -1) )
        ne (mapv vector (range (inc x) sx) (range (dec y) -1 -1))
        sw (mapv vector (range (dec x) -1 -1) (range (inc y) sy))
        se (mapv vector (range (inc x) sx) (range (inc y) sy))
        paths (vector n s w e nw ne sw se)]
    (->> paths
         (map (fn [coords]
             (map #(apply pos-get m %) coords)))
         (map #(filter (fn [seat] (not (= seat \.))) %))
         (map first))))

(defn- free-los? [m x y]
  (->> (los-seats m x y)
       (filter #(occupied? %))
       empty?))

(defn- seat-los-step [m [x y]]
  (->> (case (pos-get m x y)
         ;; Empty
         \L (if (free-los? m x y)
              \#
              \L)

         ;; Occupied
         \# (if (>= (->> (los-seats m x y)
                         (filter #(occupied? %))
                         count)
                    5)
              \L
              \#)

         ;; Floor
         \. \.
         )
       (vector x y)))

(defn- step [f m]
  (let [[xs _] (seating-size m)]
    (->> m
         seat-positions
         (map #(f m %))
         (partition xs)
         (mapv #(mapv (fn [x]
                        (nth x 2))
                      %)))))

(defn- steps [sf f m]
  (loop [m m
         s 0]
    (let [next-step (f sf m)]
      (if (= m next-step)
        m
        (recur next-step (inc s))))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       parse-map
       (steps seat-step step)
       flatten
       (filter #(= % \#))
       count))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       parse-map
       (steps seat-los-step step)
       flatten
       (filter #(= % \#))
       count))

(defn show-map [m]
  (do (->> m
           (map #(str/join "" %))
           (map println)
           count)
      nil))

(comment
  (solution-a "sample-11.txt") ; 37
  (solution-a "input-11.txt")  ; 2273
  )
