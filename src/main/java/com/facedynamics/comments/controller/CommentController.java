package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.CommentDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping
    public CommentDTO createComment(@RequestBody @Valid Comment comment) {
        return commentService.save(comment);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Comment> findById(@PathVariable int id) {
        return commentService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        commentService.deleteById(id);
    }
    @GetMapping("/posts/{postId}")
    public List<Comment> findCommentsByPostId(@PathVariable int postId) {
        return commentService.findCommentsByPostId(postId);
    }
}
