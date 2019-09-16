package com.pollra.aop.jwt.exception;

public class JwtToolException extends JwtServiceException {
    public JwtToolException(String message) {
        super(message);
    }

    public JwtToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
