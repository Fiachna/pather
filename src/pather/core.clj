(ns pather.core
  (:gen-class))
(use 'seesaw.core)
(use 'pather.ui.osx)
(use 'pather.ui.view)
(use 'pather.ui.actions)
(use 'pather.grid)

(defn -main
  "Initialise the app, get the UI built and begin the main loop"
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (create-window)
  (build-ui)
  (reset! grid
          (build-grid num-rows num-cols))
  (config! viewport-canvas :paint #(draw-grid %1 %2 [0 0 num-cols num-rows] grid)))
