package com.pollra.web.post.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "post_list")
public class PostList {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String date;
    private String img_path;
}
/**
 * 해야할 것
 * JPA 는 블로그를 만들고 학습, 정리해서 올리는것으로 하고
 * 일단은 관계형 DB 가 아닌 일반적인 DB로 구현
 * 이 클래스를 사용할때는 PostData 와 PostInfo 의 데이터를
 * 각각의 Repository 로 불러와서 입력하는것으로 제작하는걸로.
 */