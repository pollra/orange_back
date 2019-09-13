package com.pollra.web.user.exception;

public class UserDataInsertionException extends UserServiceException {
    public UserDataInsertionException(String message) {
        super(message);
    }

    public UserDataInsertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
