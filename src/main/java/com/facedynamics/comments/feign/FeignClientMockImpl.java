package com.facedynamics.comments.feign;

import com.facedynamics.comments.dto.post.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "feignMock",url = "https://690db50a-ba0d-493c-98d5-aa27ab6f16c0.mock.pstmn.io")
public interface FeignClientMockImpl {

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}")
    PostDTO getPostById(@PathVariable int postId);
}
