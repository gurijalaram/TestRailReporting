package com.apriori.shared.util.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class KeyValueException extends RuntimeException {

    public KeyValueException(String message, List<String[]> throwableCause) {
        log.debug("" + message + ". Is your key/value pair correct and complete?");
        throwableCause.forEach(o -> log.debug(Arrays.stream(o).map(String::toUpperCase).collect(Collectors.joining(" > "))));
    }
}
