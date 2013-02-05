(ns turtle.core-test
  (:use clojure.test
        turtle.core))

;; TODO: test all the things.

(defn proc-fixture [f]
  (defcommands [python :as not-python] ls)
  (f))

(use-fixtures :once proc-fixture)

(deftest test-defcommands
  (testing "Read the output of a shell command."
    (is (= 2 (read-string (-> (not-python "-c" "print(1 + 1)")
                              :stdout
                              first))))))

(deftest test-write
  (testing "Test process-write."
    (is (= (let [proc (python "-c" "a = raw_input(''); b = raw_input(''); c = raw_input(''); print(a + b + c)")]
             (process-write proc "foo" "bar" "baz")
             (:stdout proc))
           '("foobarbaz")))))

(deftest test-exit
  (testing "Make sure exit code is 0."
    (is (= 0 (let [proc (ls)]
               (doseq [line (:stdout proc)])
               ((:status proc))))))
  (testing "Can't take the exit code of a process which hasn't finished yet."
    (is (thrown? java.lang.IllegalThreadStateException
                 ((:status (ls)))))))

(deftest test-kill
  (testing "Killing a process shouldn't throw an exception."
    (is (= nil (let [proc (ls)]
                 (:stdout proc) ;; read all output before killing
                 ((:kill proc)))))))
