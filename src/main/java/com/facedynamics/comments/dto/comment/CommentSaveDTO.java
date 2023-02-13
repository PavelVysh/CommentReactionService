package com.facedynamics.comments.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSaveDTO {
    private int id;
    private int userId;
    private int postId;
    private String text;
}
