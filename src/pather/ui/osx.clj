(ns pather.ui.osx
  (:use seesaw.core)
  (:import
   [com.apple.eawt Application ApplicationListener]
   [java.awt.image BufferedImage]))

;;osx handling stuff from https://gist.github.com/actsasgeek/2720254

(def OS_NAME (System/getProperty "os.name"))

(defn event-not-handled [e] (.setHandled e false))
(defn event-handled [e] (.setHandled e true))

(def ^:private default-application-fn (fn [e] (event-not-handled e)))

(def ^:private application-defaults {
                                     :about default-application-fn
                                     :open-application default-application-fn
                                     :open-file default-application-fn
                                     :preferences default-application-fn
                                     :print-file default-application-fn
                                     :quit default-application-fn
                                     :re-open-application default-application-fn})

(defn application
  "This function returns the com.apple.eawt.Application instance accessed through
Application.getApplication()

If provided, the options configure the Application through the Application Listener interface. The
default behavior for all function is to pass the ApplicationEvent through unhandled, ie,
event.isHandled(false).

Each option takes an function a value. This function should take one argument, e: the ApplicationEvent
and execute (event-handled e) as the last expression. Most of these events are associated with
scripting in OS X (AppleScript). However, it is very likely that many users might want to customize
the About, Preferences and Quit menu items. Note that by default, the Preferences menu item is not
visible but providing a function to handle it will make it visible.

The options are:

  :about                a function to show a customized About frame.
                        the argument e is an ApplicationEvent.
  :open-application     a function to handle an OpenApplication event from the Finder or elsewhere.
  :open-file            a function to handle an OpenDocument event from the Finder or elsewhere.
  :preferences          a function called when the Preferences menu item is selected. The preferences
                        menu item does not show by default. Providing a custom handler for the
                        preferences menu causes it to be shown automatically
  :print-file           a function to handle a Print Document event from the Finder or elsewhere.
  :quit                 a function to customize the quit command of the default application menu. Note
                        that most OSes put the quit command elsewhere such in the File menu. If your
                        application is meant to be cross-platform you'll need to conditionally control
                        how you handle Quit or you could end up with several Quit menu items.
  :re-open-application  a function to handle a ReopenApplication event from the Finder or elsewhere.

Note that none of these functions is expected to return anything. If necessary you'll need to modify
application state in them."
  ([] (Application/getApplication))
  ([& {:as options}]
     (let [
           a (application)
           {:keys [about open-application open-file preferences print-file quit re-open-application]} (merge application-defaults options)]
       (if (options :preferences)
         (.setEnabledPreferencesMenu a true))
       (.addApplicationListener a
                                (reify ApplicationListener
                                  (handleAbout [this event]
                                    (about event))
                                  (handleOpenApplication [this event]
                                    (open-application event))
                                  (handleOpenFile [this event]
                                    (open-file event))
                                  (handlePreferences [this event]
                                    (preferences event))
                                  (handlePrintFile [this event]
                                    (print-file event))
                                  (handleQuit [this event]
                                    (quit event))
                                  (handleReOpenApplication [this event]
                                    (re-open-application event))))
       a)))

(defn application-icon
  "Grabs the Dock Icon Image and scales it as requested. Uses java.awt.Image/SCALE_REPLICATE

  w        desired width of new image.
  h        desired height of new image."
  [w h] (.getScaledInstance (.getDockIconImage (application)) w h BufferedImage/SCALE_REPLICATE))

(defn center-frame!
  "This centers any frame. If only f (the frame) is supplied, it centers the frame on the monitor.
If a component is provided as well, it should center f (the frame) over that component"
  ([f] (.setLocationRelativeTo f nil)
     f)
  ([f component]
     (.setLocationRelativeTo component)
     f))

(defn about-window [app-name content]
  "This function returns a function that creates a Frame that can be used as a replacement action
for the About... menu item.

  app-name       the name of your application
  content        the contents of the frame."
  (fn [e]
    (let [w (frame :title (str "About " app-name) :content content)]
      (-> w pack! center-frame! show!)
      (event-handled e))))

(defn quit-fn [e]
  (System/exit 0)
  (event-handled e))

(def about-content
  (border-panel :hgap 10 :vgap 10
                :west (vertical-panel :items [(label :icon (application-icon 48 48) :size [48 :by 48])])
                :east (vertical-panel :items
                                      [(label :text "Pather" :font "LUCIDAGRANDE-BOLD-24")
                                       (text
                                        :editable? false
                                        :background "#EDEDED"
                                        :multi-line? true
                                        :text "A pathfinding test app written in clojure.\nLicensed under the Eclipse Public License\nhttp://www.eclipse.org/legal/epl-v10.html")])))

(if (= "Mac OS X" OS_NAME)
  (application
   :about (about-window "Pather" about-content)
   :quit quit-fn))
