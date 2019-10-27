package com.pollra.web.categories.exception;

public class CategoriesDeleteException extends CategoriesServiceException {
    public CategoriesDeleteException(String message) {
        super(message);
    }

    public CategoriesDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
