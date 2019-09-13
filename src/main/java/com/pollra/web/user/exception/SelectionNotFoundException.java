package com.pollra.web.user.exception;

public class SelectionNotFoundException extends UserServiceException{

    public SelectionNotFoundException(String message) {
        super(message);
    }

    public SelectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
