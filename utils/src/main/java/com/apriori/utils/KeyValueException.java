package com.apriori.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyValueException extends RuntimeException {

    public KeyValueException(String message, List<String[]> throwableCause) {
        System.out.println("\n" + message);
        System.out.println("\nIs your key/value pair correct and complete?\n");
        throwableCause.forEach(o -> System.out.println(Arrays.stream(o).map(String::toUpperCase).collect(Collectors.joining(" > "))));
    }
}
