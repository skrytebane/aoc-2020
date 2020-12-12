(ns aoc-2020.day-12
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- parse-navigation [s]
  (let [[_ direction distance] (re-matches #"([NSEWLRF])([0-9]+)" s)]
    (vector (first direction) (Long/parseLong distance))))

(defn- direction->degrees [direction]
  (case direction
    \N 0
    \S 180
    \E 90
    \W 270))

(defn- degrees->direction [degrees]
  (case (mod degrees 360)
    0 \N
    180 \S
    90 \E
    270 \W))

(def init-pos
  {:facing (direction->degrees \E)
   :east 0
   :north 0})

(defn- turn [pos degrees]
  (assoc pos :facing (mod (+ (:facing pos) degrees)
                          360)))

(defn- step [pos [direction value]]
  (case direction
    \N (update pos :north #(+ % value))
    \S (update pos :north #(- % value))
    \E (update pos :east #(+ % value))
    \W (update pos :east #(- % value))
    \F (step pos [(degrees->direction (:facing pos)) value])
    \L (turn pos (- value))
    \R (turn pos value)))

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

(def init-waypoint
  (assoc init-pos
         :waypoint-north (inc (:north init-pos))
         :waypoint-east (+ 10 (:east init-pos))))

(defn- rotate-waypoint [pos degrees]
  (let [radians (Math/toRadians degrees)
        s (Math/sin radians)
        c (Math/cos radians)]
    (assoc pos
           :waypoint-east (Math/round (- (* (:waypoint-east pos) c)
                                   (* (:waypoint-north pos) s)))
           :waypoint-north (Math/round (+ (* (:waypoint-east pos) s)
                                    (* (:waypoint-north pos) c))))))

(defn- step-waypoint [pos [direction distance]]
  (case direction
    \N (update pos :waypoint-north #(+ % distance))
    \S (update pos :waypoint-north #(- % distance))
    \E (update pos :waypoint-east #(+ % distance))
    \W (update pos :waypoint-east #(- % distance))
    \F (-> pos
           (update :north #(+ % (* (:waypoint-north pos) distance)))
           (update :east #(+ % (* (:waypoint-east pos) distance))))
    \L (rotate-waypoint pos distance)
    \R (rotate-waypoint pos (- distance))))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       (map parse-navigation)
       (reduce step-waypoint init-waypoint)
       ((juxt :east :north))
       (map #(Math/abs %))
       (reduce +)))

(comment
  (solution-a "sample-12.txt") ; 25
  (solution-a "input-12.txt")  ; 2280
  (solution-b "sample-12.txt") ; 286
  (solution-b "input-12.txt")  ; 38693
  )
