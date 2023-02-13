package com.facedynamics.comments.dto.reaction;

import com.facedynamics.comments.entity.enums.EntityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionSaveDTO {
    private int id;
    private int userId;
    private int entityId;
    private EntityType entityType;
    private Boolean like;
    public Boolean isLike() {
        return like;
    }
}
