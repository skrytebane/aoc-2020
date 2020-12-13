(ns aoc-2020.day-13
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.string :as str]
            [com.hypirion.primes :as p]))

(defn- parse-schedule [lines]
  (let [[earliest buses] lines]
    {:earliest (Long/parseLong earliest)
     :buses (as-> buses v
              (str/split v #",")
              (remove #(= "x" %) v)
              (mapv #(Long/parseLong %) v))}))

(defn- departures [bus-id target]
  (->> (iterate #(+ bus-id %) 0)
       (drop-while #(< % target))
       first))

(defn- find-earliest [target buses]
  (->> (map #(vector % (departures % target)) buses)
       (sort #(compare (second %1) (second %2)))
       first))

(defn solution-a [filename]
  (let [schedule (->> filename
                      slurp-lines
                      parse-schedule)
        [earliest-bus earliest-time] (find-earliest (:earliest schedule) (:buses schedule))]
    (* earliest-bus (- earliest-time (:earliest schedule)))))

(defn- parse-schedule-2 [lines]
  (let [[_ buses] lines]
    (as-> buses v
      (str/split v #",")
      (mapv #(if (= % "x") \x (Long/parseLong %)) v))))

(defn- find-step [schedule]
  (let [biggest-step (reduce max (remove #{\x} schedule))]
    (vector (.indexOf schedule biggest-step) biggest-step)))

(defn- find-start [from div]
  (->> (iterate inc from)
       (filter #(zero? (mod % div)))
       first))

(defn- make-matcher [schedule index]
  (as-> (range (- index) (- (count schedule) index)) v
    (mapv vector v schedule)
    (remove (fn [[i n]] (or (= n \x) (= i 0))) v)
    (fn [n]
      (every? (fn [[sub div]]
                (zero? (mod (+ n sub) div)))
              v))))

(defn- timestamps [schedule]
  (let [[step-index stride] (find-step schedule)
        matcher (make-matcher schedule step-index)
        start (find-start 100000000000000 stride)]
    (- (->> (iterate (partial + stride) start)
            (filter matcher)
            first)
       step-index)))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       parse-schedule-2
       timestamps))

(comment
  (solution-a "sample-13.txt") ; 295
  (solution-a "input-13.txt")  ; 115
  (solution-b "sample-13.txt") ; 1068785
  (solution-b "input-13.txt")  ; ?!
  )
