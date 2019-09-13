package com.pollra.web.post.exception;

public class PostServiceException extends RuntimeException {
    public PostServiceException(String message) {
        super(message);
    }

    public PostServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
