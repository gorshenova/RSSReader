package com.egorshenova.rss.exceptions;

public class RSSVersionException extends Exception {

    public RSSVersionException() {
    }

    public RSSVersionException(String message) {
        super(message);
    }

    public RSSVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSSVersionException(Throwable cause) {
        super(cause);
    }
}
