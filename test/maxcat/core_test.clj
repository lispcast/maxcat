(ns maxcat.core-test
  (:require [maxcat.core :as sut]
            [clojure.math.combinatorics :as combo]
            [clojure.test :as t :refer [deftest is]]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]))

(defspec all-digits-included 100
  (prop/for-all [ints (gen/not-empty (gen/vector gen/pos-int))]
    (let [digits (sort (apply str ints))
          tdigits (sort (str (sut/maxcat ints)))]
      (or
       (= [\0] tdigits)
       (= digits tdigits)))))

(defspec return-greatest {:max-size 10}
  (prop/for-all [ints (gen/not-empty (gen/vector gen/pos-int))]
    (let [m (sut/maxcat ints)]
      (every? #(>= m (java.math.BigInteger. (apply str %)))
              (combo/permutations ints)))))

(defspec return-greatest-big-size {:max-size 1000}
  (prop/for-all [ints (gen/tuple gen/pos-int gen/pos-int)]
    (let [m (sut/maxcat ints)]
      (every? #(>= m (java.math.BigInteger. (apply str %)))
              (combo/permutations ints)))))

;; from Val
(deftest return-greatest-with-zeroes
  (is (= 110 (sut/maxcat [10 1]))))

(deftest example-tests
  (is (= 987654321 (sut/maxcat [1 2 3 4 5 6 7 8 9])))
  (is (= 563419912 (sut/maxcat [12 34 56 199])))
  ;; from Steve
  (is (= 92233720368547758072147483647N (sut/maxcat [Integer/MAX_VALUE Long/MAX_VALUE]))))
