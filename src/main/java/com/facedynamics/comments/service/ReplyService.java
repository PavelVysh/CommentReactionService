package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.repository.ReplyRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class ReplyService {

    private ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }
    public void save(Reply reply) {
        replyRepository.save(reply);
    }
    public Reply findById(int id) {
        return replyRepository.findById(id).orElse(null);
    }
    public void deleteById(int id) {
        replyRepository.deleteById(id);
    }
    public List<Reply> findRepliesByCommentId(int commentId) {
        return replyRepository.findRepliesByCommentId(commentId);
    }
}
