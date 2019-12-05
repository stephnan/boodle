Boodle is a [single-page
application](https://en.wikipedia.org/wiki/Single-page_application) for
accounting, born out of my desire to learn more about Clojure and
ClojureScript and build something useful for the family.

Overview and rationale
======================

Boodle main features are:

- expense tracking
- monthly budgeting
- savings management
- short-term and long-term goal planning

Boodle tries to be as simple as possible, providing only the
functionalities I need at home. It’s not, and it doesn’t want to be, a
replacement for more powerful tools such as
[ledger](https://www.ledger-cli.org/).

Boodle is intentionally provided as a self-hosted application. It’s been
running on my home server since its first deploy in mid-2017, and I do
not plan to install it and run it elsewhere. I open-sourced it because
it was about time to give something back to the Clojure community, which
over the years has been immensely helpful and kind.

Architecture
============

Tooling
-------

Boodle is built and developed with [Clojure
CLI](https://clojure.org/guides/getting_started) and
[shadow-cljs](http://shadow-cljs.org/).

Originally there were [Leiningen](https://leiningen.org/) and
[Figwheel](https://github.com/bhauman/lein-figwheel) behind the
curtains. I changed developing tools only to explore the Clojure
ecosystem and learn something new, not because there is something wrong
with the previously used tools.

Server-side
-----------

Boodle runs on [PostgreSQL](https://www.postgresql.org/), which is
always my go-to database when it comes to personal projects.

The initialisation is simple. I read the global configuration with
[aero](https://github.com/juxt/aero) to get the details for the database
connection and the HTTP server. Then I call
`boodle.services.http/start-server!`, which makes the datasource
available to all my routes thanks to a handy Ring handler
(`boodle.services.http/add-datasource`) and starts
[http-kit](http://www.http-kit.org/).

Boodle is a single-page application, but to avoid `404` errors on random
page reloading, I added server-side routes matching the corresponding
client-side panels: expenses (`/`), savings (`/savings`), and categories
(`/categories`).

Client-side
-----------

The core of the client-side is
[re-frame](https://github.com/Day8/re-frame), and I tried my best to
stick to its conventions. Every Boodle topic (expenses, savings,
categories, aims, funds, etc.) has its own set of events, subscriptions
and views.

This not only makes the code easier to understand, it also lets me focus
on *what* a panel should do instead of *how*.

I also tried to isolate page components as much as possible. The rule of
thumb is: if there is a subscription involved, that is a component I can
*inject* wherever I need. Take a look at the [expenses
views](https://github.com/manuel-uberti/boodle/blob/master/src/cljs/boodle/expenses/views.cljs)
for an example of what I mean.

Localization
============

Boodle is localized in Italian, but I didn’t want it to be an
Italian-only application. Thanks to
[tongue](https://github.com/tonsky/tongue), localizing
Clojure/ClojureScript applications is easy.

Anything you see on the screen is ready to be translated. If you plan to
change the language, you can find the translations in
[i18n.cljs](https://github.com/manuel-uberti/boodle/blob/master/src/cljs/boodle/i18n.cljs).

You should also check out
[utils.clj](https://github.com/manuel-uberti/boodle/blob/master/src/clj/boodle/utils.clj)
which takes care of the localization of dates and amounts according to
Italian standards.
