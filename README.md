Simple accounting SPA in Clojure and ClojureScript.

![Screenshot](https://github.com/manuel-uberti/boodle/blob/master/resources/img/screenshot.png)

boodle uses these Clojure/ClojureScript libraries:

- [aero](https://github.com/juxt/aero)
- [bidi](https://github.com/juxt/bidi)
- [compojure](https://github.com/weavejester/compojure)
- [honeysql](https://github.com/jkk/honeysql)
- [http-kit](http://www.http-kit.org/)
- [next.jdbc](https://github.com/seancorfield/next-jdbc)
- [re-frame](https://github.com/Day8/re-frame)
- [ring](https://github.com/ring-clojure/ring)
- [tongue](https://github.com/tonsky/tongue)

I used [Bulma](https://bulma.io/) for the UI and customised
[re-frame-modal](https://github.com/benhowell/re-frame-modal) and
[cljs-pikaday](https://github.com/timgilbert/cljs-pikaday) to play well
with it. Check the
[documentation](https://github.com/manuel-uberti/boodle/blob/master/doc/index.adoc)
for more details.

Usage
=====

Prerequisites
-------------

To run boodle, you need:

- a Java JDK/JRE suitable for your system (I use
  [OpenJDK](https://openjdk.java.net/))
- [Clojure](https://clojure.org/guides/getting_started)
- [PostgreSQL](https://www.postgresql.org)
- [yarn](https://yarnpkg.com/en/)
- [gulp](https://gulpjs.com/)
- [shadow-cljs](http://shadow-cljs.org/)

Database setup
--------------

Set up the database with the necessary tables and permissions you find
in
[model.sql](https://github.com/manuel-uberti/boodle/blob/master/resources/sql/model.sql).
Check also `resources/config/config.edn` to adjust the database
connection parameters.

Compiling
---------

From the project root, run:

    $ yarn
    $ cd resources/src
    $ gulp

Compile the ClojureScript files with:

    $ npx shadow-cljs compile boodle

Running
-------

From the project root, fire up the server with:

    $ clj -A:run

You can now browse at `http://localhost:8080` and interact with boodle.

Docker and Docker Compose
-------------------------

Thanks to [Moritz Marquardt](https://github.com/moqmar), you can also
run boodle via [Docker](https://docs.docker.com/get-started/) and
[Docker Compose](https://docs.docker.com/compose/overview/):

    $ git clone https://github.com/manuel-uberti/boodle.git && cd boodle
    $ docker-compose up -d

You can now access boodle at `http://localhost:8080`.

Tests
=====

Unit tests are configured with
[kaocha](https://github.com/lambdaisland/kaocha) and can be run from the
project root with:

    $ ./bin/kaocha

Linting
=======

I use [clj-kondo](https://github.com/borkdude/clj-kondo) to check my
code. As per its instructions, you should create a `.clj-kondo`
directory at the root of `boodle` and run:

    $ clj-kondo --lint (clj -Spath) --cache

Note that I am using a slightly different syntax from the one suggested
[here](https://github.com/borkdude/clj-kondo#project-setup) because
I use Fish shell. Refer to the `clj-kondo` README if you want to know
more.

License
=======

Copyright © (iterate inc 2017) Manuel Uberti

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
