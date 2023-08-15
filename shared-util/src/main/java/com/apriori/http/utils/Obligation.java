package com.apriori.http.utils;

import org.openqa.selenium.NoSuchElementException;

import java.util.function.Supplier;

/**
 * Represents a helper class that can be used to force a null or non-null value.
 */
public abstract class Obligation {

    /**
     * Forces an exception on a null value.
     *
     * @param factory The factory object that will supply the object to retrieve.
     * @param <T>     The type of data to return.
     * @return The value from factory.  If this value is null, then a NoSuchElementException will be thrown.
     */
    public static <T> T mandatory(Supplier<T> factory, String errorIfMissing) throws NoSuchElementException {
        return mandatory(factory, () -> new NoSuchElementException(errorIfMissing));
    }

    /**
     * Forces an exception on a null value.
     *
     * @param factory The factory object that will supply the object to retrieve.
     * @param <T>     The type of data to return.
     * @return The value from factory.  If this value is null, then the exception factory will be invoked.
     */
    public static <T, S extends Throwable> T mandatory(Supplier<T> factory, Supplier<S> exceptionFactory) throws S {

        T value = factory.get();

        if (value == null) {
            throw exceptionFactory.get();
        }

        return value;
    }

    /**
     * Allows a factory that may throw an exception to return null instead.
     *
     * @param factory The factory that will supply the object to retrieve.  This method
     *                can throw an exception if needed.
     * @param <T>     The type of data to return.
     * @return The value from the factory, or null if factory throws an exception.
     */
    public static <T> T optional(Supplier<T> factory) {

        try {
            return factory.get();
        } catch (Exception t) {
            return null;
        }
    }
}
