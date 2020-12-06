(ns aoc-2020.day-01-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-01 :refer [solution-day01]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day01 "sample-01.txt" 2) 514579))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day01 "sample-01.txt" 3) 241861950))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day01 "input-01.txt" 2) 1019904))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day01 "input-01.txt" 3) 176647680))))
