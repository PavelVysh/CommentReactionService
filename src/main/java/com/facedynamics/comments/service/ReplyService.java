package com.facedynamics.comments.service;

import com.facedynamics.comments.repository.ReplyRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class ReplyService {

    private ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }
}
