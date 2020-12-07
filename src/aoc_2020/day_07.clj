(ns aoc-2020.day-07
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn- keywordify [color]
  (keyword (str/replace color \space \-)))

(defn- parse-contains [contains]
  (case contains
    "no other bags" {}
    (->> (str/split contains #",\s?")
         (map #(re-matches #"(\d+)\s+(\w+\s+\w+) bags?" %))
         (mapv rest)
         (map (fn [[length color]]
                [(Integer/parseInt length)
                 (keywordify color)]))
         (map reverse)
         flatten
         (apply hash-map))))

(defn- parse-bag-specification [line]
  (let [[_ color contains] (re-matches #"(\w+\s+\w+) bags contain (.+)\." line)]
    {(keywordify color)
     (parse-contains contains)}))

(defn- merge-count-maps [m1 m2]
  (let [keys (set/union (set (keys m1)) (set (keys m2)))]
    (reduce conj {}
            (for [key keys]
              {key (+ (get m1 key 0)
                      (get m2 key 0))}))))

(defn- expand-bag [bags color]
  (let [direct (get bags color {})
        expansions (map (partial expand-bag bags) (keys direct))
        expansions' (reduce merge-count-maps {} expansions)]
    (merge-count-maps direct expansions')))

(defn- expand-bags [bags]
  (reduce conj {}
          (for [bag (keys bags)]
            {bag (expand-bag bags bag)})))

(defn reversed-map [m]
  (reduce
   merge
   (map
    (fn [[k v]] {k (set (map second v))})
    (group-by
     first
     (for [color (keys m)
           deps (keys (get m color))]
       [deps color])))))

(defn solution-day07 [filename bag]
  (let [direct-bags (reduce merge (map parse-bag-specification (slurp-lines filename)))
        transitive-bags (expand-bags direct-bags)
        reversed-map (reversed-map transitive-bags)]
    (-> reversed-map
        (get bag)
        count)))

(defn bag-sum [color bags]
  (let [direct (get bags color)
        direct-sum (reduce + (vals direct))
        indirect-sums
        (reduce + (map (fn [[color size]]
                         (* size (bag-sum color bags)))
                       direct))]
    (+ direct-sum indirect-sums)))

(defn solution-day07-b [filename]
  (->> filename
       slurp-lines
       (map parse-bag-specification)
       (reduce merge)
       (bag-sum :shiny-gold)))

(comment
  (solution-day07 "sample-07.txt" :shiny-gold) ;; 4
  (solution-day07 "input-07.txt" :shiny-gold) ;; 370
  (solution-day07-b "sample-07.txt") ;; 32
  (solution-day07-b "input-07.txt") ;; 29547
  )
