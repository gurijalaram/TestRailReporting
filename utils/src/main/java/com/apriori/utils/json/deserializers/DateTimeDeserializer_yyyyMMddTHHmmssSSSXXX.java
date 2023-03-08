package com.apriori.utils.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kpatel
 */
public class DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX extends JsonDeserializer<LocalDateTime> {
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            // making sure that date has max 3 trailing numbers after seconds
            // example: 2018-06-28T05:50:59.533242Z --> 2018-06-28T05:50:59.000Z
            String formattedTime = jsonParser.getText().substring(0, 20) + "000Z";
            return LocalDateTime.parse(formattedTime, formatter);
        } else if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
            String formattedTime = Instant.ofEpochMilli(jsonParser.getLongValue()).toString().substring(0, 19) + ".000Z";
            return LocalDateTime.parse(formattedTime, formatter);
        }
        return null;
    }
}