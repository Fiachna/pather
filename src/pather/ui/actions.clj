(ns pather.ui.actions
  (:use [seesaw.core]
        [seesaw.graphics]
        [seesaw.behave]
        [seesaw.widgets.log-window]
        [pather.ui.view]
        [pather.grid]))

(def brush
  (atom {
         :brush nil
         :tool nil
         :size 1
         :intensity 100}))

(defn mouse-coords-to-grid [[pos-x pos-y :as mouse-pos] [min-col min-row max-col max-row :as bounds]]
  (let [x-coord (- pos-x (* min-col cell-w) 1)
        y-coord (- pos-y (* min-row cell-h) 3)
        col (if (>= x-coord (* max-col cell-w))
              (- max-col 1)
              (if (<= x-coord 0)
                0
                (quot x-coord cell-w)))
        row (if (>= y-coord (* max-row cell-h))
              (- max-row 1)
              (if (<= y-coord 0)
                0
                (quot y-coord cell-h)))]
    {:col col
     :row row}))

(defn standard-draw-cell [_ g [min-x min-y max-x max-y :as bounds] x y grid-atom]
  (let [traversable? (cell-value grid-atom [x y] :traversable)
        cell-color (if traversable? "#000D5E" "#F00")]
    (draw g
          (rect (* (- x min-x) cell-w) (* (- y min-y) cell-h) cell-w cell-h)
          (style :background cell-color :foreground "#000"))))

(defn draw-grid [_ g [min-x min-y max-x max-y :as bounds] grid-atom]
  (let [rows (- max-y min-y)
        cols (- max-x min-x)]
    (doseq [y (range rows)
            x (range cols)]
      (standard-draw-cell _ g bounds x y grid-atom))))

(when-mouse-dragged viewport-canvas
                    :start (fn [e]
                             (let [p (.getPoint e)
                                  coords (mouse-coords-to-grid [(.x p) (.y p)] [0 0 num-cols num-rows])]
                              (logf message-panel "col: %s row: %s mouse-x: %d mouse-y %d\n" (str (coords :col)) (str (coords :row)) (.x p) (.y p))
                              (modify-cell grid [(coords :col) (coords :row)] :traversable (not (cell-value grid [(coords :col) (coords :row)] :traversable)))
                              (repaint! viewport-canvas)))
                    :drag  (fn [e [dx dy]]
                            (let [p (.getPoint e)
                                  coords (mouse-coords-to-grid [(.x p) (.y p)] [0 0 num-cols num-rows])]
                              (logf message-panel "col: %s row: %s mouse-x: %d mouse-y: %d\n" (str (coords :col)) (str (coords :row)) (.x p) (.y p))
                              (modify-cell grid [(coords :col) (coords :row)] :traversable (not (cell-value grid [(coords :col) (coords :row)] :traversable)))
                              (repaint! viewport-canvas))))

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
