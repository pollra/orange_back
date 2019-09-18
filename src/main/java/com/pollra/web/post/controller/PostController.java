package com.pollra.web.post.controller;

import com.pollra.response.ApiDataDetail;
import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.service.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class PostController {
    private PostServiceImpl postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping("posts/target/{numberPath}")
    public RequestEntity<?> getOnePost(@PathVariable String numberPath){
        return null;
    }

    @PostMapping("posts")
    public ResponseEntity<?> createOnePost(){
        PostData resultPostData = null;
        try {
            log.info("/posts logic start");
            resultPostData = postService.createOne();

            return new ResponseEntity<Long>(resultPostData.getNum(),HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
