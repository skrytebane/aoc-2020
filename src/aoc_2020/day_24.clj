(ns aoc-2020.day-24
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-directions [line]
  (->> line
       (re-seq #"(e|w|se|sw|ne|nw)")
       (map second)
       (map keyword)))

(defn- move [directions]
  (loop [x 0
         y 0
         directions directions]
    (if (empty? directions)
      [x y]
      (let [[nx ny]
            (case (first directions)
              :w [(dec x) y]
              :nw [(dec x) (inc y)]
              :sw [(dec x) (dec y)]
              :e [(inc x) y]
              :ne [(inc x) (inc y)]
              :se [(inc x) (dec y)])]
        (recur nx ny (rest directions))))))

(defn- flip-tile [tile-value]
  (case tile-value
    (:white nil) :black
    :black :white))

(defn- flip-tiles [directions]
  (loop [state {}
         directions directions]
    (if (empty? directions)
      state
      (let [pos (move (first directions))]
        (println "pos" (str pos) "m" (str state))
        (recur (update state pos flip-tile)
               (rest directions))))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map parse-directions)
       flip-tiles))

(comment
  (solution-a "sample-24.txt")
  )
