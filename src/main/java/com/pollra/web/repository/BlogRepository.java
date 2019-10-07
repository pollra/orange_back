package com.pollra.web.repository;

import com.pollra.web.blog.domain.BlogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogInfo, String> {
    // Create
    // save method

    /**
     * Update
     */

    // update one

    @Modifying  // 타이틀 변경
    @Query( value = "UPDATE blog_info SET title = :title WHERE id = :id", nativeQuery = true)
    void updateOneByTitleById(@Param("title") String title, @Param("id") String id);

    @Modifying  // 설명 변경
    @Query( value = "UPDATE blog_info SET explanation = :explanation WHERE id = :id", nativeQuery = true)
    void updateOneByExplanationById(@Param("explanation") String explanation, @Param("id") String id);

    @Modifying  // 이미지 변경
    @Query( value = "UPDATE blog_info SET img_path = :imgPath WHERE id = :id", nativeQuery = true)
    void updateOneByImgPathById(@Param("imgPath") String imgPath, @Param("id") String id);

    @Modifying  // 메타 타이틀 변경
    @Query( value = "UPDATE blog_info SET meta_title = :metaTitle WHERE id = :id", nativeQuery = true)
    void updateOneByMetaTitleById(@Param("metaTitle") String metaTitle, @Param("id") String id);

    // update two

    @Modifying  // 타이틀, 설명 변경
    @Query( value = "UPDATE blog_info SET title = :title, explanation = :explanation WHERE id = :id", nativeQuery = true)
    void updateTwoByTiTleAndExplanationById(@Param("title") String title, @Param("explanation") String ex, @Param("id") String id);

    @Modifying  // 타이틀, 이미지 변경
    @Query( value = "UPDATE blog_info SET title = :title, img_path = :ImgPath WHERE id = :id", nativeQuery = true)
    void updateTwoByTiTleAndImgPathById(@Param("title") String title, @Param("ImgPath") String imgPath, @Param("id") String id);

    @Modifying  // 이미지, 설명 변경
    @Query( value = "UPDATE blog_info SET img_path = :imgPath, explanation = :explanation WHERE id = :id", nativeQuery = true)
    void updateTwoByImgPathAndExplanationById(@Param("imgPath") String imgPath, @Param("explanation") String ex, @Param("id") String id);

    // update all
    @Modifying  // 타이틀, 이미지, 설명 변경
    @Query( value = "UPDATE blog_info SET title = :title, img_path = :imgPath, explanation = :ex WHERE id = :id", nativeQuery = true)
    void updateTIEById(@Param("title") String title,@Param("imgPath") String imgPath, @Param("ex") String ex, @Param("id") String id);


    /**
     * Delete
     */
    // remove method

    // Read
    @Override
    BlogInfo getOne(String id);
    Optional<BlogInfo> findById(String id);

}
