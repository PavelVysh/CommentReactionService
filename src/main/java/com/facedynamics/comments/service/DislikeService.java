package com.facedynamics.comments.service;

import com.facedynamics.comments.repository.DislikeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class DislikeService {

    private DislikeRepository dislikeRepository;

    public DislikeService(DislikeRepository dislikeRepository) {
        this.dislikeRepository = dislikeRepository;
    }
}
