package com.pollra.aop.jwt.exception;

public class JwtNotFoundException extends JwtServiceException {
    public JwtNotFoundException(String message) {
        super(message);
    }

    public JwtNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
