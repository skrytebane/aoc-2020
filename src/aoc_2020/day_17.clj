(ns aoc-2020.day-17
  (:require [aoc-2020.core :refer [parse-map slurp-lines]]))

(defn- make-cube [init]
  (->> (for [y (range (count init))
             x (range (count (nth init 0)))]
         {[x y 0] (nth (nth init y) x)})
       (reduce merge)))

(def directions
  (vec
   (for [x (range -1 2)
         y (range -1 2)
         z (range -1 2)
         :when (not= x y z 0)]
     [x y z])))

(defn- neighbour-coordinates [x y z]
  (map #(map + [x y z] %) directions))

(defn- neighbours [cube x y z]
  (->> (neighbour-coordinates x y z)
       (map #(get cube %))))

(defn- expand-cube [cube]
  (loop [cube cube
         neighbours (mapcat #(apply neighbour-coordinates %) (keys cube))]
    (if-let [neighbour (first neighbours)]
      (recur (assoc cube
                    (vec neighbour)
                    (get cube (vec neighbour) \.))
             (rest neighbours))
      cube)))

(defn- active? [state] (= state \#))

(defn- active-neighbours [cube x y z]
  (->> (neighbours cube x y z)
       (filter active?)
       count))

(defn- step [cube]
  (->> cube
       expand-cube
       (map (fn [[[x y z] state]]
              [[x y z]
               (let [active-neighbour-count (active-neighbours cube x y z)]
                 (cond
                   (and (active? state)
                        (contains? #{2 3} active-neighbour-count))
                   \#
                   (and (not (active? state))
                        (= 3 active-neighbour-count))
                   \#
                   :else \.))]))
       (reduce merge {})))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       parse-map
       make-cube
       ((apply comp (take 6 (repeat step))))
       vals
       (filter #(= % \#))
       count))

(comment
  (solution-a "sample-17.txt")
  (solution-a "input-17.txt")
  )
