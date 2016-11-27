(ns hyper-template.core-test
  (:require [clojure.test :refer :all]
            [hyper-template.core :refer :all]))

(deftest str-test
  (testing "simple string 1"
    (is (= (hstr #(apply str (into [] %))
                 {:s (fn [a _] (str "-" a "-"))}
                 ["a" :s:b:1] 
                 {:b "b"}) 
           "a-b-"))))

(deftest sql-str-test
  (testing "simple sql string 1"
    (is (= (hstr #(apply str (into [] %))
                 {:limit  
                  (fn [v _] 
                    (if v 
                      (str " limit " v)
                    v))
                  }
                 ["a" :limit:limit] 
                 {:limit 9}) 
           "a limit 9"))))

(deftest sql-str-test-2
  (testing "simple sql string 2"
    (is (= (hstr #(apply str (into [] %))
                 {:limit  
                  (fn [v _] 
                    (if v 
                      (str " limit " v)
                    v))
                  }
                 ["a" :limit:limit] 
                 {}) 
           "a"))))

(deftest sql-str-test-3
  (testing "simple sql string 3"
    (is (= (hstr #(apply str (into [] %))

                 {:from (fn [v _]
                          (let [[tname alias-1] v]
                            (str " from " tname " " alias-1)))

                  :where (fn [v _]
                           (let [[expr col-v] v]
                             (if (vector? col-v)
                               (if-not (empty? col-v)
                                 (str " where " expr 
                                      " (" (clojure.string/join " " col-v)
                                      ")")))))

                  :limit  (fn [v _] 
                            (if v 
                              (str " limit " v)
                              v))}

                 ["select" 
                  :from:from 
                  :where:where
                  :limit:limit] 

                 {:from ["tab-1" "t1"]
                  :where ["c in" [1 2]]
                  }) 
           "select from tab-1 t1 where c in (1 2)"))))
