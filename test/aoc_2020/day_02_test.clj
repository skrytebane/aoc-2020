(ns aoc-2020.day-02-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-02 :refer [solution-day02 solution-day02-b]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day02 "sample-02.txt") 2))))

(deftest sample-1b
  (testing "Sample should match expected output."
    (is (= (solution-day02-b "sample-02.txt") 1))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day02 "input-02.txt") 542))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day02-b "input-02.txt") 360))))
