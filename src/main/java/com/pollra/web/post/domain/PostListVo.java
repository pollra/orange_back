package com.pollra.web.post.domain;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Data
public class PostListVo {
    private String title;
    private String date;
    private String img_path;
    private String category;
    private String uri;

    public PostListVo(PostList list) {
        this.title = list.getTitle();
        this.date = list.getDate();
        this.category = list.getCategory();
        this.img_path = list.getImg_path();
        this.uri = list.getUri();
    }

    public PostListVo() {
    }
}
