package com.facedynamics.comments.service;

import com.facedynamics.comments.repository.LikeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class LikeService {

    private LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
}
