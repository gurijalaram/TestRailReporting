package com.apriori.http.utils;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author kpatel
 */
public class ByteArrayObjectMapper implements ObjectMapper {

    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public InputStream serialize(ObjectMapperSerializationContext context) {
        byte[] byteArr = context.getObjectToSerializeAs(byte[].class);
        return new ByteArrayInputStream(byteArr);
    }
}