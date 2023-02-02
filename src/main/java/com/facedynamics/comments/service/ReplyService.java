package com.facedynamics.comments.service;

import com.facedynamics.comments.entity.Reply;
import com.facedynamics.comments.entity.enums.EntityType;
import com.facedynamics.comments.repository.DislikeRepository;
import com.facedynamics.comments.repository.LikeRepository;
import com.facedynamics.comments.repository.ReplyRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class ReplyService {

    private ReplyRepository replyRepository;
    private LikeRepository likeRepository;
    private DislikeRepository dislikeRepository;

    public ReplyService(ReplyRepository replyRepository,
                        LikeRepository likeRepository,
                        DislikeRepository dislikeRepository) {
        this.replyRepository = replyRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
    }

    public void save(Reply reply) {
        replyRepository.save(reply);
    }
    public Reply findById(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.setLikes(likeRepository.countLikesByEntityIdAndEntityType(
                    reply.getId(), EntityType.reply
            ));
            reply.setDislikes(dislikeRepository.countDislikesByEntityIdAndEntityType(
                    reply.getId(), EntityType.reply
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
            x.setDislikes(dislikeRepository.countDislikesByEntityIdAndEntityType(
                    x.getId(), EntityType.reply
            ));
            x.setLikes(likeRepository.countLikesByEntityIdAndEntityType(
                    x.getId(), EntityType.reply
            ));
        });
        return replies;
    }
}
