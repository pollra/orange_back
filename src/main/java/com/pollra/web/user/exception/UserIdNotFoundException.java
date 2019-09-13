package com.pollra.web.user.exception;

public class UserIdNotFoundException extends  UserServiceException {
    public UserIdNotFoundException(String message) {
        super(message);
    }

    public UserIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
