(ns aoc-2020.day-16
  (:require [aoc-2020.core :refer [slurp-groups parse-ints]]
            [clojure.string :as str]))

(defn- parse-rule [[name val]]
  (as-> (str/split val #"\s*or\s*") v
    (map #(str/split % #"-") v)
    (map parse-ints v)
    {name v}))

(defn- parse-rules [lines]
  (->> lines
       (map #(str/split % #":\s*" 2))
       (map parse-rule)
       (reduce merge {})))

(defn- parse-tickets [lines]
  (let [[[header] tickets] (split-at 1 lines)]
    (when (contains? #{"nearby tickets:" "your ticket:"} header)
      (->> tickets
           (map #(str/split % #","))
           (map parse-ints)
           (mapv vec)))))

(defn- parse-notes [groups]
  (let [[rules my-ticket nearby-tickets] groups]
    {:rules (parse-rules rules)
     :my-ticket (first (parse-tickets my-ticket))
     :nearby-tickets (parse-tickets nearby-tickets)}))

(defn invalid-ticket-field? [rules num]
  (->> rules
       vals
       (apply concat)
       (not-any? (fn [[a b]] (<= a num b)))))

(defn solution-a [filename]
  (let [notes (parse-notes (slurp-groups filename))]
    (->> notes
         :nearby-tickets
         flatten
         (filter #(invalid-ticket-field? (:rules notes) %))
         (reduce +))))

(comment
  (solution-a "sample-16.txt")
  (solution-a "input-16.txt")
  )
