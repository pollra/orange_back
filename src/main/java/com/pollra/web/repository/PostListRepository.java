package com.pollra.web.repository;

import com.pollra.web.post.domain.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostListRepository extends JpaRepository<PostList, Long> {

}
