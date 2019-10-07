package com.pollra.web.blog.exception;

public class SelectionNotFoundException extends BlogServiceException{
    public SelectionNotFoundException(String message) {
        super(message);
    }

    public SelectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
