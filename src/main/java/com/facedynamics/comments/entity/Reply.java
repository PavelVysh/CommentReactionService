package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "comment_id")
    @NotNull(message = "You must provide commentId")
    private Integer commentId;
    @NotNull(message = "You must provide userId")
    private Integer userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Size(min = 2, message = "Text must be at least 2 characters long")
    @Size(max = 500, message = "Text should not be longer then 500 characters")
    @NotNull(message = "You can't save a reply without a text")
    private String text;
    @Transient
    private int likes, dislikes;

}
