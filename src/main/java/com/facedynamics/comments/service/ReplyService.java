package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.ReplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyDTO save(Reply reply) {
        Reply savedReply = replyRepository.save(reply);
        return DTOMapper.fromReplyToReplyDTO(savedReply);
    }
    public Reply findById(int id) {
        return replyRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Reply with id - " + id + " was not found");
        });
    }

    public String deleteById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            replyRepository.deleteById(id);
            return reply.getText();
        } else {
            throw new NotFoundException("Reply with id - " + id + " was not found");
        }
    }

    public List<Reply> findRepliesByCommentId(int commentId) {
        List<Reply> replies = replyRepository.findRepliesByCommentId(commentId);
        if (replies.size() < 1) {
            throw new NotFoundException("Replies for comment with id - " +
                    commentId + " were not found");
        }
        return replies;
    }
}
