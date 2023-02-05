package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "replies")
@Getter
@Setter
public class Reply implements Likable{
    @Id
    private int id;
    @Column(name = "comment_id", insertable = false, updatable = false)
    private int commentId;
    private int userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String text;
    @Transient
    private int likes, dislikes;

}
