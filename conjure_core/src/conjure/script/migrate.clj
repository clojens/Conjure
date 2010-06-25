(ns conjure.script.migrate
  (:require [clojure.contrib.command-line :as command-line]
            [conjure.core.migration.runner :as runner]
            [conjure.core.server.server :as server]))

(defn 
#^{:doc "Prints out how to use the migration command."}
  print-usage []
  (println "Usage: ./run.sh script/migrate.clj [VERSION=<version number>]"))

(defn
#^{:doc "Gets the version number from the passed in version parameter. If the given version string is nil, then this method returns Integer.MAX_VALUE. If the version parameter is invalid, then this method prints an error and returns nil."}
  version-number [version]
  (if version
    (if (string? version)
      (. Integer parseInt version)
      version)
    (. Integer MAX_VALUE)))

(defn
  migrate [mode version]
  (server/set-mode mode)
  (server/init)
  (runner/update-to-version (version-number version)))

(defn
  run [args]
  (command-line/with-command-line args
    "lein conjure migrate [options]"
    [ [version "The version to migrate to. Example: -version 0 -> migrates to version 2." nil]
      [mode "The server mode. For example, development, production, or test." nil]
      remaining]
  
    (migrate mode version)))

