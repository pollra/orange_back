package com.pollra.web.blog.exception;


import java.util.NoSuchElementException;

public class NoSuchBlogInfoException extends NoSuchElementException {
    public NoSuchBlogInfoException(String message) {
        super(message);
    }
}
