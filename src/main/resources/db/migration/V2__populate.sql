INSERT INTO comments(post_id, user_id, text)
VALUES (1, 1, 'I am the first comment made by a first user'),
       (1, 2, 'I am a comment made by a user number 2 for post number one'),
       (2, 5, 'I am a comment for second post made by fifth user'),
       (5, 11, 'I am a comment for fifth post made by 11-th user'),
       (4, 2, 'I am a comment for fourth post made by second user');

INSERT INTO replies(comment_id, user_id, text)
VALUES (2, 1, 'I am a first reply for second comment'),
       (3, 2, 'I am a second reply made for comment number three by user 2'),
       (5, 11, 'A reply for fifth comment by 11-th user'),
       (2, 11, 'A reply for second comment by 11-th user');
INSERT INTO dislikes(user_id, entity_id, entity_type)
VALUES (12, 1, 'comment'), (13, 1, 'comment'), (1, 2, 'comment'),
       (11, 2, 'comment'), (10, 3, 'comment'), (65, 2, 'comment'),
       (121, 5, 'comment'), (131, 4, 'comment'), (1, 3, 'comment');
INSERT INTO likes(user_id, entity_id, entity_type)
VALUES (1, 1, 'reply'), (2, 2, 'reply'), (3, 1, 'reply'),
       (1, 2, 'reply'), (2, 3, 'reply'), (3, 4, 'reply'),
       (11, 1, 'reply'), (22, 2, 'reply'), (33, 4, 'reply'),
       (111, 3, 'reply'), (222, 2, 'reply'), (333, 2, 'reply');

