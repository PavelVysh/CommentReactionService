package com.facedynamics.comments.dto.comment;

import com.facedynamics.comments.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentReturnDTO {
    private int id;
    private int userId;
    private int postId;
    private LocalDateTime createdAt;
    private int likes, dislikes;
    private String text;
    private List<Comment> replies;
}
