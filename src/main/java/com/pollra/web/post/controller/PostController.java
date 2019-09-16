package com.pollra.web.post.controller;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.service.PostService;
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

    @GetMapping("public/posts/{numberPath}")
    public RequestEntity<?> getOnePost(@PathVariable String numberPath){
        return null;
    }

    @PostMapping("protected/posts/create")
    public ResponseEntity<?> createOnePost(){
        PostData resultPostData = null;
        try {
            log.info("/protected/posts/create logic start");
            resultPostData = postService.createOne();

            return new ResponseEntity<Long>(resultPostData.getNum(),HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<Error>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
