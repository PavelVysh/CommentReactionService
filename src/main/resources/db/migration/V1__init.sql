CREATE TABLE IF NOT EXISTS comments
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    parent_id  INTEGER,
    post_id    INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    text       VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS replies
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    comment_id INTEGER,
    user_id    INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    text       VARCHAR(500) NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comments (id)
);

CREATE TABLE IF NOT EXISTS reactions
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id     INTEGER,
    entity_id   INTEGER,
    entity_type VARCHAR(255),
    update_time TIMESTAMP,
    is_like     BOOLEAN
);
ALTER TABLE reactions
    ADD CONSTRAINT uniq_only UNIQUE (user_id, entity_id, entity_type);
