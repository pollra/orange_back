package com.pollra.web.blog.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@NotEmpty
@Data
@Getter
public class BlogInfoVo {
    /*
      title:"Pollra 블로그",
      explanation: "잊을만 하면 찾아오는 기억 저장소",
      imgPath: "",
      meta:""
     */
    private String title;
    private String explanation;
    private String imgPath;
    private String metaTitle;

    public BlogInfoVo(BlogInfo blogInfo) {
        this.title = blogInfo.getTitle();
        this.explanation = blogInfo.getExplanation();
        this.imgPath = blogInfo.getImgPath();
        this.metaTitle = blogInfo.getMetaTitle();
    }

    public BlogInfoVo(String title, String explanation, String imgPath, String metaTitle) {
        this.title = title;
        this.explanation = explanation;
        this.imgPath = imgPath;
        this.metaTitle = metaTitle;
    }

    public String[] EmptySet(){
        return new String[]{this.title, this.explanation, this.imgPath, this.metaTitle};
    }
}
