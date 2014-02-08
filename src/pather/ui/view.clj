(ns pather.ui.view
  (:use [seesaw core border table mig]
        [seesaw.widgets.log-window]))

(native!)

(def window-id "pather-window")
(def window-title "pather")
(def window-size {:width 1280
                  :height 960})

(def divider-color "#aaaaaa")
(def default-border (line-border :color divider-color))

(def file-menu
  (menu :text "File" :items ["Item1" "Item2" "Item3"]))
(def edit-menu
  (menu :text "Edit" :items ["Item1" "Item2" "Item3"]))
(def view-menu
  (menu :text "View" :items ["Item1" "Item2" "Item3"]))

(def main-menubar
  (menubar :items [file-menu edit-menu view-menu]))

(def main-window
  (frame :id window-id
            :title window-title
            :width (:width window-size)
            :height (:height window-size)
            :menubar main-menubar))

(def log-panel
  (log-window :border default-border
              :id "log-panel"
              :limit 4096
              :auto-scroll? true
              :editable? false))

(def toolbar-right
  (listbox :border default-border
           :model ["Obstacle" "Discomfort" "Eraser" "Agent" "Goal"]))

(def viewport-canvas
  (scrollable
   (canvas)))

(def toolbar-top
  (let [mode-group (button-group)
        view-group (button-group)]
    (toolbar :floatable? false
             :orientation :horizontal
             :border default-border
             :items [(toggle :id :default-mode :text "Default" :group mode-group :selected? true)
                     (toggle :id :enhanced-mode :text "Enhanced" :group mode-group)
                     (toggle :id :flowfield-mode :text "Flowfield" :group mode-group)
                     :separator
                     (toggle :id :standard-view :text "Standard" :group view-group :selected? true)
                     (toggle :id :node-view :text "Nodes" :group view-group)
                     (toggle :id :heatmap-view :text "Heatmap" :group view-group)
                     (toggle :id :flowfield-view :text "Flowfield" :group view-group)
                     :separator
                     (label "Grid Size x:")
                     (spinner :id :grid-x :model 200)
                     (label "y:")
                     (spinner :id :grid-y :model 200)])))

(def frame-content
  (mig-panel :constraints ["wrap 2"
                           "[80%][20%]"
                           "[40px][80%][20%]"]
             :border (line-border :color "#aaaaaa")
             :items [[toolbar-top "spanx, grow"]
                     [viewport-canvas "grow"]
                     [toolbar-right "spany, grow"]
                     [log-panel "grow"]]))

(defn create-window []
  (show! main-window)
  main-window)

(defn build-ui []
  (config! main-window :content frame-content)
  (logf log-panel "Hello World!")
  main-window)
