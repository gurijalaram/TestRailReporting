package com.apriori.utils.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kpatel
 */
public class DateTimeDeserializer_EEEMMMdHHmmsszyyyy extends JsonDeserializer<LocalDateTime> {

    private static DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss z yyyy");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            return LocalDateTime.parse(jsonParser.getText(), formatter);
        }
        return null;
    }
}