package com.apriori.trr.api.testrail.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.util.List;

/**
 * Deserializer to convert csv string to {@code List<String>}.
 */
public class CsvToListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getValueAsString() == null) {
            return null;
        }
        return Splitter.on(',').trimResults().omitEmptyStrings().splitToList(jp.getValueAsString());
    }
}
