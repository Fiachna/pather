(ns pather.grid
  (:use [seesaw.core]
        [seesaw.widgets.log-window]
        [pather.ui.view]))

(def cell-w 10)
(def cell-h 10)

(def num-rows 30)
(def num-cols 30)

(def grid
  (atom nil))

(defrecord Cell [traversable metric discomfort neighbours direction occupant])

(defn build-grid [grid-rows grid-cols]
  (apply vector
         (map (fn [_]
                (apply vector
                       (map (fn [_]
                              (ref (Cell. true 0 500 [] nil nil)))
                            (range grid-cols))))
              (range grid-rows))))

(defn get-cell [grid-atom [x y]]
  (-> @grid-atom (nth x) (nth y)))

(defn cell-value [grid-atom [x y] key]
  (key @(get-cell grid-atom [x y])))

(defn modify-cell [grid-atom [x y] key value]
  (dosync
   (alter (get-cell grid-atom [x y]) assoc-in [key] value)))
