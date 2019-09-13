package com.pollra.web.post.exception.data;

import com.pollra.web.post.exception.PostServiceException;

public class PostDataInsertException extends PostServiceException {
    public PostDataInsertException(String message) {
        super(message);
    }

    public PostDataInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
