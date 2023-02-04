package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
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
    @Size(min = 2, message = "Text must be at least 2 characters long")
    @Size(max = 500, message = "Text should not be longer then 500 characters")
    @NotNull(message = "You can't save a comment without a text")
    private String text;
    @Transient
    private int likes, dislikes;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private List<Reply> replies;

}
