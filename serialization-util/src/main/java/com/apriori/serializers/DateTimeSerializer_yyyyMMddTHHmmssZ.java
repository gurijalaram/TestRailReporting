package com.apriori.serializers;

import com.apriori.DateFormattingUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kpatel
 */
public class DateTimeSerializer_yyyyMMddTHHmmssZ extends JsonSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter = DateFormattingUtils.dtf_yyyyMMddTHHmmssZ;

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeString(localDateTime.format(formatter));
    }
}
