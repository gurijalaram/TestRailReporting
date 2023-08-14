package com.apriori.utils.json.utils;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JsonManager {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T deserializeJsonFromInputStream(InputStream inputStream, Class<T> klass) {
        T value = null;
        try {
            value = mapper.readValue(inputStream, klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static <T> T deserializeJsonFromFile(String fileName, Class<T> klass) {

        T value = null;
        try {
            value = mapper.readValue(new File(fileName), klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static <T> T deserializeJsonFromString(String json, Class<T> klass) {

        T value = null;
        try {
            value = mapper.readValue(json, klass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void serializeJsonToFile(String fileName, Object object) {
        try {
            mapper.writeValue(
                new FileOutputStream(fileName), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> deserializeJsonToMap(String jsonKeyValuePairString) {
        if (null == jsonKeyValuePairString || jsonKeyValuePairString.equals("") || !isJsonValid(jsonKeyValuePairString)) {
            throw new IllegalArgumentException("No valid JSON string was provided for deserialization!");
        }
        Map<String, Object> returnMap = null;
        try {
            returnMap = mapper.readValue(jsonKeyValuePairString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    public static boolean isJsonValid(String json) {
        Gson gson = new Gson();
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static <T> T convertBodyToJson(ResponseWrapper<String> response, Class<T> klass) {
        Gson gson = new Gson();
        return gson.fromJson(
            gson.toJson(
                JsonParser.parseString(
                    response.getBody()).getAsJsonObject().getAsJsonObject("response")),
            klass);
    }
}
