package com.apriori.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kpatel
 */
public class DateTimeKeyDeserializer_yyyyMMddTHHmmssSSSZZZZ extends KeyDeserializer {

    private static DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) {
        return LocalDateTime.parse(s, formatter);
    }
}
