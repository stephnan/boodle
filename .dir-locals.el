;;; Directory Local Variables
;;; For more information see (info "(emacs) Directory Variables")

((nil
  (cider-default-cljs-repl . shadow-select)
  (cider-pprint-fn . zprint)
  (cider-preferred-build-tool . clojure-cli)
  (cider-ns-refresh-after-fn . "mount.core/start")
  (cider-ns-refresh-before-fn . "mount.core/stop"))
 (emacs-lisp-mode
  (flycheck-disabled-checkers . "emacs-lisp-checkdoc")))
