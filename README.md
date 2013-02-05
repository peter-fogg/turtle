# turtle

A simple Clojure library for executing shell commands as subprocesses. Just define the commands you want to use, and use them as functions returning a map with keys `[:stdout :stderr :stdin :kill :status]`.

## Usage

```clj
user=> (defcommands python)
#'user/python
user=> (let [proc (python "-c" "print('hello, world!')")]
         (println (str "Python says \"" (first (:stdout proc)) "\"")))
```

## License

Copyright © 2013 Peter Fogg.

Distributed under the Eclipse Public License, the same as Clojure.
