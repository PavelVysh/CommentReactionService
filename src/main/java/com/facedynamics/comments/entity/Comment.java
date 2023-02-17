package com.facedynamics.comments.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "You have to provide postId")
    private Integer postId;
    @NotNull(message = "You have to provide userId")
    private Integer userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Size(min = 2, message = "Text must be at least 2 characters long")
    @Size(max = 500, message = "Text should not be longer then 500 characters")
    @NotNull(message = "You can't save a comment without a text")
    private String text;

    @Formula(value = "(SELECT count(*) from reactions r where r.is_like=true" +
            " AND r.entity_type='comment' AND r.entity_id=id)")
    private int likes;

    @Formula(value = "(SELECT count(*) from reactions r where r.is_like=false" +
            " AND r.entity_type='comment' AND r.entity_id=id)")
    private int dislikes;
    @Positive(message = "parentId should be a positive integer number")
    @Column(name = "parent_id")
    private Integer parentId;
    @OneToMany
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<Comment> comments;
}
