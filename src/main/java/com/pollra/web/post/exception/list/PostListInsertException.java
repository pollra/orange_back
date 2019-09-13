package com.pollra.web.post.exception.list;

import com.pollra.web.post.exception.PostServiceException;

public class PostListInsertException extends PostServiceException {
    public PostListInsertException(String message) {
        super(message);
    }

    public PostListInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
