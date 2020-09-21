package com.pollra.web.post.entity;

import com.pollra.configuration.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Description("글번호")
    @Id
    @GeneratedValue
    private Long id;

    @Description("제목")
    @Column(nullable=false)
    private String title;

    @Description("내용")
    @Column(nullable=false)
    private String content;

    @Description("대표 이미지 웹 경로")
    private String imgPath;

    @Description("속해있는 카테고리")
    @Column(nullable=false)
    private String category;

    @Description("등록자")
    @Column(nullable=false)
    @CreatedBy
    private String createdBy;

    @Description("수정자")
    @LastModifiedBy
    private String updatedBy;

    @Description("등록일시")
    @CreatedDate
    private LocalDateTime createdAt;

    @Description("수정일시")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void set(Post post) {
        this.category   = post.category;
        this.title      = post.title;
        this.content    = post.content;
        this.imgPath    = post.imgPath;
        this.createdAt  = post.createdAt;
        this.createdBy  = post.createdBy;
        this.updatedAt  = post.updatedAt;
        this.updatedBy  = post.updatedBy;
    }
}
