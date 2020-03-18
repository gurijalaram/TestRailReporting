package com.apriori.utils;

public class ResourceFileException extends RuntimeException {

    public ResourceFileException(String format) {
        super(format);
    }

    public ResourceFileException(String message, Throwable cause) {
        super(message, cause);
    }
}