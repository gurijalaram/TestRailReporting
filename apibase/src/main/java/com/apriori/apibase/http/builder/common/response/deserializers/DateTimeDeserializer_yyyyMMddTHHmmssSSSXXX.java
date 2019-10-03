package com.apriori.apibase.http.builder.common.response.deserializers;

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
public class DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX extends JsonDeserializer<LocalDateTime> {
    private static DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        // making sure that date has max 3 trailing numbers after seconds
        // example: 2018-06-28T05:50:59.533242Z --> 2018-06-28T05:50:59.000Z
        String formattedTime = jsonParser.getText().substring(0, 20) + "000Z";

        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            return LocalDateTime.parse(formattedTime, formatter);
        }
        return null;
    }
}