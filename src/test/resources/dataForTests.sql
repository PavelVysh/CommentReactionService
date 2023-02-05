DROP TABLE IF EXISTS reactions;
DROP TABLE IF EXISTS replies;
DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    post_id    INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    text       VARCHAR(500) NOT NULL
);

CREATE TABLE replies
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    comment_id INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    text       VARCHAR(500) NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comments (id)
);

CREATE TABLE reactions
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id     INTEGER,
    entity_id   INTEGER,
    entity_type VARCHAR(255),
    is_like     BOOLEAN
);
ALTER TABLE reactions
    ADD CONSTRAINT uniq_only UNIQUE (user_id, entity_id, entity_type);
INSERT INTO comments(post_id, user_id, text)
VALUES (1, 1, 'I am the first comment made by a first user'),
       (1, 2, 'I am a comment made by a user number 2 for post number one'),
       (2, 5, 'I am a comment for second post made by fifth user'),
       (5, 11, 'I am a comment for fifth post made by 11-th user'),
       (4, 2, 'I am a comment for fourth post made by second user'),
       (4, 5, 'I am a filler comment');

INSERT INTO replies(comment_id, user_id, text)
VALUES (2, 1, 'I am a first reply for second comment'),
       (2, 2, 'I am a second reply made for comment number three by user 2'),
       (5, 11, 'A reply for fifth comment by 11-th user'),
       (2, 11, 'A reply for second comment by 11-th user'),
       (4, 11, 'A reply for second comment by 11-th user');
INSERT INTO reactions(user_id, entity_id, entity_type, is_like)
VALUES (12, 1, 'comment', false),
       (13, 1, 'comment', false),
       (1, 2, 'comment', false),
       (11, 2, 'comment', false),
       (10, 3, 'comment', false),
       (65, 2, 'comment', false),
       (121, 5, 'comment', false),
       (131, 4, 'comment', false),
       (1, 3, 'comment', false);
INSERT INTO reactions(user_id, entity_id, entity_type, is_like)
VALUES (1, 1, 'reply', true),
       (2, 2, 'reply', true),
       (3, 1, 'reply', true),
       (1, 2, 'reply', true),
       (2, 3, 'reply', true),
       (3, 4, 'reply', true),
       (11, 1, 'reply', true),
       (22, 2, 'reply', true),
       (33, 4, 'reply', true),
       (1111, 1, 'comment', true),
       (2222, 1, 'comment', true),
       (3333, 1, 'comment', true),
       (111, 3, 'reply', true),
       (222, 2, 'reply', true),
       (333, 2, 'reply', true);

