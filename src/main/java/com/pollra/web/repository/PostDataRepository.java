package com.pollra.web.repository;

import com.pollra.web.post.domain.PostData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDataRepository extends JpaRepository<PostData, Long> {
    int countByNum(Long num);
}
