(ns aoc-2020.day-07-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-07 :refer [solution-day07 solution-day07-b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day07 "sample-07.txt") 4))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day07-b "sample-07.txt") 32))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day07 "input-07.txt") 370))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day07-b "input-07.txt") 29547))))
