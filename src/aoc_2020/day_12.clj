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
    \N (update pos :north #(+ % distance))
    \S (update pos :north #(- % distance))
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
       (map #(Math/abs %))
       (reduce +)))

(def init-wp
  (assoc init-pos
         :wp-north (inc (:north init-pos))
         :wp-east (+ 10 (:east init-pos))))

(defn- rotate-waypoint [pos degrees]
  (let [radians (Math/toRadians degrees)
        s (Math/sin radians)
        c (Math/cos radians)]
    (assoc pos
           :wp-east (Math/round (- (* (:wp-east pos) c)
                                   (* (:wp-north pos) s)))
           :wp-north (Math/round (+ (* (:wp-east pos) s)
                                    (* (:wp-north pos) c))))))

(defn- step-waypoint [pos [direction distance]]
  (case direction
    \N (update pos :wp-north #(+ % distance))
    \S (update pos :wp-north #(- % distance))
    \E (update pos :wp-east #(+ % distance))
    \W (update pos :wp-east #(- % distance))
    \F (-> pos
           (update :north #(+ % (* (:wp-north pos) distance)))
           (update :east #(+ % (* (:wp-east pos) distance))))
    \L (rotate-waypoint pos distance)
    \R (rotate-waypoint pos (- distance))))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       (map parse-navigation)
       (reduce step-waypoint init-wp)
       ((juxt :east :north))
       (map #(Math/abs %))
       (reduce +)))

(comment
  (solution-a "sample-12.txt")
  (solution-a "input-12.txt")
  (solution-b "sample-12.txt")
  (solution-b "input-12.txt")
  )
