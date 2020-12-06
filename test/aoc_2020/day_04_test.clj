(ns aoc-2020.day-04-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-04 :refer [solution-day04 solution-day04b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day04 "sample-04.txt") 2))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day04b "sample-04.txt") 2))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day04 "input-04.txt") 226))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day04b "input-04.txt") 160))))
