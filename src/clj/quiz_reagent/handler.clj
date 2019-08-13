(ns quiz-reagent.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :as c-handler]
            [hiccup.page :as page]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]
            [ring.util.response :as resp]
            [selmer.parser :refer [render-file]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (sente-web-server-adapter)]
            [quiz-reagent.partials :as p]
            [quiz-reagent.quiz-defs :as quiz-defs]
            [quiz-reagent.database :as db]
            [clojure.string :as str]
            [cemerick.friend :as friend]
            [cemerick.friend.credentials :as creds]
            [cemerick.friend.workflows :as workflows]))

(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! sente-web-server-adapter {})]
  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(defonce router (atom nil))

(defn event-msg-handler [{:keys [id ?reply-fn ?data]}]
  (when (= id :thales/start-quiz)
    (when ?reply-fn
      (?reply-fn (get quiz-defs/quizes (:quiz-id ?data)))))

  (when (= id :thales/overview-data)

    (println (str "user '1' stats: " (db/fetch-stats-by-user-id 1)))

    (when ?reply-fn
      (?reply-fn (->> quiz-defs/quizes
                      quiz-defs/sorted
                      vals
                      (map #(dissoc %1 :questions)))))))

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

(defn home []
  (page/html5
    (p/head "Quiz World 2")
    [:body
     (p/nav-bar)
     [:div.container
      (p/home-content "Quote Quiz World 2")
      [:hr]
      (p/footer)]]))

(defn- create-user
  [{:keys [email password] :as user-data}]
  (-> (dissoc user-data :email)
      (assoc :identity email
             :password (creds/hash-bcrypt password))))

(defn- signup [flash]
  (page/html5
    (p/head "Quiz World 2 - Sign up")
    [:body
     (p/nav-bar)
     [:div.container
      (p/signup-content flash)
      [:hr]
      (p/footer)]]))

(defn- login [{:keys [username login_failed] :as params}]
  (println username)
  (println login_failed)
  (page/html5
    (p/head "Quiz World 2 - Log in")
    [:body
     (p/nav-bar)
     [:div.container
      (p/login-content login_failed)
      [:hr]
      (p/footer)]]))

(defroutes routes

           (GET "/" [] (home))

           (GET "/signup" req (signup (:flash req)))

           (POST "/signup" {{:keys [email password confirm] :as params} :params :as req}
             (if (and (not-any? str/blank? [email password confirm])
                      (= password confirm))
               (let [user (create-user (select-keys params [:email :password]))]
                 (db/insert-user! (:identity user) (:password user))
                 (friend/merge-authentication (resp/redirect "/overview") user))
               (assoc (resp/redirect "/signup") :flash :password-unequal)))

           (GET "/login" req (login (:params req)))

           (GET "/overview" [] (friend/authenticated (render-file "templates/overview.html" {:dev (env :dev?)})))

           ; TODO: authenticate WebSocket endpoints with friend as well?
           (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
           (POST "/chsk" req (ring-ajax-post                req))

           (resources "/")
           (not-found "Not Found"))

(def app
  (let [handler (c-handler/site
                  (friend/authenticate
                    routes
                    {:allow-anon?          true
                     :login-uri            "/login"
                     :default-landing-uri  "/overview"
                     :credential-fn        #(creds/bcrypt-credential-fn (db/auth-map) %)
                     :workflows            [(workflows/interactive-form)]
                     }))]
    (if (env :dev?)
      (wrap-exceptions handler)
      handler)))
