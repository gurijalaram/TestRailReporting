package com.apriori.utils;

public class CustomFileException extends RuntimeException {

    public CustomFileException(String format) {
        super(format);
    }

    public CustomFileException(String message, Throwable cause) {
        super(message, cause);
    }
}