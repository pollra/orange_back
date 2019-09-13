package com.pollra.web.user.exception;

public class UserDuplicateException extends UserServiceException {
    public UserDuplicateException(String message) {
        super(message);
    }

    public UserDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
