(ns aoc-2020.day-24
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-directions [line]
  (->> line
       (re-seq #"(e|w|se|sw|ne|nw)")
       (map second)
       (map keyword)))

(defn- compute-position [directions]
  (reduce
   (fn [[x y] direction]
     (case direction
       :w [(- x 2) y]
       :nw [(dec x) (inc y)]
       :sw [(dec x) (dec y)]
       :e [(+ x 2) y]
       :ne [(inc x) (inc y)]
       :se [(inc x) (dec y)]))
   [0 0]
   directions))

(defn- flip-tile [tile-value]
  (case tile-value
    (:white nil) :black
    :black :white))

(defn- flip-tiles [positions]
  (reduce
   (fn [state directions]
     (update state (compute-position directions) flip-tile))
   {}
   positions))

(defn- count-black-tiles [tiles]
  (->> tiles
       vals
       (filter #(contains? #{:black} %))
       count))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map parse-directions)
       flip-tiles
       count-black-tiles))

(comment
  (solution-a "sample-24.txt")
  (solution-a "input-24.txt")
  )
