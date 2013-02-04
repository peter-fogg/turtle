(ns turtle.core
  (:import java.util.Scanner)
  (require [clojure.java.io :as io]))

;; TODO
;; make check-command throw a better exception
;; work out [ls :as not-ls] syntax
;; pipe between multiple processes

(defn- check-command [cmd]
  (if (and (vector? cmd) (= 3 (count cmd)))
    (if (= (second cmd) :as)
      (cmd 2)
      (throw (Exception. "")))
    cmd))

(defmacro defcommands
  "For each command in commands, defines a function of the same name
  which takes any arguments to the shell command and returns a map of
  stdin, stdout, and stderr."
  [& commands]
  (cons 'do (for [cmd commands]
              `(defn ~cmd [& args#]
                 (let [p# (.start (ProcessBuilder. (cons ~(str cmd) args#)))]
                   {:stdout (scanner->seq (Scanner. (.getInputStream p#)))
                    :stderr (scanner->seq (Scanner. (.getErrorStream p#)))
                    :stdin (io/writer (.getOutputStream p#))})))))

(defn scanner->seq
  "Takes a java.util.Scanner and returns a lazy sequence consisting of
  the lines of said Scanner's output."
  [scanner]
  (lazy-seq
   (when (.hasNextLine scanner)
     (cons (.nextLine scanner) (scanner->seq scanner)))))

(defcommands pwd)

(defn -main [& args]
  (for [cmd (:stdout (pwd))]
    (println cmd)))