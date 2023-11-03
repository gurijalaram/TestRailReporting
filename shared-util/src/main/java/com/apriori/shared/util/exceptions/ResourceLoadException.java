package com.apriori.shared.util.exceptions;

public class ResourceLoadException extends RuntimeException {

    public ResourceLoadException(String format) {
        super(format);
    }

    public ResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}