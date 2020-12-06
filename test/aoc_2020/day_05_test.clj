(ns aoc-2020.day-05-test
  (:require [clojure.test :refer :all]
            [aoc-2020.day-05 :refer [solution-day05 solution-day05-b3 solution-day05-samples]]))

(deftest sample-1a
  (testing "Sample should match expected output."
    (is (= (solution-day05-samples) 820))))

;; These are regression tests, basically:
(deftest input-1a
  (testing "Input should match expected output."
    (is (= (solution-day05 "input-05.txt") 888))))

(deftest input-1b
  (testing "Input should match expected output."
    (is (= (solution-day05-b3 "input-05.txt") 522))))
