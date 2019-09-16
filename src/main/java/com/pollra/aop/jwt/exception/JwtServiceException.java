package com.pollra.aop.jwt.exception;

public class JwtServiceException extends RuntimeException {
    public JwtServiceException(String message) {
        super(message);
    }

    public JwtServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
