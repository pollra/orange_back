package com.pollra.web.post.controller;

import com.pollra.web.post.form.PostForm.*;
import com.pollra.web.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.pollra.web.post.mapper.PostMapper.mapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("${property.api.end-point}")
public class PostController {

    private final PostService postService;

    @GetMapping("/blog/posts/page")
    public Page<Response.FindAll> getPage(@Valid Pageable page){
        return postService.getPage(page).map(mapper::toFindAll);
    }

    @GetMapping("/blog/posts/{postId}")
    public Response.FindOne get(@PathVariable("postId") Long postId) {
        return mapper.toFindOne(postService.get(postId));
    }

    @PostMapping("/blog/posts")
    public Long add(@Valid @RequestBody Request.Add add) {
        return postService.add(mapper.toPost(add)).getId();
    }

    @PutMapping("/blog/posts/{postId}")
    public Long modify(@PathVariable("postId") Long postId, @Valid @RequestBody Request.Modify modify){
        return postService.modify(mapper.toPost(postId, modify));
    }

    @DeleteMapping("/blog/posts/{postId}")
    public void remove(@PathVariable("postId") Long postId){
        postService.remove(mapper.toPost(postId));
    }
}
