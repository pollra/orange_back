package com.pollra.web.blog.controller;

import com.pollra.web.blog.domain.BlogInfo;
import com.pollra.web.blog.service.BlogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api")
public class BlogController {
    private BlogInfoService blogService;

    public BlogController(BlogInfoService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("public/blog")
    public ResponseEntity<?> getBlogInfo(HttpServletRequest request){
        BlogInfo blogInfo = new BlogInfo();
//        InfoPrint.headersPrint(request);
        blogInfo = blogService.getBlogInfoOfId("pollra");
        return new ResponseEntity<>(blogInfo, HttpStatus.OK);
    }
}
