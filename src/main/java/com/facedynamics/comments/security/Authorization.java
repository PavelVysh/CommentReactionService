package com.facedynamics.comments.security;

import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.feign.PostsClient;
import com.facedynamics.comments.repository.CommentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Authorization {

    private final CommentRepository commentRepository;
    private final PostsClient postsClient;

    public boolean isOwner(int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Entity with id - " + commentId + " doesn't exist"));
        Long userIdFromComment = Long.valueOf(comment.getUserId());
        return Objects.equals(getCurrentUserId(), userIdFromComment);
    }
    public boolean isUser(Long userId) {
        return Objects.equals(userId, getCurrentUserId());
    }

    public boolean isPostOwner(int postId) {
        long postOwnerId;
        try {
            postOwnerId = postsClient.getPostById(postId).getUserId();
        } catch (FeignException exc) {
            throw new NotFoundException("Post with id - " + postId + " was not found");
        }
        return Objects.equals(postOwnerId, getCurrentUserId());
    }

    private static Long getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (Long) jwt.getClaims().get("identity_id");
    }
}