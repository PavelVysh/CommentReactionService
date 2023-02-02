package com.facedynamics.comments.controller;

import com.facedynamics.comments.service.ReactionsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reactions")
public class ReactionsController {

    private final ReactionsService reactionsService;

    public ReactionsController(ReactionsService reactionsService) {
        this.reactionsService = reactionsService;
    }
}
