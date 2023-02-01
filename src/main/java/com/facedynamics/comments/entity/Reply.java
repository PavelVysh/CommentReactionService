package com.facedynamics.comments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "replies")
public class Reply {
    @Id
    private int id;
    @Column(name = "comment_id")
    private int commentId;
    private int userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String text;
}
