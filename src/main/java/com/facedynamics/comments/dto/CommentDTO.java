package com.facedynamics.comments.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDTO {
    private int id;
    private int userId;
    private int postId;
    private String text;
}
