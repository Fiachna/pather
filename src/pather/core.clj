(ns pather.core
  (:gen-class))

(use 'pather.ui)

(defn -main
  "Initialise the app, get the UI built and begin the main loop"
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (create-window))
