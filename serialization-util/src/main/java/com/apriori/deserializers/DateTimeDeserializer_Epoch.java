package com.apriori.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author rgurijala
 */

public class DateTimeDeserializer_Epoch extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
            return Instant.ofEpochMilli(jsonParser.readValueAs(Long.class)).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }
}
