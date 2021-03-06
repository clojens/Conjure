;; This file is used to configure the database and connection.

(ns config.db-config
  (:require [conjure.config.environment :as environment]
            [drift-db-h2.flavor :as h2-flavor]))

(defn dbname [environment]
  (cond
     ;; The name of the production database to use.
     (= environment :production) "conjure_production"

     ;; The name of the development database to use.
     (= environment :development) "conjure_development"

     ;; The name of the test database to use.
     (= environment :test) "conjure_test"))

(defn
#^{:doc "Returns the database flavor which is used by Conjure to connect to the database."}
  create-flavor 
  ([] (create-flavor :production))
  ([environment]
    (h2-flavor/h2-flavor

      ;; Calculates the database to use.
      (dbname environment)

      ;; The directory containing the database files.
      "db/data/")))
            
(defn
  load-config []
  (let [environment (environment/environment-name)
        flavor (create-flavor (keyword environment))]
    (if flavor
      flavor
      (throw (new RuntimeException (str "Unknown environment: " environment ". Please check your conjure.environment system property."))))))
      