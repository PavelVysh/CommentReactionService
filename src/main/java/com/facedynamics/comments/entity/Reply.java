package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "replies")
public class Reply implements Likable{
    @Id
    private int id;
    @Column(name = "comment_id")
    private int commentId;
    private int userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String text;
    @Transient
    private int likes, dislikes;

}
