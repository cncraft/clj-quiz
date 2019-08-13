(ns quiz-reagent.prod
  (:require [quiz-reagent.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
