package com.pollra.web.post.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "post_data")
public class PostData {
    /**
     * 포스트(글) 번호
     */
    @Id
    @GeneratedValue
    private Long num;

    /**
     * 포스트(글) 제목
     */
    @Column(nullable = false)
    private String title;

    /**
     * 포스트(글) 내용
     */
    @Column(nullable = false, name = "post_content")
    private String PostContent;

}
