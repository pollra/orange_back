package com.pollra.aop.jwt.exception;

public class JwtFindException extends JwtServiceException{
    public JwtFindException(String message) {
        super(message);
    }

    public JwtFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
