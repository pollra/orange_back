package com.pollra.web.categories.exception;

import java.util.NoSuchElementException;

public class NoSuchCategoriesException extends NoSuchElementException {
    public NoSuchCategoriesException(String message) {
        super(message);
    }
}
