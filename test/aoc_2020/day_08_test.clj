(ns aoc-2020.day-08-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-08 :refer [solution-day08 solution-day08-b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day08 "sample-08.txt") 5))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day08-b "sample-08.txt") 2))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day08 "input-08.txt") 1859))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day08-b "input-08.txt") 1235))))
