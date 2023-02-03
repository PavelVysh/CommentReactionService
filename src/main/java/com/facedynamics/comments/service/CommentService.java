package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Likable;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private ReactionsRepository reactionsRepository;

    public CommentService(CommentRepository commentRepository,
                          ReactionsRepository reactionsRepository) {
        this.commentRepository = commentRepository;
        this.reactionsRepository = reactionsRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    public Comment findById(int id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment = setLikesDislikes(comment, EntityType.comment);
        }
        return comment;
    }
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
    public List<Comment> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        comments = setLikesDislikes(comments, EntityType.comment);
        return comments;
    }

    public <T extends Likable> T setLikesDislikes(T t, EntityType entityType) {
        t.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                        t.getId(), entityType, false));
        t.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                t.getId(), entityType, true));
        if (t instanceof Comment) {
            ((Comment)t).setReplies(setLikesDislikes(((Comment)t).getReplies(), EntityType.reply));
        }
        return t;
    }
    public <T extends Likable> List<T> setLikesDislikes(List<T> list, EntityType entityType) {
        list.forEach(x -> setLikesDislikes(x, entityType));
        return list;
    }
}
