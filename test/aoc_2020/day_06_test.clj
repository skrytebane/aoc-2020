(ns aoc-2020.day-06-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-06 :refer [solution-day06 solution-day06-b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day06 "sample-06.txt") 11))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day06-b "sample-06.txt") 6))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day06 "input-06.txt") 6763))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day06-b "input-06.txt") 3512))))
