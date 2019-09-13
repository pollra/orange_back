package com.pollra.web.post.service;

import com.pollra.web.post.domain.TargetPost;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PostService {
    void createOne(HttpServletRequest request);

    void deleteOne(HttpServletRequest request);

    void updateOne(HttpServletRequest request);

    Object readOne(TargetPost targetPost, HttpServletRequest request);
    List<Object> readList(TargetPost targetPost, HttpServletRequest request);
}
