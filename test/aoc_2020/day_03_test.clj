(ns aoc-2020.day-03-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-03 :refer [solution-day03 solution-day03b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day03 "sample-03.txt") 7))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day03b "sample-03.txt") 336))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day03 "input-03.txt") 198))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day03b "input-03.txt") 5140884672))))
