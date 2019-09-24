package com.pollra.web.post.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
board_info:{
  parent: "main",
  title: "포스팅을 확인할 수 없습니다.",
  date: "",
  comment: "",
  contents: ""
},*/
@Data
@Getter
@ToString
public class PostDataVo {
    private String category;
    private String title;
    private String date;
    private String comment;
    private String contents;

    public PostDataVo(PostData postData) {
        this.category = "";
        this.title = postData.getTitle();
        this.date = "";
        this.comment = "";
        this.contents = postData.getPostContent();
    }

    public PostDataVo(PostData data, PostInfo info) {
        this.category = info.getCategory();
        this.title = data.getTitle();
        this.date = info.getDate();
        this.comment = "";
        this.contents = data.getPostContent();
    }
}
