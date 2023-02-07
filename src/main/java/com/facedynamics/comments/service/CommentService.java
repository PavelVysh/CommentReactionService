package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Likable;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReactionsRepository reactionsRepository;

    public CommentDTO save(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return DTOMapper.fromCommentToCommentDTO(savedComment);
    }

    public ResponseEntity<?> findById(int id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment = setLikesDislikes(comment, EntityType.comment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comment with id - " + id + " was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    public ResponseEntity<?> deleteById(int id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            commentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(comment.getText());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with id - " + id +
                    " was not found");
        }
    }

    public ResponseEntity<?> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        if (comments.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comments for post with id "
                    + postId + " were not found");
        }
        comments = setLikesDislikes(comments, EntityType.comment);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    public <T extends Likable> T setLikesDislikes(T t, EntityType entityType) {
        t.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                t.getId(), entityType, false));
        t.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                t.getId(), entityType, true));
        if (t instanceof Comment) {
            ((Comment) t).setReplies(setLikesDislikes(((Comment) t).getReplies(), EntityType.reply));
        }
        return t;
    }

    public <T extends Likable> List<T> setLikesDislikes(List<T> list, EntityType entityType) {
        list.forEach(x -> setLikesDislikes(x, entityType));
        return list;
    }
}
