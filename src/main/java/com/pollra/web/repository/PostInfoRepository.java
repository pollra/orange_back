package com.pollra.web.repository;

import com.pollra.web.post.domain.PostInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {

    int countByOwner(String owner);

}
