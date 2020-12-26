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
       frequencies
       :black))

(defn- arrange-tiles [filename]
  (->> filename
       slurp-lines
       (map parse-directions)
       flip-tiles))

(defn solution-a [filename]
  (->> filename
       arrange-tiles
       count-black-tiles))

(defn- neighbours [[x y]]
  (for [direction [:w :nw :sw :e :ne :se]]
    (adjust-position [x y] direction)))

(defn- adjacent-tiles [m pos]
  (->> pos
       neighbours
       (map #(get m %))
       (remove nil?)
       frequencies))

(defn- expand-floor [tiles]
  (let [positions (set (keys tiles))]
    (->> positions
         (map neighbours)
         (map set)
         (reduce set/union positions)
         (map #(vector % (get tiles % :white)))
         (reduce merge {}))))

(defn- rearrange-tiles [tiles]
  (->> (for [[pos color] tiles]
         (let [adjacent (adjacent-tiles tiles pos)]
           [pos
            (case color
              :black (if (or (zero? (:black adjacent 0))
                             (>= (:black adjacent 0) 2))
                       :white
                       :black)
              :white (if (= 2 (:black adjacent 0))
                       :black
                       :white))]))
       (reduce merge {})))

(defn solution-b [filename]
  (->> filename
       arrange-tiles
       expand-floor
       rearrange-tiles
       ))

(comment
  (solution-a "sample-24.txt")
  (solution-a "input-24.txt")
  (solution-b "sample-24.txt")
  (solution-b "input-24.txt")
  )
