package com.apriori.exceptions;

public class ArgumentNotValidException extends IllegalStateException {
    public ArgumentNotValidException(String argument) {
        super(argument.toUpperCase() + " is not a supported argument.");
    }
}
