-- name: stats-by-user-id
-- Fetches statistics of a user by his id
SELECT user_id, won, lost
FROM stats
WHERE user_id = :p1

-- name: count-stats
-- The number of statistics entries
SELECT count(*) AS 'count'
FROM stats

-- name: update-stats-stmt!
-- Updates the stats of a user
UPDATE stats
SET won = :p1, lost = :p2
WHERE user_id = :p3

-- name: fetch-users-stmt
-- Fetches all users
SELECT user_id, password
FROM users

-- name: insert-user-stmt!
-- Inserts a user into the database
INSERT INTO users (user_id, password) VALUES
(:p1, :p2)
