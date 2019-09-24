package com.pollra.web.post.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "post_info")
public class PostInfo {

    /**
     * 포스트(글) 번호
     */
    @Id
    private Long num;

    /**
     * 개인 포스트(글) 번호
     */
    @Column(nullable = false)
    private String uri;

    /**
     * 포스트(글) 작성자
     */
    @Column(nullable = false)
    private String owner;

    /**
     * 포스트(글) 작성일
     */
    private String date;

    /**
     * 대표 이미지 웹 경로
     */
    private String imgPath;

    /**
     * 속해있는 카테고리
     */
    private String category;
}
