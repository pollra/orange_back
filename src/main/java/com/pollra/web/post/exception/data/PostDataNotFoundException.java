package com.pollra.web.post.exception.data;

import com.pollra.web.post.exception.PostServiceException;

public class PostDataNotFoundException extends PostServiceException {
    public PostDataNotFoundException(String message) {
        super(message);
    }

    public PostDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
