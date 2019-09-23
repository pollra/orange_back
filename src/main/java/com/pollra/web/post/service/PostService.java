package com.pollra.web.post.service;

import com.pollra.web.post.domain.PL_Range;
import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.PostList;
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
    List<PostList> readList(PL_Range range, String category);
}
