package com.pollra.web.repository;

import com.pollra.web.post.domain.PostData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDataRepository extends JpaRepository<PostData, Long> {
    int countByNum(Long num);
    PostData getByNum(Long num);

    /*update*/
    @Modifying
    @Query(value = "UPDATE post_data SET title = :title, post_content = :content WHERE num = :num",nativeQuery = true)
    void updatePostContentAndTitleByNum(@Param("title")String title, @Param("content")String content, @Param("num")Long num);
}
