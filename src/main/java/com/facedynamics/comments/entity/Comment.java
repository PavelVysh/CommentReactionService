package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment implements Likable{
    @Id
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
