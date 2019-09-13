package com.pollra.web.user.exception;

public class UserPasswordMatchingException extends UserServiceException {
    public UserPasswordMatchingException(String message) {
        super(message);
    }

    public UserPasswordMatchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
