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
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public CommentSaveDTO createComment(@RequestBody @Valid Comment comment) {
        return commentService.save(comment);
    }

    @GetMapping("/{id}")
    public Page<CommentReturnDTO> findById(@PathVariable int id,
                                           @RequestParam(required = false) boolean post,
                                           Pageable pageable) {

        return commentService.findById(id, post, pageable);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id, @RequestParam EntityType type) {
        return "%d comment(s) have been deleted"
                .formatted(commentService.deleteById(id, type));
    }
}
