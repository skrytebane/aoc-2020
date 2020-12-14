(ns aoc-2020.day-14
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.string :as str]))

(defn- parse-bitfiddle [line]
  (let [[_ mask] (re-matches #"mask\s*=\s*([10X]+)" line)
        [_ address value] (re-matches #"mem\[(\d+)\]\s*=\s*(\d+)" line)]
    (cond
      (string? mask) {:mask mask}
      (string? address) {:address (Long/parseUnsignedLong address)
                         :value (Long/parseUnsignedLong value)}
      :else "ffs")))

(defn- make-or-mask [mask]
  (Long/parseLong (str/join "" (replace {\X 0} mask)) 2))

(defn- make-and-mask [mask]
  (Long/parseLong (str/join "" (replace {\X 1} mask)) 2))

(defn- write-masked [memory instruction mask]
  (let [address (:address instruction)
        value (:value instruction)
        or-mask (make-or-mask mask)
        and-mask (make-and-mask mask)]
    (assoc memory address (bit-and (bit-or value or-mask) and-mask))))

(defn- write-mem [instructions]
  (loop [instructions instructions
         memory {}
         mask nil]
    (if (empty? instructions)
      memory
      (let [instruction (first instructions)]
        (if (:mask instruction)
          (recur (rest instructions) memory (:mask instruction))
          (recur (rest instructions)
                 (write-masked memory instruction mask)
                 mask))))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map parse-bitfiddle)
       write-mem
       vals
       (reduce +)))

(defn- compute-address [address mask expanded]
  ;; TODO: WIP HERE
  )

(defn- update-memory [memory mask address value]
  (let [or-mask (make-or-mask mask)
        and-mask (make-and-mask mask)
        address (bit-and (bit-or address or-mask) and-mask)
        wildcards (->> mask (filter #(= % \X)) count)
        expanded (range 0 (long (Math/pow 2 wildcards)))]
    (loop [expanded expanded
           memory memory]
      (if (empty? expanded)
        memory
        (let [addr (compute-address address mask (first expanded))]
          (recur (rest expanded) (assoc memory addr value)))))))

(defn- count-mem [instructions]
  (loop [instructions instructions
         memory {}
         mask nil]
    (if (empty? instructions)
      memory
      (let [instruction (first instructions)]
        (if (:mask instruction)
          (recur (rest instructions) memory (:mask instruction))
          (recur (rest instructions) (update-memory memory mask (:address instruction) (:value instruction)) mask))))))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       (map parse-bitfiddle)
       count-mem))

(comment
  (solution-a "sample-14.txt")
  (solution-a "input-14.txt")
  )
