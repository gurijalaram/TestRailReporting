package com.apriori.utils.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonManager {

    public static Object serializeJsonFromFile(String fileName, Class klass) {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = mapper.readValue(new File(fileName), klass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
