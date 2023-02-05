package com.facedynamics.comments.dto;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.Reply;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public static CommentDTO fromCommentToCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .text(comment.getText())
                .postId(comment.getPostId())
                .build();
    }
    public static ReplyDTO fromReplyToReplyDTO(Reply reply) {
        return ReplyDTO.builder()
                .comment_id(reply.getCommentId())
                .user_id(reply.getUserId())
                .id(reply.getId())
                .text(reply.getText())
                .build();
    }
}
