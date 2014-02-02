(ns pather.ui.view
  (:use [seesaw.core]
        [seesaw.widgets.log-window]
        [seesaw.mig]))

(native!)

(def window-id "pather-window")
(def window-title "pather")
(def window-size {:width 1280
                  :height 960})

(def file-menu
  (menu :text "File" :items ["Item1" "Item2" "Item3"]))
(def edit-menu
  (menu :text "Edit" :items ["Item1" "Item2" "Item3"]))

(def main-menubar
  (menubar :items [file-menu edit-menu]))

(def main-window
  (frame :id window-id
            :title window-title
            :width (:width window-size)
            :height (:height window-size)
            :menubar main-menubar))

(def log-panel
  (log-window :id "log-panel"
              :limit 4096
              :auto-scroll? true
              :editable? false))

(def frame-content
  (mig-panel :constraints ["", ""]
             :items [
                     ["Tools"]
                     ["Logs"]]))

(defn create-window []
  (show! main-window)
  main-window)

(defn build-ui []
  (config! main-window :content frame-content)
  (logf log-panel "Hello World!")
  main-window)
