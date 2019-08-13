(ns quiz-reagent.partials
  (:require [hiccup.page :as page]
            [ring.util.anti-forgery :as anti-forgery]))

(defn head [title]
  [:head
   [:title title]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (page/include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
                     "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"
                     "css/site.css")])

(defn nav-bar []
  [:nav.navbar.navbar-inverse {:role "navigation"}
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href "/"} "The Thales Project"]]
    [:div.navbar-text.navbar-right
     [:a.smooth-link {:href "/login"} "Log in"]]]])

(defn home-content [title]
  [:div.center.jumbotron
   [:h1.jumbo-h1.center title]
   [:h2.jumbo-h2 "Entertaining quizzes for every occasion"]
   [:a.btn.btn-lg.btn-primary {:href "/signup"} "Sign up now!"]])

(defn- error-when-password-unequal [flash]
  (when (= flash :password-unequal)
    {:class "has-error"}))

(defn signup-content [flash]
  [:div.row
   [:div.col-md-4]
   [:div.col-md-4
    [:h1.center "Sign up"]
    [:form {:method "POST" :action "signup"}

     (anti-forgery/anti-forgery-field)

     (when (= flash :password-unequal) [:div.alert.alert-danger "Passwords don't match!"])

     [:div.form-group
      [:label {:for "email"} "Email"]
      [:input#email.form-control {:type "email" :name "email" :required "required" :placeholder "Email"}]]

     [:div.form-group (error-when-password-unequal flash)
      [:label {:for "password"} "Password"]
      [:input#password.form-control {:type "password" :name "password" :required "required" :placeholder "Password"}]]

     [:div.form-group (error-when-password-unequal flash)
      [:label {:for "confirm"} "Confirm"]
      [:input#confirm.form-control.error {:type "password" :name "confirm" :required "required" :placeholder "Password"}]]

     [:input.btn.btn-md.btn-primary {:type "submit" :name "commit" :value "Create my account"}]]]
   [:div.col-md-4]])

(defn login-content [login-failed]
  [:div.row
   [:div.col-md-4]
   [:div.col-md-4
    [:h1.center "Log in"]
    [:form {:method "POST" :action "login"}

     (anti-forgery/anti-forgery-field)

     (when (= login-failed "Y") [:div.alert.alert-danger "Invalid login data!"])

     [:div.form-group (when (= login-failed "Y") {:class "has-error"})
      [:label {:for "email"} "Email"]
      [:input#email.form-control {:type "email" :name "username" :required "required" :placeholder "Email"}]]

     [:div.form-group (when (= login-failed "Y") {:class "has-error"})
      [:label {:for "password"} "Password"]
      [:input#password.form-control {:type "password" :name "password" :required "required" :placeholder "Password"}]]

     [:input.btn.btn-md.btn-primary {:type "submit" :name "commit" :value "Log in"}]]]
   [:div.col-md-4]])

(defn footer []
  [:footer
   [:small "Quiz framework"]])