package com.pollra.web.post.exception.data;

import com.pollra.web.post.exception.PostServiceException;

public class PostNotFoundException extends PostServiceException {
    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
