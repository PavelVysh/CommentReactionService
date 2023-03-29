package com.facedynamics.comments.security;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.CommentRepository;
import com.facedynamics.comments.repository.ReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Authorization {

    private final CommentRepository commentRepository;

    public boolean isOwner(int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Entity with id - " + commentId + " doesn't exist"));
        Long userIdFromComment = Long.valueOf(comment.getUserId());
        System.out.println("Current user " + getCurrentUserId());
        return Objects.equals(getCurrentUserId(), userIdFromComment);
    }

    public boolean belongsToUser(int commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        int userId = -1;
        if (comment.isPresent()) {
            userId = comment.get().getUserId();
        }
        return Objects.equals(getCurrentUserId(), userId);
    }

    private static Long getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (Long) jwt.getClaims().get("identity_id");
    }
}