(ns itmo-functional-programming.lab1.task_7-test
  (:require [clojure.test :refer :all]
            [itmo-functional-programming.lab1.task-7 :refer :all]))

(deftest seven-task-loop-test
  (testing "7-th task, loop."
    (is (= (n-th-prime-number-loop 10001) 104743))))

(deftest seven-task-filter-test
  (testing "7-th task, filter"
    (is (= (n-th-prime-number-filtered 10001) 104743))))