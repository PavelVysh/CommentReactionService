package com.facedynamics.comments.dto;

import com.facedynamics.comments.entity.Comment;
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
}
