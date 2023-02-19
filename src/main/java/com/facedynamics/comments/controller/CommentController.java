package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.comment.CommentSaveDTO;
import com.facedynamics.comments.dto.comment.CommentReturnDTO;
import com.facedynamics.comments.entity.Comment;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<CommentReturnDTO> findById(@PathVariable int id, @RequestParam(required = false)boolean post) {
        return commentService.findById(id, post);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id, @RequestParam EntityType type) {
        return "%d comment(s) have been deleted"
                .formatted(commentService.deleteById(id, type));
    }
}
