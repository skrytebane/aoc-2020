(ns aoc-2020.day-07
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn- parse-contains [contains]
  (case contains
    "no other bags" {}
    (->> (str/split contains #",\s?")
         (map #(re-matches #"(\d+)\s+(\w+\s+\w+) bags?" %))
         (mapv rest)
         (map (fn [[length color]]
                (vector color (Long/parseLong length))))
         (reduce merge {}))))

(defn- parse-bag-specification [line]
  (let [[_ color contains] (re-matches #"(\w+\s+\w+) bags contain (.+)\." line)]
    {color (parse-contains contains)}))

(defn- parse-bag-specifications [lines]
  (->> lines
       (map parse-bag-specification)
       (reduce merge)))

(defn- bag-contains? [bag all-bags]
  (letfn [(get-bags [b]
            (get all-bags b))
          (get-colors [b]
            (reduce set/union (map get-bags b)))]
    (for [direct-contains (vals all-bags)]
      (or (contains? direct-contains bag)
          (loop [step (get-colors direct-contains)]
            (when (not-empty step)
              (or (contains? step bag)
                  (recur (get-colors step)))))))))

(defn solution-day07 [filename]
  (->> filename
       slurp-lines
       parse-bag-specifications
       (reduce-kv (fn [m k v] (assoc m k (set (keys v)))) {})
       (bag-contains? "shiny gold")
       (filter true?)
       count))

(defn bag-sum [color bags]
  (let [contains (get bags color)
        contains-sum (reduce + (vals contains))
        transitive-sums (reduce + (map (fn [[color size]]
                                         (* size (bag-sum color bags)))
                                       contains))]
    (+ contains-sum transitive-sums)))

(defn solution-day07-b [filename]
  (->> filename
       slurp-lines
       parse-bag-specifications
       (bag-sum "shiny gold")))

(defn scratch [filename]
  (->> filename
       slurp-lines
       (map parse-bag-specification)))

(comment
  (solution-day07 "sample-07.txt") ;; 4
  (solution-day07 "input-07.txt") ;; 370
  (solution-day07-b "sample-07.txt") ;; 32
  (solution-day07-b "input-07.txt") ;; 29547
  )
