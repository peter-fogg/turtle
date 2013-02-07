# turtle

A simple Clojure library for executing shell commands as subprocesses. Just define the commands you want to use, and use them as functions returning a map with information about said process.

## Usage

```clj
user=> (defcommands python grep ls sort)
#'user/python
user=> (let [proc (python "-c" "print('hello, world!')")]
         (println (str "Python says \"" (first (:stdout proc)) "\"")))
Python says "hello, world!"
nil
user=> (doseq [line (pipe (ls "/Users/yourname/") (grep "foo") (sort "-r"))]
         (println line))
this_file_has_food.c
ilikefood
foo.clj
nil
user=> (process-write (python "yourscript.py") ["foo" "bar" "baz"])
nil
user=> (let [proc (long-running-process "args")]
         ((:kill proc))
	 (println (str "Status was " ((:status proc)) ".")))
Status was 1.
```

## Installation
Just add `[turtle "0.1.0"]` to your `project.clj`. Shiny.

## License

Copyright © 2013 Peter Fogg.

Distributed under the Eclipse Public License, the same as Clojure.
