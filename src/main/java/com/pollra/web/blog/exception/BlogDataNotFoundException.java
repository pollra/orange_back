package com.pollra.web.blog.exception;

public class BlogDataNotFoundException extends BlogServiceException {
    public BlogDataNotFoundException(String message) {
        super(message);
    }

    public BlogDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
