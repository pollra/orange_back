package com.pollra.web.post.controller;

import com.pollra.aop.jwt.anno.TokenCertification;
import com.pollra.aop.jwt.anno.TokenCredential;
import com.pollra.response.ApiDataDetail;
import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.service.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/posts")
@Slf4j
@CrossOrigin
public class PostController {
    private PostServiceImpl postService;
    private HttpServletRequest request;

    public PostController(PostServiceImpl postService, HttpServletRequest request) {
        this.postService = postService;
        this.request = request;
    }

    @GetMapping("target/{numberPath}")
    public RequestEntity<?> getOnePost(@PathVariable String numberPath){
        return null;
    }

    @TokenCertification
    @PostMapping // /api/posts
    public ResponseEntity<?> createOnePost(){
        log.info("post 입력 확인");
        if(!StringUtils.isEmpty(request.getAttribute("error"))){
            log.warn("토큰 정보를 확인할 수 없습니다: {}",request.getAttribute("error").toString());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("토큰 정보를 확인할 수 없습니다."),HttpStatus.FORBIDDEN);
        }
        if(StringUtils.isEmpty(request.getAttribute("jwt-user"))&& StringUtils.isEmpty(request.getAttribute("jwt-auth"))){
            log.warn("로그인된 유저의 정보를 읽는데 실패했습니다: jwt-user[{}], jwt-auth[{}]", request.getAttribute("jwt-user"), request.getAttribute("jwt-auth"));
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("토큰을 읽어들이는데 실패했습니다."),HttpStatus.FORBIDDEN);
        }
        log.warn("로그인된 유저 정보: jwt-user[{}], jwt-auth[{}]", request.getAttribute("jwt-user"), request.getAttribute("jwt-auth"));
        PostData resultPostData = null;
        try {
            log.info("one post insert logic start");
            resultPostData = postService.createOne();
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok",resultPostData.getNum()),HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
