package com.facedynamics.comments.entity;

import com.facedynamics.comments.entity.enums.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "You must provide userId")
    private Integer userId;
    @NotNull(message = "You must provide entityId")
    private Integer entityId;
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "You must provide entityType")
    private EntityType entityType;
    @Column(name = "is_like")
    @NotNull(message = "You must specify is this a like (like=true/false)")
    private Boolean like;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    public Boolean isLike() {
        return like;
    }

}
