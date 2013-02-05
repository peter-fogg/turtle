(ns turtle.core
  (:import java.util.Scanner)
  (require [clojure.java.io :as io]))

;; TODO
;; pipe between multiple processes
;; clean up writing to process

(defmacro defcommands
  "For each command in commands, defines a function of the same name
  which takes any arguments to the shell command and returns a map of
  stdin, stdout, and stderr. If any of the commands is of the form
  [cmd :as other-name], defines a function other-name which runs cmd
  as usual."
  [& commands]
  (cons 'do (for [cmd commands]
              (let [[cmdname fname] (if (vector? cmd)
                                      [(cmd 0) (cmd 2)]
                                      [cmd cmd])]
                `(defn ~fname [& args#]
                   (let [p# (-> (cons ~(str cmdname) args#)
                                ProcessBuilder.
                                .start)]
                     {:stdout (scanner->seq (Scanner. (.getInputStream p#)))
                      :stderr (scanner->seq (Scanner. (.getErrorStream p#)))
                      :stdin (io/writer (.getOutputStream p#))
                      :kill #(.destroy p#)
                      :status #(.exitValue p#)}))))))

(defn process-write
  "Writes lines and closes process stdin stream."
  [proc & lines]
  (doseq [line lines]
    (.write (:stdin proc) (str line "\n")))
  (.close (:stdin proc)))

(defn scanner->seq
  "Takes a java.util.Scanner and returns a lazy sequence consisting of
  the lines of said Scanner's output."
  [scanner]
  (lazy-seq
   (when (.hasNextLine scanner)
     (cons (.nextLine scanner) (scanner->seq scanner)))))

;; (defcommands python)

;; (defn -main [& args]
;;   (let [proc (python "-c" "a = raw_input(''); b = raw_input(''); c = raw_input(''); print(a + b + c)")]
;;     (process-write proc "hell yeah" "bar" "baz")
;;     (doseq [line (:stdout proc)]
;;       (println line))))
