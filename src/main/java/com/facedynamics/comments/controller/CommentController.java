package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comments")
    public CommentSaveDTO createComment(@RequestBody @Valid Comment comment) {
        return commentService.save(comment);
    }

    @GetMapping("/comments/{id}")
    public CommentReturnDTO findById(@PathVariable int id) {
        return commentService.findById(id);
    }
    @GetMapping("/posts/{id}/comments")
    public Page<CommentReturnDTO> findByPostId(@PathVariable int id, Pageable pageable) {
        return commentService.findCommentsByPostId(id, pageable);
    }

    @DeleteMapping("/comments/{id}")
    public String deleteById(@PathVariable int id, @RequestParam EntityType type) {
        return "%d comment(s) have been deleted"
                .formatted(commentService.deleteById(id, type));
    }
}
