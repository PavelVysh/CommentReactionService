package com.facedynamics.comments.dto.reaction;

import com.facedynamics.comments.entity.enums.EntityType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ReactionReturnDTO {
    private int userId;
    private int entityId;
    private EntityType entityType;
    private LocalDateTime updateTime;
    private Boolean like;
    private int pageSize;
    private int currentPage;
    private int totalPages;
    public Boolean isLike() {
        return like;
    }
}
