package com.pollra.web.repository;

import com.pollra.web.post.domain.PostInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {

    int countByOwner(String owner);
    PostInfo getByNum(Long num);
    List<PostInfo> getAllByUri(String uri);

    @Modifying
    @Query(value = "UPDATE post_info SET category = :category WHERE num = :num", nativeQuery = true)
    void updateCategoryByNum(@Param("category") String category, @Param("num") Long num);
}
