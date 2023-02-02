package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reply;
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
            comment = setLikesDislikes(comment);
        }
        return comment;
    }
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
    public List<Comment> findCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        comments = setLikesDislikes(comments);
        return comments;
    }

    public Comment setLikesDislikes(Comment comment) {
        comment.setDislikes(
                reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                        comment.getId(),
                        EntityType.comment,
                        false
                ));
        comment.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                comment.getId(),
                EntityType.comment,
                true
        ));
        comment.setReplies(setLikeDislikes(comment.getReplies()));
        return comment;
    }
    public List<Comment> setLikesDislikes(List<Comment> commentList) {
        commentList.forEach(x -> {
                x.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                        x.getId(),EntityType.comment, false
                ));
                x.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                        x.getId(),EntityType.comment, true
                ));
                x.setReplies(setLikeDislikes(x.getReplies()));
            });
        return commentList;
    }
    public List<Reply> setLikeDislikes(List<Reply> replies) {
        replies.forEach(x -> {
            x.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    x.getId(),EntityType.reply, false
            ));
            x.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    x.getId(),EntityType.reply, true
            ));
        });
        return replies;
    }
}
