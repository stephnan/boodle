# boodle

[![Build
Status](https://travis-ci.org/manuel-uberti/boodle.svg?branch=master)](https://travis-ci.org/manuel-uberti/boodle)

Simple accounting SPA in Clojure and ClojureScript.

![Screenshot](https://github.com/manuel-uberti/boodle/blob/master/screenshot/01.png)

boodle uses these Clojure/ClojureScript libraries:

- [bidi](https://github.com/juxt/bidi)
- [compojure](https://github.com/weavejester/compojure)
- [compojure-api](https://github.com/metosin/compojure-api)
- [hikari-cp](https://github.com/tomekw/hikari-cp)
- [honeysql](https://github.com/jkk/honeysql)
- [http-kit](http://www.http-kit.org/)
- [mount](https://github.com/tolitius/mount)
- [re-frame](https://github.com/Day8/re-frame)
- [ring](https://github.com/ring-clojure/ring)
- [tongue](https://github.com/tonsky/tongue)

I used [Skeleton](http://getskeleton.com/) to style the UI. The modal panel is a
customised version of
[re-frame-modal](https://github.com/benhowell/re-frame-modal). The date picker
is a customised version of
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

Install [lein](https://leiningen.org/). Compile the ClojureScript files and fire
up [figwheel-main](https://github.com/bhauman/figwheel-main) with:

```console
$ lein build
```

Then fire up the server with:

```console
$ lein run
```

You can now browse at `http://localhost:8080` and interact with boodle.

A production-ready ClojureScript build is available via:

```console
$ lein cljsbuild once min
```

The included `boodle.service` is a basic [systemd
unit](https://www.freedesktop.org/software/systemd/man/systemd.unit.html) that
executes `deploy.sh` to update, build and run boodle automatically when
I start/restart my home server.

## Tests

Unit tests can be run with:

```console
$ lein eftest
```

## License

Copyright © 2017-2018 Manuel Uberti

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
