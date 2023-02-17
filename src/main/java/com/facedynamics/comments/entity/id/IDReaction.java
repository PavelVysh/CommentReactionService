package com.facedynamics.comments.entity.id;

import com.facedynamics.comments.entity.enums.EntityType;

import java.io.Serializable;

public class IDReaction implements Serializable {
    private EntityType entityType;
    private int userId;
    private int entityId;
}
