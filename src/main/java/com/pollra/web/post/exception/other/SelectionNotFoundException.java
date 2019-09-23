package com.pollra.web.post.exception.other;

import com.pollra.web.post.exception.PostServiceException;

public class SelectionNotFoundException extends PostServiceException {
    public SelectionNotFoundException(String message) {
        super(message);
    }

    public SelectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
