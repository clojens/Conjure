(ns generators.migration-generator
  (:import [java.io File])
  (:require [conjure.migration.migration :as migration]
            [conjure.util.file-utils :as file-utils]))

(defn
#^{:doc "Prints out how to use the generate migration command."}
  migration-usage []
  (println "You must supply a migration name (Like migration-name).")
  (println "Usage: ./run.sh script/generate.clj migration <migration name>"))
  
(defn
#^{:doc "Generates the migration content and saves it into the given migration file."}
  generate-file-content [migration-file migration-name up-content down-content]
  (let [migration-number (migration/migration-number-from-file migration-file)
        migration-namespace (migration/migration-namespace migration-file)
        content (str "(ns " migration-namespace "
  (:use [conjure.server.jdbc-connector :as jdbc-connector]))

(defn
#^{:doc \"Migrates the database up to version " migration-number ".\"}
  up []
  " (if up-content up-content (str "(println \"" migration-namespace " up...\")"))")
  
(defn
#^{:doc \"Migrates the database down from version " migration-number ".\"}
  down []
  " (if down-content down-content (str "(println \"" migration-namespace " down...\")"))")")]
    (file-utils/write-file-content migration-file content)))

(defn
#^{:doc "Creates the migration file from the given migration-name."}
  generate-migration-file 
    ([migration-name] (generate-migration-file migration-name nil nil))
    ([migration-name up-content down-content]
      (if migration-name
        (let [db-directory (migration/find-db-directory)]
          (if db-directory
            (let [migrate-directory (migration/find-or-create-migrate-directory db-directory)
                  migration-file (migration/create-migration-file migrate-directory migration-name)] 
              (generate-file-content migration-file migration-name up-content down-content))
            (println "Could not find db directory.")))
        (migration-usage))))

(defn 
#^{:doc "Generates a migration file for the migration name given in params."}
  generate-migration [params]
  (generate-migration-file (first params)))