package com.pollra.web.post.exception.other;

import com.pollra.web.post.exception.PostServiceException;

public class PostNullPointerException extends PostServiceException {
    public PostNullPointerException(String message) {
        super(message);
    }

    public PostNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}
