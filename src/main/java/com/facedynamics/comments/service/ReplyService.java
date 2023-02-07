package com.facedynamics.comments.service;

import com.facedynamics.comments.dto.DTOMapper;
import com.facedynamics.comments.dto.ReplyDTO;
import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.exeption.NotFoundException;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.repository.ReplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReactionsRepository reactionsRepository;

    public ReplyDTO save(Reply reply) {
        Reply savedReply = replyRepository.save(reply);
        return DTOMapper.fromReplyToReplyDTO(savedReply);
    }

    public ResponseEntity<Reply> findById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply = setLikesDislikes(reply);
        } else {
            throw new NotFoundException("Reply with id - " + id +
                    " was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reply);
    }

    public ResponseEntity<String> deleteById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            replyRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(reply.getText());
        } else {
            throw new NotFoundException("Reply with id - " + id + " was not found");
        }
    }

    public ResponseEntity<List<Reply>> findRepliesByCommentId(int commentId) {
        List<Reply> replies = replyRepository.findRepliesByCommentId(commentId);
        if (replies.size() < 1) {
            throw new NotFoundException("Replies for comment with id - " +
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
