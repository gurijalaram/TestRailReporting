package com.apriori.utils.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
