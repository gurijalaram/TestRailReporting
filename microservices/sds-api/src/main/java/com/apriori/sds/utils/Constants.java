package com.apriori.sds.utils;

import com.apriori.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "int-core";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String sdsApiUrl;
    private static String apUserContext = "eyJpZGVudGl0eSI6IjhHRkRJRzIyOTYyOSIsImNyZWF0ZWRCeSI6IiNTWVNURU0wMDAwMCIsImNyZWF0ZWRBdCI6IjIwMjAtMDctMzFUMTY6MDdaIiwidXBkYXRlZEF0IjoiMjAyMS0wNi0wN1QxMTo1NVoiLCJ1c2VyUHJvZmlsZSI6eyJpZGVudGl0eSI6IjhHRkRJRzIyTDMxNSIsImNyZWF0ZWRCeSI6IiNTWVNURU0wMDAwMCIsImNyZWF0ZWRBdCI6IjIwMjAtMDctMzFUMTY6MDdaIiwiZ2l2ZW5OYW1lIjoiUUEiLCJmYW1pbHlOYW1lIjoiQXV0b21hdGlvbiBBY2NvdW50IDAxIn0sImVtYWlsIjoicWEtYXV0b21hdGlvbi0wMUBhcHJpb3JpLmNvbSIsInVzZXJuYW1lIjoicWEtYXV0b21hdGlvbi0wMSIsImFjdGl2ZSI6dHJ1ZSwic2l0ZXMiOltdLCJjdXN0b21BdHRyaWJ1dGVzIjp7IndvcmtzcGFjZUlkIjoyMTF9LCJjdXN0b21lcklkZW50aXR5IjoiSDMzN0dLRDBMQTBNIiwidXNlclR5cGUiOiJBUF9DTE9VRF9VU0VSIiwibWZhUmVxdWlyZWQiOmZhbHNlfQ==";

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("sds-api-" + environment + ".properties");

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            log.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getDefaultUrl() {
        baseUrl = System.getProperty(DEFAULT_BASE_URL_KEY) == null ? PROPERTIES.getProperty("url.default") : System.getProperty(DEFAULT_BASE_URL_KEY);
        System.setProperty("baseUrl", baseUrl);

        return baseUrl;
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getApiUrl() {
        return sdsApiUrl = System.getProperty("sdsApiUrl") == null ? PROPERTIES.getProperty("sds.api.url") : System.getProperty("sdsApiUrl");
    }

    /**
     * Get user context
     *
     * @return string
     */
    public static String getUserContext() {
        return apUserContext;
    }
}
