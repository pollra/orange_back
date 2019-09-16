package com.pollra.aop.jwt.exception;

public class JwtTamperingDetectionException extends JwtServiceException {
    public JwtTamperingDetectionException(String message) {
        super(message);
    }

    public JwtTamperingDetectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
