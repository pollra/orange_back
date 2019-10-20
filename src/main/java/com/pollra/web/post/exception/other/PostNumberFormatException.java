package com.pollra.web.post.exception.other;

import com.pollra.web.post.exception.PostServiceException;

public class PostNumberFormatException extends PostServiceException {
    public PostNumberFormatException(String message) {
        super(message);
    }

    public PostNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
