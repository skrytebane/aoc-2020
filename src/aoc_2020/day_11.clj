(ns aoc-2020.day-11
  (:require [aoc-2020.core :refer [slurp-lines parse-map]]
            [clojure.string :as str]))

(defn- pos-get [m x y]
  (nth (nth m y) x))

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

(defn- neighbours [m x y]
  (for [ny (range (dec y) (+ 2 y))]
    (for [nx (range (dec x) (+ 2 y))
          :when (not (= [nx ny] [x y]))]
      [x y])))

(defn solution-a [filename]
  (-> filename
      slurp-lines
      parse-map))

(defn scratch []
  (solution-a "sample-11.txt"))

(comment
  (solution-a "sample-11.txt")
  )
