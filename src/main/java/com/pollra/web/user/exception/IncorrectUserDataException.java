package com.pollra.web.user.exception;

public class IncorrectUserDataException extends UserServiceException {
    public IncorrectUserDataException(String message) {
        super(message);
    }

    public IncorrectUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
