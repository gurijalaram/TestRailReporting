package com.apriori.utils.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonManager {
    private static ObjectMapper mapper = new ObjectMapper();

    public static Object deserializeJsonFromFile(String fileName, Class klass) {

        Object obj = null;
        try {
            obj = mapper.readValue(new File(fileName), klass);
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
