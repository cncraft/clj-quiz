(ns quiz-reagent.core
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [quiz-reagent.reagent-components :as rcomp]))

; WebSocket-related defs (sente)
; ==================================

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" ; Note the same path as before
                                  {:type :auto ; e/o #{:auto :ajax :ws}
                                   })]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(declare mount-root)

(declare overview-data)
(declare current-question)
(declare current-question-text)
(declare current-answers)
(declare answer-selected?)
(declare selection)

(declare start-quiz!)
(declare reset-quiz!)
(declare select-answer!)
(declare submit-answer!)
(declare next-question!)

(declare finished?)
(declare submitted?)
(declare correct-and-selected?)
(declare correct-and-not-selected?)
(declare selected-and-wrong?)

(defn answers [answers]
  ; A nice explanation how a component quite similar can be build with only local state can be found under:
  ; http://yogthos.net/posts/2014-07-15-Building-Single-Page-Apps-with-Reagent.html
  [:ul.list-group (when (not (submitted?)) {:style {:cursor "pointer"}})
   (doall
     ; Why the doall?
     ; If we don't wrap the for in a doall we get this warning:
     ; "Reactive deref not supported in lazy seq, it should be wrapped in doall"
     ; And the selection doesn't work anymore (see https://github.com/reagent-project/reagent/issues/18 for details)!
     (for [answer answers]
       ^{:key (:id answer)}
       [:li {:class (str "list-group-item"
                         (cond
                           (or (correct-and-selected? (:id answer))
                               (correct-and-not-selected? (:id answer))) " list-group-item-success"
                           (selected-and-wrong? (:id answer)) " list-group-item-danger"
                           (answer-selected? (:id answer)) " active"))
              :on-click #(select-answer! (:id answer))} (:text answer)]))])

(defn quiz-play []
  [:div
   [:h3 (current-question-text)]
   [answers (current-answers)]
   [:div.row
    [:div.col-xs-11
     [:button {:type "submit"
               :class "btn btn-default"
               :disabled (if (and (selection)
                                  (not (submitted?)))
                           ""
                           "disabled")
               :on-click #(submit-answer!)}
      "Submit"]]
    [:div.col-xs-1
     [:button
      {:type "submit"
       :class "pull-right btn btn-default glyphicon glyphicon-play"
       :disabled (if (and (selection)
                          (submitted?)
                          (not (finished?)))
                   ""
                   "disabled")
       :on-click #(next-question!)}
      "Next"]]]])

(defn start-new-quiz-btn-row []
  [:div.row
   [:div.col-md-4]
   [:div.col-md-4
    (when (and (current-question) (finished?) (submitted?))
      [:button {:type "submit"
                :class "btn-primary btn-lg"
                :on-click #(reset-quiz!)}
       "Start new quiz"]
      )]
   [:div.col-md-4]])

(defn thales-app []
  [:div

   [:div.row
    [:div.col-md-9
     (if (= nil (current-question))
       [rcomp/teaser-overview (overview-data) start-quiz! {:columns 3}]
       [quiz-play])]
    [:div.col-md-3]]

   [:div {:style {:height "30px"}}]

   [:div.row
    [:div.col-md-9
     [start-new-quiz-btn-row]]
    [:div.col-md-3]]

   [:hr]

   [:footer
    [:div.row
     [:div.col-lg-12
      [:p "FmRacner Solutions - 2015"]]]]
   ])

; =============================================================================
; app-state specific part
; =============================================================================

(defonce quiz-state
         (atom {:questions        {}
                :current-question 0
                :overview-data    nil}))

(defn overview-data []
  (:overview-data @quiz-state))

(defn current-question []
  (get (:questions @quiz-state) (:current-question @quiz-state)))

(defn current-question-text []
  (:question (current-question)))

(defn current-answers []
  (:answers (current-question)))

(defn answer-selected? [id]
  (= id (:selection (current-question))))

(defn selection []
  (:selection (current-question)))

(defn fetch-and-set-overview-data! [options]
  (chsk-send! [:thales/overview-data]
              3000
              (fn [response]
                (when (not (= :chsk/timeout response))
                  (swap! quiz-state assoc :overview-data response)

                  (when (:mount-root options)
                    (mount-root))
                  ))))

(defn start-quiz! [quiz-id]
  (chsk-send! [:thales/start-quiz {:quiz-id quiz-id}]
              3000
              (fn [response]
                (when (not (= :chsk/timeout response))
                  (reset! quiz-state {:questions        (:questions response)
                                      :current-question 1})))))

(defn reset-quiz! []
  (reset! quiz-state {:questions        {}
                      :current-question 1})

  (fetch-and-set-overview-data! {:mount-root false}))

(defn select-answer! [id]
  (when (not (submitted?))
    (swap! quiz-state assoc-in [:questions (:current-question @quiz-state) :selection] id)))

(defn submit-answer! []
  (swap! quiz-state assoc-in [:questions (:current-question @quiz-state) :submitted] true))

(defn next-question! []
  (when (submitted?)
    (swap! quiz-state assoc-in [:current-question] (inc (:current-question @quiz-state)))))

(defn finished? []
  (= (:current-question @quiz-state) (count (:questions @quiz-state))))

(defn submitted? []
  (= true (:submitted (current-question))))

(defn correct-and-selected? [id]
  (and (submitted?)
       (= (selection) (:correct-answer (current-question)) id)))

(defn correct-and-not-selected? [id]
  (and (submitted?)
       (= id (:correct-answer (current-question)))
       (not= id (selection))))

(defn selected-and-wrong? [id]
  (and (submitted?)
       (not= (selection) (:correct-answer (current-question)))
       (= id (selection))))

; mounting reagent root to "app"
; ==================================

(defn mount-root []
  (reagent/render [thales-app] (.getElementById js/document "app")))

; WebSocket-related code (sente)
; ==================================

(defn event-msg-handler [{:keys [id ?reply-fn ?data]}]
  (println id)
  (when (= (:first-open? ?data) true)

    ; if :first-open? event occurs the quiz overview data can be fetched from the server (but not before - when
    ; WebSocket is still closed!!)
    (fetch-and-set-overview-data! {:mount-root true})))

(def router (atom nil))

(defn stop-router! []
  (when-let [stop-f @router]
    (stop-f)
    (reset! router nil)))

(defn start-router! []
  (reset! router
          (sente/start-chsk-router! ch-chsk event-msg-handler)))

(defn restart-router! []
  (stop-router!)
  (start-router!))

; init function (entry point of app)
; ==================================

(defn init! []
  (restart-router!)

  #_(mount-root)
  ; This mount-root call was moved to event-msg-handler so it is called only when WebSocket communication is set up and
  ; ready (see fetch-and-set-overview-data! for details).
  )
