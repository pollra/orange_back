package com.pollra.web.post.exception.other;

import com.pollra.web.post.exception.PostServiceException;

public class IncorrectPostDataException extends PostServiceException {
    public IncorrectPostDataException(String message) {
        super(message);
    }

    public IncorrectPostDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
