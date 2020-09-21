package com.pollra.web.post.service;

import com.pollra.web.post.entity.Post;
import com.pollra.web.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService{

    private final PostRepository postRepository;

    @Transactional(readOnly=true)
    public Page<Post> getPage(Pageable page) {
        return postRepository.findAll(page);
    }

    @Transactional(readOnly=true)
    public Post get(Long id) {
        return postRepository.findById(id).get();
    }

    public Post add(Post post){
        post.setTitle(escapeString(post.getTitle()));
        post.setContent(escapeString(post.getContent()));
        return postRepository.save(post);
    }

    public Long modify(Post post){
        Post findData = postRepository.findById(post.getId()).get();
        findData.set(post);
        return findData.getId();
    }

    public void remove(Post post){
        postRepository.delete(post);
    }

    /**
     * other method
     */

    // 이스케이프 처리
    private String escapeString(String text){
        Map<String, String> escape = new HashMap<>();
        escape.put("&","&amp;");
        escape.put("<","&lt;");
        escape.put(">","&gt;");
        escape.put("\"","&quot;");
        escape.put("\'","&#39;");
        escape.put("/","&#x2F;");
        escape.put("`","&#x60;");
        escape.put("=","&#x3D;");

        for(Map.Entry<String, String> item : escape.entrySet()){
            text.replaceAll(item.getKey(), item.getValue());
        }
        return text;
    }

}
