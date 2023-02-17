CREATE TABLE IF NOT EXISTS comments
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    parent_id  INTEGER,
    post_id    INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    text       VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS reactions
(
    user_id     INTEGER,
    entity_id   INTEGER,
    entity_type VARCHAR(255),
    update_time TIMESTAMP,
    is_like     BOOLEAN,
    PRIMARY KEY (user_id, entity_id, entity_type)
);

ALTER TABLE comments ADD FOREIGN KEY cascade_delete (parent_id)
REFERENCES comments(id)
ON DELETE CASCADE ;

CREATE TRIGGER cascade_delete_reactions Before DELETE ON comments
    for each row
    delete from reactions
    where entity_type='comment' AND entity_id=OLD.id;
