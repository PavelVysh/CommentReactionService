package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Likable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int postId;
    private int userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String text;
    @Transient
    private int likes, dislikes;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private List<Reply> replies;
}
