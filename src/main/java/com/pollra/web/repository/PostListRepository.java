package com.pollra.web.repository;

import com.pollra.web.post.domain.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostListRepository extends JpaRepository<PostList, Long> {

    List<PostList> findAllByCategory(String category);
    List<PostList> findAllByOwner(String owner);

    @Modifying
    @Query(value = "UPDATE post_list SET title = :title, category = :category WHERE id = :num", nativeQuery = true)
    void updateTitleAndCategoryByNum(@Param("title") String title, @Param("category") String category, @Param("num") Long num);
}
