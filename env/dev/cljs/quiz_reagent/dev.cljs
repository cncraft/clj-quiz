(ns ^:figwheel-no-load quiz-reagent.dev
  (:require [quiz-reagent.core :as core]
            [figwheel.client :as figwheel :include-macros true]
            [reagent.core :as r]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback core/mount-root)

(core/init!)
