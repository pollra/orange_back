package com.pollra.web.post.exception.info;

import com.pollra.web.post.exception.PostServiceException;

public class PostInfoInsertException extends PostServiceException {

    public PostInfoInsertException(String message) {
        super(message);
    }

    public PostInfoInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
