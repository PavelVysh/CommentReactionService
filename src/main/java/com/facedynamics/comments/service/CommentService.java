package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.DislikeRepository;
import com.facedynamics.comments.repository.LikeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private DislikeRepository dislikeRepository;
    private LikeRepository likeRepository;

    public CommentService(CommentRepository commentRepository,
                          DislikeRepository dislikeRepository,
                          LikeRepository likeRepository) {
        this.commentRepository = commentRepository;
        this.dislikeRepository = dislikeRepository;
        this.likeRepository = likeRepository;
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
                dislikeRepository.countDislikesByEntityIdAndEntityType(
                        comment.getId(),
                        EntityType.comment
                ));
        comment.setLikes(likeRepository.countLikesByEntityIdAndEntityType(
                comment.getId(),
                EntityType.comment
        ));
        comment.setReplies(setLikeDislikes(comment.getReplies()));
        return comment;
    }
    public List<Comment> setLikesDislikes(List<Comment> commentList) {
        commentList.forEach(x -> {
                x.setDislikes(dislikeRepository.countDislikesByEntityIdAndEntityType(
                        x.getId(),EntityType.comment
                ));
                x.setLikes(likeRepository.countLikesByEntityIdAndEntityType(
                        x.getId(),EntityType.comment
                ));
                x.setReplies(setLikeDislikes(x.getReplies()));
            });
        return commentList;
    }
    public List<Reply> setLikeDislikes(List<Reply> replies) {
        replies.forEach(x -> {
            x.setDislikes(dislikeRepository.countDislikesByEntityIdAndEntityType(
                    x.getId(),EntityType.reply
            ));
            x.setLikes(likeRepository.countLikesByEntityIdAndEntityType(
                    x.getId(),EntityType.reply
            ));
        });
        return replies;
    }
}
