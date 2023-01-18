CREATE TABLE IF NOT EXISTS comments(
id INTEGER PRIMARY KEY,
post_id INTEGER NOT NULL,
user_id INTEGER NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
text VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS replies(
id INTEGER PRIMARY KEY,
comment_id INTEGER NOT NULL,
user_id INTEGER NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
text VARCHAR(500) NOT NULL,
FOREIGN KEY (comment_id) REFERENCES comments(id)
);

CREATE TABLE IF NOT EXISTS likes(
id INTEGER PRIMARY KEY,
user_id INTEGER,
entity_id INTEGER,
entity_type ENUM("comment", "reply", "post", "repost")
);

CREATE TABLE IF NOT EXISTS dislikes(
id INTEGER PRIMARY KEY,
user_id INTEGER,
entity_id INTEGER,
entity_type ENUM("comment", "reply", "post", "repost")
);
