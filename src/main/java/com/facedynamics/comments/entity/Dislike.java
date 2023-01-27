package com.facedynamics.comments.entity;

import com.facedynamics.comments.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dislikes")
public class Dislike {
    @Id
    private int id;
    private int user_id;
    private int entity_id;
    @Enumerated(value = EnumType.STRING)
    private EntityType entityType;
}
