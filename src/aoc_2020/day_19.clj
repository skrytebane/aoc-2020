(ns aoc-2020.day-19
  (:require [aoc-2020.core :refer [slurp-groups]]
            [clojure.string :as str]))

(defn- parse-int? [s]
  (when (re-matches #"\d+" s)
    (Long/parseLong s)))

(defn- parse-terminal? [s]
  (some->> (re-seq #"\"([^\"]+)\"" s)
           (mapv second)))

(defn- transform-rule [rule]
  (cond
    (string? rule)
    (or (parse-int? rule)
        (parse-terminal? rule))

    (seqable? rule)
    (if (= (count rule) 1)
      (first (mapv transform-rule rule))
      (mapv transform-rule rule))))

(defn- parse-rule [line]
  (->> (str/split line #"\s*\|\s*")
       (mapv #(str/split % #"\s+"))
       transform-rule))

(defn- parse-rules [lines]
  (->> lines
       (mapv #(str/split % #":\s*"))
       (mapv (fn [[k v]]
               [(parse-int? k)
                (parse-rule v)]))
       (reduce merge {})))

(defn- make-regexp
  ([rules]
   (make-regexp rules (get rules 0)))
  ([rules rule]
   (cond
     (int? rule)
     (make-regexp rules (get rules rule))

     (string? rule)
     rule

     (vector? rule)
     (mapv (fn [thing]
             (cond
               (vector? thing)
               (mapv (partial make-regexp rules) thing)

               (string? thing)
               thing

               (int? thing)
               (make-regexp rules (get rules thing))))
           rule))))

(defn- parse-inputs [lines]
  (let [[rules messages] lines]
    {:rules (parse-rules rules)
     :messages messages}))

(defn solution-a [filename]
  (-> filename
      slurp-groups
      parse-inputs))

(comment
  (solution-a "sample-19.txt")
  )
