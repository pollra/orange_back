package com.pollra.web.categories.exception;

public class CategoriesServiceException extends RuntimeException{
    public CategoriesServiceException(String message) {
        super(message);
    }

    public CategoriesServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
