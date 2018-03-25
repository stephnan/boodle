# boodle

[![Build
Status](https://travis-ci.org/manuel-uberti/boodle.svg?branch=master)](https://travis-ci.org/manuel-uberti/boodle)

Simple accounting SPA in Clojure and ClojureScript.

![Screenshot](https://github.com/manuel-uberti/boodle/blob/master/screenshot/01.png)

boodle uses these Clojure/ClojureScript libraries:

- [bidi](https://github.com/juxt/bidi)
- [compojure](https://github.com/weavejester/compojure)
- [compojure-api](https://github.com/metosin/compojure-api)
- [dire](https://github.com/MichaelDrogalis/dire)
- [hikari-cp](https://github.com/tomekw/hikari-cp)
- [http-kit](http://www.http-kit.org/)
- [mount](https://github.com/tolitius/mount)
- [re-frame](https://github.com/Day8/re-frame)
- [ring](https://github.com/ring-clojure/ring)
- [tongue](https://github.com/tonsky/tongue)

boodle runs on [PostgreSQL](https://www.postgresql.org) 9.4.15 and 9.6.8, and
uses [Skeleton](http://getskeleton.com/) to style the UI.

The modal panel is a customised version of
[re-frame-modal](https://github.com/benhowell/re-frame-modal).

The date picker is a customised version of
[cljs-pikaday](https://github.com/timgilbert/cljs-pikaday).

## Usage

To run boodle, install and configure [PostgreSQL](https://www.postgresql.org) on
your machine. Set up the database with the necessary tables and permissions you
find in
[model.sql](https://github.com/manuel-uberti/boodle/blob/master/resources/sql/model.sql).

Install [yarn](https://yarnpkg.com/en/) and [gulp](https://gulpjs.com/), and
then run:

```console
$ cd resources/src
$ yarn
$ gulp
```

Install [lein](https://leiningen.org/) and compile the ClojureScript files with:

```console
$ lein cljsbuild once dev
```

Otherwise, you can run `lein figwheel` if you want a REPL to play with the
browser. Then fire up the server with:

```console
$ lein run
```

You can now browse at `http://localhost:8080` and interact with boodle. Press
<kbd>Ctrl+h</kbd> to instrument and inspect boodle using
[re-frame-10x](https://github.com/Day8/re-frame-10x).

A production-ready build is available via:

```console
$ lein cljsbuild once min
```

## Tests

Unit tests can be run with:

```console
$ lein eftest
```

## License

Copyright © 2017-2018 Manuel Uberti

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
