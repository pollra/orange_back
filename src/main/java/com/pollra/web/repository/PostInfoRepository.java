package com.pollra.web.repository;

import com.pollra.web.post.domain.PostInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {

    int countByOwner(String owner);
    PostInfo getByNum(Long num);
    List<PostInfo> getAllByUri(String uri);

}
