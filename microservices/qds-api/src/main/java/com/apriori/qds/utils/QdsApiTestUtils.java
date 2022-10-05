package com.apriori.qds.utils;

import java.util.HashMap;
import java.util.Map;

public class QdsApiTestUtils {


    /**
     * setup header information for QDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader(String userContext) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("ap-user-context", userContext);
        header.put("Content-Type","application/json");
        return header;
    }

    public static Map<String, String> setUpHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type","application/json");
        return header;
    }

}
