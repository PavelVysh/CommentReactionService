package com.facedynamics.comments.controller;

import com.facedynamics.comments.service.DislikeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dislikes")
public class DislikeController {

    private final DislikeService dislikeService;

    public DislikeController(DislikeService dislikeService) {
        this.dislikeService = dislikeService;
    }
}
