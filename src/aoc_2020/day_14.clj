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

(defn- write-masked [memory instruction mask]
  (let [address (:address instruction)
        value (:value instruction)
        or-mask (Long/parseLong (str/join "" (replace {\X 0} mask)) 2)
        and-mask (Long/parseLong (str/join "" (replace {\X 1} mask)) 2)]
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

(comment
  (solution-a "sample-14.txt")
  (solution-a "input-14.txt")
  )
