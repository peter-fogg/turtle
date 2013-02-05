(ns turtle.core-test
  (:use clojure.test
        turtle.core))

;; TODO: test all the things.

(defn python-fixture [f]
  (defcommands [python :as not-python])
  (f))

(use-fixtures :once python-fixture)

(deftest test-defcommands
  (testing "Read the output of a shell command."
    (is (= 2 (read-string (first (:stdout (not-python "-c" "print(1 + 1)"))))))))

(deftest test-write
  (testing "Test process-write."
    (is (= (let [proc (python "-c" "a = raw_input(''); b = raw_input(''); c = raw_input(''); print(a + b + c)")]
             (process-write proc "foo" "bar" "baz")
             (:stdout proc))
           '("foobarbaz")))))
