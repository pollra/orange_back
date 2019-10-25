package com.pollra.web.categories.exception;

public class CategoriesInsertException extends CategoriesServiceException{
    public CategoriesInsertException(String message) {
        super(message);
    }

    public CategoriesInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
