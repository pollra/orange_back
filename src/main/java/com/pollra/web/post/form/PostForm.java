package com.pollra.web.post.form;

import com.pollra.configuration.Description;
import com.pollra.web.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class PostForm {
    public static class Request {
        @Data
        @AllArgsConstructor
        public static class Find {
            private String title;
        }

        @Data
        public static class Add {
            private String title;
            private String content;
            private String imgPath;
            private String category;
            private String createdBy;
            private LocalDateTime createdAt;
            private String updatedBy;
            private LocalDateTime updatedAt;
        }

        @Data
        public static class Modify {
            private String title;
            private String content;
            private String imgPath;
            private String category;
            private String updatedBy;
            private LocalDateTime updatedAt;
        }
    }

    public static class Response {
        @Data
        public static class FindOne {

        }

        @Data
        public static class FindAll {

            @Description("글번호")
            private Long id;

            @Description("제목")
            private String title;

            @Description("내용")
            private String content;

            @Description("대표 이미지 웹 경로")
            private String imgPath;

            @Description("속해있는 카테고리")
            private String category;

            @Description("등록자")
            private String createdBy;

            @Description("수정자")
            private String updatedBy;

            @Description("등록일시")
            private LocalDateTime createdAt;

            @Description("수정일시")
            private LocalDateTime updatedAt;

            public FindAll form(Post post) {
                this.id        = post.getId()       ;
                this.title     = post.getTitle()    ;
                this.content   = post.getContent()  ;
                this.imgPath   = post.getImgPath()  ;
                this.category  = post.getCategory() ;
                this.createdBy = post.getCreatedBy();
                this.updatedBy = post.getUpdatedBy();
                this.createdAt = post.getCreatedAt();
                this.updatedAt = post.getUpdatedAt();
                return this;
            }
        }
    }
}
