(ns aoc-2020.day-24
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.set :as set]))

(defn- parse-directions [line]
  (->> line
       (re-seq #"(e|w|se|sw|ne|nw)")
       (map second)
       (map keyword)))

(defn- adjust-position [[x y] direction]
  (case direction
    :w [(- x 2) y]
    :nw [(dec x) (inc y)]
    :sw [(dec x) (dec y)]
    :e [(+ x 2) y]
    :ne [(inc x) (inc y)]
    :se [(inc x) (dec y)]))

(defn- compute-position [directions]
  (reduce adjust-position [0 0] directions))

(defn- arrange-tiles [filename]
  (->> filename
       slurp-lines
       (map parse-directions)
       (map compute-position)
       (reduce (fn [blacks pos]
                 (if (contains? blacks pos)
                   (set/difference blacks #{pos})
                   (set/union blacks #{pos})))
               #{})))

(defn solution-a [filename]
  (->> filename
       arrange-tiles
       count))

(defn- neighbours [[x y]]
  (for [direction [:w :nw :sw :e :ne :se]]
    (adjust-position [x y] direction)))

(defn- all-tiles [black-tiles]
  (->> black-tiles
       (mapcat neighbours)
       set
       (set/union black-tiles)))

(defn adjacent-blacks [black-tiles pos]
  (->> pos
       neighbours
       (filter #(contains? black-tiles %))
       count))

(defn- flip-to-black? [black-tiles pos]
  (let [adjacent-blacks (adjacent-blacks black-tiles pos)]
    (if (contains? black-tiles pos)
      (not (or (zero? adjacent-blacks)
               (> adjacent-blacks 2)))
      (= adjacent-blacks 2))))

(defn- flip-tiles [black-tiles]
  (->> black-tiles
       all-tiles
       (filter (partial flip-to-black? black-tiles))
       set))

(defn solution-b [filename]
  (as-> (arrange-tiles filename) v
    (iterate flip-tiles v)
    (nth v 100)
    (count v)))

(comment
  (solution-a "sample-24.txt")
  (solution-a "input-24.txt")
  (solution-b "sample-24.txt")
  (solution-b "input-24.txt")
  )
