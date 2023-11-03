package com.apriori.serialization.util.deserializers;

import com.apriori.serialization.util.DateFormattingUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author cfrith
 */

public class DateDeserializer_yyyyMMdd extends JsonDeserializer<LocalDate> {

    private static DateTimeFormatter formatter = DateFormattingUtils.dtf_yyyyMMdd;

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            return LocalDate.parse(jsonParser.getText(), formatter);
        }
        return null;
    }
}
