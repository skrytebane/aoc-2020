(ns aoc-2020.day-12
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-navigation [s]
  (let [[_ direction distance] (re-matches #"([NSEWLRF])([0-9]+)" s)]
    (vector (first direction) (Long/parseLong distance))))

(def init-pos
  {:facing \E
   :east 0
   :north 0})

(defn- step-direction [facing degrees]
  (case (mod (+ (case facing
                  \N 0
                  \S 180
                  \E 90
                  \W 270)
                degrees)
             360)
    0 \N
    180 \S
    90 \E
    270 \W))

(defn- step [pos [direction distance]]
  (case direction
    \N (update pos :north #(- % distance))
    \S (update pos :north #(+ % distance))
    \E (update pos :east #(+ % distance))
    \W (update pos :east #(- % distance))
    \F (step pos [(:facing pos) distance])
    \L (assoc pos :facing (step-direction (:facing pos) (- distance)))
    \R (assoc pos :facing (step-direction (:facing pos) distance))))

(defn- travel [pos instructions]
  (reduce step pos instructions))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map parse-navigation)
       (travel init-pos)
       ((juxt :east :north))
       (reduce +)))

(comment
  (solution-a "sample-12.txt")
  (solution-a "input-12.txt")
  )
