package com.apriori.utils.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonManager {
    private static ObjectMapper mapper = new ObjectMapper();

    public static Object deserializeJsonFromInputStream(InputStream inputStream, Class klass) {
        Object obj = null;
        try {
            obj = mapper.readValue(inputStream, klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }


    public static Object deserializeJsonFromFile(String fileName, Class klass) {

        Object obj = null;
        try {
            obj = mapper.readValue(new File(fileName), klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Object deserializeJsonFromString(String json, Class klass) {

        Object obj = null;
        try {
            obj = mapper.readValue(json, klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
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
