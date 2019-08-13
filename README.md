# Quiz UI Prototype

Prototype of a quiz app based on [Reagent](https://github.com/reagent-project/reagent), a ClojureScript interface to React.

## How to run (in dev environment)

- run `lein run` and `rlwrap lein figwheel` in two different console tabs

### Server-side REPL

- run `lein repl`
- call `(use 'quiz-reagent.server :reload)` and then `(-main)`
- after making code changes call `(use 'quiz-reagent.handler :reload)` and `(restart-router!)`
- go [here](http://localhost:3000/) to see app running locally **(do not forget to start client-side REPL as well)**

### Client-side REPL

    lein figwheel

The REPL currently doesn't have built-in readline support. To have a
better experience please install **rlwrap**. You can to this on OSX
using brew: `brew install rlwrap`.

When `rlwrap` is installed you can now execute lein figwheel as so:

    rlwrap lein figwheel

This will give you a much nicer REPL experience with history and line
editing.

## How to deploy on heroku

App was created via `heroku create app-name --region eu` and `git push heroku master`. Run `heroku logs --tail` after git push to see the application logs. See the contents of `Procfile` as well!
