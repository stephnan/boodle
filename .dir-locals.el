;;; Directory Local Variables
;;; For more information see (info "(emacs) Directory Variables")

((nil
  (cider-refresh-before-fn . "mount.core/stop")
  (cider-refresh-after-fn . "mount.core/start")
  (cider-default-cljs-repl . "Figwheel"))
 (emacs-lisp-mode
  (flycheck-disabled-checkers . "emacs-lisp-checkdoc")))
