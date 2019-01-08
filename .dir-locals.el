;;; Directory Local Variables
;;; For more information see (info "(emacs) Directory Variables")

((nil
  (cider-ns-refresh-after-fn . "mount.core/start")
  (cider-ns-refresh-before-fn . "mount.core/stop")
  (cider-preferred-build-tool . clojure-cli)
  (cider-pprint-fn . zprint))
 (emacs-lisp-mode
  (flycheck-disabled-checkers . "emacs-lisp-checkdoc")))
