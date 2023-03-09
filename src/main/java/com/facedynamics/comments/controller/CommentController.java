package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comments")
    public CommentSaveDTO createComment(@RequestBody @Valid Comment comment) {
        return commentService.save(comment);
    }

    @GetMapping("comments/{id}")
    public CommentReturnDTO findById(@PathVariable int id) {
        return commentService.findById(id);
    }

    @DeleteMapping("comments/{id}")
    public Map<String, Integer> deleteById(@PathVariable int id) {
        return commentService.deleteByCommentId(id);
    }
    @DeleteMapping("/posts/{postId}/comments")
    public Map<String, Integer> deleteByPostId(@PathVariable int postId) {
        return commentService.deleteByPostId(postId);
    }

    @GetMapping("comments/posts/{postId}")
    public List<CommentReturnDTO> findCommentsByPostId(@PathVariable int postId) {
        return commentService.findCommentsByPostId(postId);
    }
}
