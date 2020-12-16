(ns aoc-2020.day-16
  (:require [aoc-2020.core :refer [slurp-groups parse-ints]]
            [clojure.string :as str]))

(defn- parse-rules [lines]
  )

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

(defn solution-a [filename]
  (-> filename
      slurp-groups
      parse-notes))

(comment
  (solution-a "sample-16.txt")
  )
