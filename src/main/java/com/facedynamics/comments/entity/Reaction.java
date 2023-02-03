package com.facedynamics.comments.entity;

import com.facedynamics.comments.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reactions")
public class Reaction {
    @Id
    private int id;
    private int userId;
    private int entityId;
    @Enumerated(value = EnumType.STRING)
    private EntityType entityType;
    @Column(name = "is_like")
    private boolean like;

}
