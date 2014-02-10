(ns pather.ui.actions
  (:use [seesaw.core]
        [seesaw.widgets.log-window]
        [pather.ui.view]))

(def brush
  (atom {
         :brush nil
         :tool nil
         :size 1
         :intensity 100}))

(defn square-brush)
(defn round-brush)

(def brushes {
              :square-brush nil
              :round-brush nil})

(def tools {
            :obstacle nil
            :discomfort nil
            :eraser nil
            :actor nil
            :goal nil})

(listen toolbar-right :mouse-released (fn [e] (logf message-panel "Selection is %s\n" (selection e))))
(listen (select toolbar-top [:.mode]) :selection (fn [e] (if (selection e)
                                                        (logf message-panel "To %s\n" (id-of e))
                                                        (logf message-panel "From %s\n" (id-of e)))))
(listen (select toolbar-top [:.view]) :selection (fn [e] (if (selection e)
                                                          (logf message-panel "To %s\n" (id-of e))
                                                          (logf message-panel "From %s\n" (id-of e)))))
