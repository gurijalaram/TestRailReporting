package com.apriori.sds.utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.CommonConstants;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    //public static String environment = CommonConstants.getEnvironment();
    private static String sdsApiUrl;
    private static String secretKey;
    private static String apUserContext;
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
//    public static final String DEFAULT_ENVIRONMENT_VALUE = "int-core";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "qa-21-1";
    public static String environment;

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
    public static String getApiUrl() {
        if (sdsApiUrl == null) {
            sdsApiUrl = System.getProperty("sdsApiUrl") == null ? PROPERTIES.getProperty("sds.api.url") : System.getProperty("sdsApiUrl");
        }
        return sdsApiUrl;
    }

    /**
     * Get secret Key of environment
     * @return
     */
    public static String getSecretKey() {
        if (secretKey == null) {
            secretKey = System.getProperty("secretKey") == null ? PROPERTIES.getProperty("sds.secret.key") : System.getProperty("secretKey");
        }

        return secretKey;
    }

    /**
     * Get user context
     *
     * @return string
     */
    public static String getApUserContext() {
        if (apUserContext == null) {
            apUserContext = System.getProperty("apUserContext") == null ? PROPERTIES.getProperty("ap.user.context") : System.getProperty("apUserContext");
        }

        return apUserContext;
    }
}
