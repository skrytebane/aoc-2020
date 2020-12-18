(ns aoc-2020.day-18
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- tokenize-expr [line]
  (->> line
       (re-seq #"(\d+)|([()+*])")
       (map (fn [[_ digit operator]]
              (if digit
                (Long/parseLong digit)
                operator)))))

(declare evaluate-expr)

;; (1 + (2 + (3 + 4 + 5)))
(defn- evaluate-paren [tokens]
  (let [[acc remaining] (evaluate-expr (rest tokens))]
    [acc (drop 2 remaining)]))

(defn- evaluate-expr [tokens]
  (let [token (first tokens)]
    (cond
      (int? token)
      (let [operator (first (rest tokens))
            [acc remaining] (evaluate-expr (drop 2 tokens))]
        (case operator
          "*" [(* token acc) remaining]
          "+" [(+ token acc) remaining]))

      (= "(" token)
      (let [[acc remaining] (evaluate-paren tokens)]
        (if remaining
          2111
          acc)))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map tokenize-expr)
       (map evaluate-expr)))

(comment
  (solution-a "sample-18.txt")
  (solution-a "input-18.txt")
  )
