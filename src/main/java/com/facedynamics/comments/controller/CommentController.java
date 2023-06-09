package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.DeleteDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comments")
    @PreAuthorize("@authorization.isUser(#comment.userId)")
    public CommentSaveDTO createComment(@RequestBody @Valid Comment comment) {
        return commentService.save(comment);
    }

    @GetMapping("/comments/{id}")
    public CommentReturnDTO findById(@PathVariable int id) {
        return commentService.findById(id);
    }
    
    @GetMapping("/posts/{postId}/comments")
    public Page<CommentReturnDTO> findCommentsByPostId(@PathVariable int postId, Pageable pageable) {
        return commentService.findCommentsByPostId(postId, pageable);
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("@authorization.isOwner(#id)")
    public DeleteDTO deleteById(@PathVariable int id) {
        return commentService.deleteByCommentId(id);
    }
    @DeleteMapping("/posts/{postId}/comments")
    @PreAuthorize("@authorization.isPostOwner(#postId)")
    public DeleteDTO deleteByPostId(@PathVariable int postId) {
        return commentService.deleteByPostId(postId);
    }
}
