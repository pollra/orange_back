package com.pollra.web.post.service;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.TargetPost;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PostService {
    PostData createOne();

    void deleteOne();

    void updateOne();

    Object readOne(TargetPost targetPost);
    List<Object> readList(TargetPost targetPost);
}
