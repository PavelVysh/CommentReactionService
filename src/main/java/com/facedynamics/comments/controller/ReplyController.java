package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.service.ReplyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping
    public ReplyDTO createReply(@RequestBody @Valid Reply reply) {
        return replyService.save(reply);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return replyService.findById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
         return replyService.deleteById(id);
    }
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> findRepliesByCommentId(@PathVariable int commentId) {
        return replyService.findRepliesByCommentId(commentId);
    }
}
