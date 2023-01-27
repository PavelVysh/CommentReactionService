package com.facedynamics.comments.service;

import com.facedynamics.comments.repository.CommentRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
