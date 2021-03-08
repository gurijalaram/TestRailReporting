package com.apriori.edc.utils;

import com.apriori.utils.FileResourceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "edc";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String edcTokenSubject;
    private static String edcTokenIssuer;
    private static String edcServiceHost;
    private static String secretKey;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            logger.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
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
     * Get service host
     *
     * @return string
     */
    public static String getEdcServiceHost() {
        return edcServiceHost = System.getProperty("edcServiceHost") == null ? PROPERTIES.getProperty("edc.service.host") : System.getProperty("edcServiceHost");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getEdcTokenIssuer() {
        return edcTokenIssuer = System.getProperty("edcTokenIssuer") == null ? PROPERTIES.getProperty("edc.token.issuer") : System.getProperty("edcTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getEdcTokenSubject() {
        return edcTokenSubject = System.getProperty("edcTokenSubject") == null ? PROPERTIES.getProperty("edc.token.subject") : System.getProperty("edcTokenSubject");
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("edcSecretKey") == null ? PROPERTIES.getProperty("secret.key") : System.getProperty("edcSecretKey");
    }
}
