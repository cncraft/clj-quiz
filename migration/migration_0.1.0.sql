CREATE TABLE IF NOT EXISTS stats (
  user_id VARCHAR (100) NOT NULL,
  won INT NULL,
  lost INT NULL,
  PRIMARY KEY (user_id)
);