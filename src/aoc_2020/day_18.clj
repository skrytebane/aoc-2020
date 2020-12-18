(ns aoc-2020.day-18
  (:require [aoc-2020.core :refer [slurp-lines]]))

(defn- tokenize-expr [line]
  (->> line
       (re-seq #"(\d+)|([()+*])")
       (map (fn [[_ digit operator]]
              (if digit
                (Long/parseLong digit)
                operator)))))

(defn- parse-expr [tokens]
  (println (str (vec tokens)))
  (let [[x op y & r] tokens]
    (cond
      (int? x)
      (case op
        ("+" "*")
        (cond (int? y)
              (if (= (first r) ")")
                (list op x y)
                (list (first r)
                      (parse-expr (rest r))
                      (list op x y)))

              (= y "(")
              (list op x (parse-expr r))))

      (= x ")")
      nil

      (= x "(")
      (parse-expr (rest tokens)))))

(defn- eval-expr [expr]
  (if (int? expr)
    expr
    (let [[op x y] expr]
      (case op
        "+" (+ (eval-expr x) (eval-expr y))
        "*" (* (eval-expr x) (eval-expr y))))))

(defn solution-a [filename]
  (->> filename
       slurp-lines
       (map tokenize-expr)
       (map parse-expr)))

(comment
  (solution-a "sample-18.txt")
  (solution-a "input-18.txt")
  )
