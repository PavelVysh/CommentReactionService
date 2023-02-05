package com.facedynamics.comments.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyDTO {
    private int id;
    private int user_id;
    private int comment_id;
    private String text;
}
