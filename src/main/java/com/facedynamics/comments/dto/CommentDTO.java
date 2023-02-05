package com.facedynamics.comments.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDTO {
    private int id;
    private int userId;
    private int postId;
    private String text;
}
