package com.facedynamics.comments.feign;

import com.facedynamics.comments.dto.post.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "feignMock",url = "${posts.url}")
public interface PostsClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}")
    PostDTO getById(@PathVariable int postId);
}
