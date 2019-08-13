(ns quiz-reagent.server
  (:require [quiz-reagent.handler :refer [app]]
            [org.httpkit.server :as httpkit])
  (:gen-class))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
     (httpkit/run-server #'app {:port port :join? false})
     (quiz-reagent.handler/start-router!)))
