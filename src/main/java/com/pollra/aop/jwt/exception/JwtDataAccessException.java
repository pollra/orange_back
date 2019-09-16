package com.pollra.aop.jwt.exception;

public class JwtDataAccessException extends JwtServiceException {
    public JwtDataAccessException(String message) {
        super(message);
    }

    public JwtDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
