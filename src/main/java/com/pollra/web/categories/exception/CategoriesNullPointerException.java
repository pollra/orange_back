package com.pollra.web.categories.exception;

public class CategoriesNullPointerException extends CategoriesServiceException{
    public CategoriesNullPointerException(String message) {
        super(message);
    }

    public CategoriesNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}
