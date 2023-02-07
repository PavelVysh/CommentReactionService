package com.facedynamics.comments.controller;

import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.service.ReplyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@AllArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ReplyDTO createReply(@RequestBody @Valid Reply reply) {
        return replyService.save(reply);
    }

    @GetMapping("/{id}")
    public Reply findById(@PathVariable int id) {
        return replyService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id) {
        return replyService.deleteById(id);
    }

    @GetMapping("/comments/{commentId}")
    public List<Reply> findRepliesByCommentId(@PathVariable int commentId) {
        return replyService.findRepliesByCommentId(commentId);
    }
}
