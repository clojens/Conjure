(ns views.templates.record-cell
  (:use conjure.view.base)
  (:require [clojure.tools.string-utils :as conjure-str-utils]
            [conjure.util.conjure-utils :as conjure-utils]))

(def-view [model-name record record-key]
  (let [record-id (:id record)]
    (if (= :id record-key)
      [:td 
        (ajax-link-to record-id
          { :update (success-fn (str "row-" record-id) :replace)
            :action "ajax-show"
            :service model-name,
            :params { :id record-id }
            :html-options
              { :href (conjure-utils/url-for
                        { :action "show", :service model-name, :params { :id (:id record) } }) } })]
      (let [record-key-str (conjure-str-utils/str-keyword record-key)]
        (if (. record-key-str endsWith "_id")
          (let [belongs-to-model (conjure-str-utils/strip-ending record-key-str "_id")
                belongs-to-id (get record record-key)]
            [:td (link-to belongs-to-id { :service belongs-to-model, :action "show", :id belongs-to-id })])
          [:td (get record record-key)])))))