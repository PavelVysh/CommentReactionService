package com.facedynamics.comments.controller;

import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.service.ReplyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping
    public void createReply(@RequestBody Reply reply) {
        replyService.save(reply);
    }
    @GetMapping("/{id}")
    public Reply findById(@PathVariable int id) {
        return replyService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        replyService.deleteById(id);
    }
    @GetMapping("/comments/{commentId}")
    public List<Reply> findRepliesByCommentId(@PathVariable int commentId) {
        return replyService.findRepliesByCommentId(commentId);
    }
}
