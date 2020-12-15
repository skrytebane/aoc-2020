(ns aoc-2020.day-14
  (:require [aoc-2020.core :refer [slurp-lines]]
            [clojure.string :as str]
            [clojure.pprint :refer [cl-format]]))

(defn- parse-bitfiddle [line]
  (let [[_ mask] (re-matches #"mask\s*=\s*([10X]+)" line)
        [_ address value] (re-matches #"mem\[(\d+)\]\s*=\s*(\d+)" line)]
    (cond
      (string? mask) {:mask mask}
      (string? address) {:address (Long/parseUnsignedLong address)
                         :value (bigint (Long/parseUnsignedLong value))}
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

(defn- compute-address [address indexes bits]
  (loop [address address
         indexes indexes
         bits bits]
    (if (empty? indexes)
      address
      (let [index (first indexes)
            bit (first bits)]
        (recur (if (zero? bit)
                 (bit-clear address index)
                 (bit-set address index))
               (rest indexes) (rest bits))))))

(defn- compute-addresses [mask address]
  (let [or-mask (make-or-mask mask)
        address (bit-or address or-mask)
        indexes (->> (map-indexed vector (reverse mask))
                     (filter (fn [[_ n]] (= n \X)))
                     (mapv first))
        wildcard-count (Math/round (Math/pow 2 (count indexes)))
        wildcards (->> (map #(cl-format nil "~v,'0B" (count indexes) %) (range 0 wildcard-count))
                       (map #(map (fn [c] (Character/digit c 2)) %)))]
    (->> wildcards
         (map #(compute-address address indexes %))
         set)))

(defn- compute-mem [instructions]
  (loop [instructions instructions
         mask nil
         mem {}]
    (if (empty? instructions)
      mem
      (let [instruction (first instructions)]
        (if (:mask instruction)
          (recur (rest instructions) (:mask instruction) mem)
          (recur (rest instructions) mask
                 (->> (compute-addresses mask (:address instruction))
                      (map #(hash-map % (:value instruction)))
                      (reduce merge mem))))))))

(defn solution-b [filename]
  (->> filename
       slurp-lines
       (map parse-bitfiddle)
       compute-mem
       vals
       (reduce +)))

(comment
  (solution-a "sample-14.txt")
  (solution-a "input-14.txt")
  (solution-b "sample-14.txt")
  )
