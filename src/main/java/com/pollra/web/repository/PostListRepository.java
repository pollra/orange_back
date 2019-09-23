package com.pollra.web.repository;

import com.pollra.web.post.domain.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostListRepository extends JpaRepository<PostList, Long> {

    List<PostList> findAllByCategory(String category);
    List<PostList> findAllByOwner(String owner);
}
