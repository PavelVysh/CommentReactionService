package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.ReactionsRepository;
import com.facedynamics.comments.repository.ReplyRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class ReplyService {

    private ReplyRepository replyRepository;
    private ReactionsRepository reactionsRepository;

    public ReplyService(ReplyRepository replyRepository,
                        ReactionsRepository reactionsRepository) {
        this.replyRepository = replyRepository;
        this.reactionsRepository = reactionsRepository;
    }

    public void save(Reply reply) {
        replyRepository.save(reply);
    }
    public Reply findById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    reply.getId(), EntityType.reply, true
            ));
            reply.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    reply.getId(), EntityType.reply, false
            ));
        }
        return reply;
    }
    public void deleteById(int id) {
        replyRepository.deleteById(id);
    }
    public List<Reply> findRepliesByCommentId(int commentId) {
        List<Reply> replies = replyRepository.findRepliesByCommentId(commentId);
        replies.forEach(x -> {
            x.setDislikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    x.getId(), EntityType.reply, false
            ));
            x.setLikes(reactionsRepository.countAllByEntityIdAndEntityTypeAndLike(
                    x.getId(), EntityType.reply, true
            ));
        });
        return replies;
    }
}
