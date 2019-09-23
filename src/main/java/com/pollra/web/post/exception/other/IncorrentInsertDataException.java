package com.pollra.web.post.exception.other;

import com.pollra.web.post.exception.PostServiceException;

public class IncorrentInsertDataException extends PostServiceException {
    public IncorrentInsertDataException(String message) {
        super(message);
    }

    public IncorrentInsertDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
