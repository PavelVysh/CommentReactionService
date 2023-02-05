package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.repository.ReplyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReactionsRepository reactionsRepository;

    public ReplyService(ReplyRepository replyRepository,
                        ReactionsRepository reactionsRepository) {
        this.replyRepository = replyRepository;
        this.reactionsRepository = reactionsRepository;
    }

    public ReplyDTO save(Reply reply) {
        Reply savedReply = replyRepository.save(reply);
        return DTOMapper.fromReplyToReplyDTO(savedReply);
    }
    public ResponseEntity<?> findById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply = setLikesDislikes(reply);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reply with id - " + id +
                    " was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reply);
    }
    public ResponseEntity<?> deleteById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            replyRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(reply.getText());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reply with id - " + id +
                    " was not found");
        }
    }
    public ResponseEntity<?> findRepliesByCommentId(int commentId) {
        List<Reply> replies = replyRepository.findRepliesByCommentId(commentId);
        if (replies.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Replies for comment with id - " +
                    commentId + " were not found");
        }
        replies.forEach(this::setLikesDislikes);
        return ResponseEntity.status(HttpStatus.OK).body(replies);
    }
    public Reply setLikesDislikes(Reply reply) {
        reply.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                reply.getId(), EntityType.reply, false));
        reply.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                reply.getId(), EntityType.reply, true));
        return reply;
    }
}
