(ns aoc-2020.day-16
  (:require [aoc-2020.core :refer [slurp-groups parse-ints]]
            [clojure.string :as str]
            [clojure.set :as set]))

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

(defn- invalid-ticket-field? [rules num]
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

(defn- discard-invalid-tickets [rules tickets]
  (remove #(some (partial invalid-ticket-field? rules) %) tickets))

(defn- possible-fields [rules num]
  (->> rules
       (filter (fn [[name ranges]]
                 (some (fn [[a b]] (<= a num b)) ranges)))
       (map first)))

(defn- resolve-mappings [m]
  (loop [m m]
    (let [singles (filter (fn [[_ v]] (= (count v) 1)) m)
          multiples (filter (fn [[_ v]] (> (count v) 1)) m)
          singles-set (reduce set/union (vals singles))]
      (if (= (count singles) (count m))
        m
        (recur (reduce merge {}
                       (concat singles
                               (for [[mk mv] multiples]
                                 [mk (set/difference mv singles-set)]))))))))

(defn- find-column-mappings [rules tickets]
  (let [width (reduce max (map count tickets))]
    (reduce (fn [m [k v]]
              (assoc m k (first v)))
            {}
            (resolve-mappings
             (reduce merge {}
                     (for [col (range width)]
                       {col
                        (reduce set/intersection
                                (for [ticket tickets]
                                  (set (possible-fields rules (nth ticket col)))))}))))))

(defn- make-ticket [ticket mappings]
  (->> ticket
       (map-indexed (fn [i v] (vector (get mappings i) v)))
       (reduce merge {})))

(defn- compute-checksum [ticket]
  (->> ticket
       (filter (fn [[k _]]
                 (str/starts-with? k "departure")))
       (map second)
       (reduce * 1)))

(defn solution-b [filename]
  (let [notes (parse-notes (slurp-groups filename))
        rules (:rules notes)
        tickets (conj (discard-invalid-tickets
                       rules
                       (:nearby-tickets notes))
                      (:my-ticket notes))
        mappings (find-column-mappings rules tickets)]
    (->> (make-ticket (:my-ticket notes) mappings)
         compute-checksum)))

(comment
  (solution-a "sample-16.txt")
  (solution-a "input-16.txt")
  )
