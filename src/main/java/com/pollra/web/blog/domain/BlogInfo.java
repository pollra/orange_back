package com.pollra.web.blog.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "blog_info")
public class BlogInfo {
    /**
     * 블로그 소유자의 아이디.
     */
    @Id
    @GeneratedValue
    private String id;

    /**
     * 블로그 타이틀
     * 블로그의 좌측 상단에 이름, 탭의 title 에 표시될 사이트 이름.
     */
    @NotNull
    @Column(unique = true)
    private String title;

    /**
     * 블로그 설명
     * 블로그의 좌측 상단, 타이틀의 아래에 표기될 설명이 들어가는 곳.
     */
    @NotNull
    private String explanation;

    @Column(name = "img_path")
    private String imgPath;
}
