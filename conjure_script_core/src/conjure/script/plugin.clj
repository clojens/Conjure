(ns conjure.script.plugin 
  (:require [conjure.server.server :as server]
            [conjure.plugin.util :as plugin-util]))

(defn print-usage []
  (println "Usage: lein conjure plugin <install|uninstall> <plugin name> <arguments>"))

(defn print-unknown-plugin [plugin-name]
  (println (str "Could not find plugin with name: " plugin-name))
  (print-usage))

(defn print-invalid-plugin [plugin-name]
  (println (str "Invalid plugin: " plugin-name ". The plugin must implement install, uninstall and initialize functions.")))

(defn install [plugin-name arguments]
  (let [install-fn (plugin-util/install-fn plugin-name)]
    (if install-fn
      (install-fn arguments)
      (print-invalid-plugin plugin-name))))

(defn uninstall [plugin-name arguments]
  (let [uninstall-fn (plugin-util/uninstall-fn plugin-name)]
    (if uninstall-fn
      (uninstall-fn arguments)
      (print-invalid-plugin plugin-name))))

(defn
  run [args]
  (server/init)
  
  (let [type-command (first args)
        plugin-name (second args)
        arguments (drop 2 args)]
    (if plugin-name
      (cond 
        (= type-command "install") (install plugin-name arguments)
        (= type-command "uninstall") (uninstall plugin-name arguments)
        true (print-usage))
      (print-usage))))