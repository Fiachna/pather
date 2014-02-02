(ns pather.ui
  (:use [seesaw.core]))

(def window-id "pather-window")
(def window-title "pather")
(def window-size {:width 1280
                  :height 960})

(def main-window
  (frame :id window-id
            :title window-title
            :width (:width window-size)
            :height (:height window-size)))

(defn create-window []
  (show! main-window))

(defn build-ui [])
