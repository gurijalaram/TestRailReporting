package com.apriori.trr.api.testrail.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;

/**
 * Deserializer string of form a,b\nc,d\ne,f to a map of form {a->b, c->d, e->f}.
 */
public class StringToMapDeserializer extends JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getValueAsString() == null) {
            return null;
        }
        Map<String, String> items = Splitter.on("\n").omitEmptyStrings().withKeyValueSeparator(',').split(jp.getValueAsString());
        items = Maps.transformValues(items, new Function<String, String>() {
            @Override
            public String apply(String value) {
                return value.trim();
            }
        });
        return items;
    }
}
