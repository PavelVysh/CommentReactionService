package com.facedynamics.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "replies")
public class Reply {
    @Id
    private int id;
    private int commentId;
    private int userId;
    private LocalDateTime createdAt;
    private String text;
}
