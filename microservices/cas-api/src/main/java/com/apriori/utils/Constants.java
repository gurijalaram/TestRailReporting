package com.apriori.utils;

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
    public static final String DEFAULT_ENVIRONMENT_VALUE = "qa-21-1";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String secretKey;
    private static String casServiceHost;
    private static String casTokenUsername;
    private static String casTokenEmail;
    private static String casTokenIssuer;
    private static String casTokenSubject;
    private static String casApiUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("cas-api-" + environment + ".properties");

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

        return baseUrl.concat(PROPERTIES.getProperty("url.additional"));
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getApiUrl() {
        return casApiUrl = System.getProperty("casApiUrl") == null ? PROPERTIES.getProperty("cas.api.url").concat("%s") : System.getProperty("casApiUrl");
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("casSecretKey") == null ? PROPERTIES.getProperty("cas.secret.key") : System.getProperty("casSecretKey");
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getCasServiceHost() {
        casServiceHost = System.getProperty("casServiceHost") == null ? PROPERTIES.getProperty("cas.service.host") : System.getProperty("casServiceHost");
        return casServiceHost;
    }

    /**
     * Get token username
     *
     * @return string
     */
    public static String getCasTokenUsername() {
        return casTokenUsername = System.getProperty("casTokenUsername") == null ? PROPERTIES.getProperty("cas.token.username") : System.getProperty("casTokenUsername");
    }

    /**
     * Get token email
     *
     * @return string
     */
    public static String getCasTokenEmail() {
        return casTokenEmail = System.getProperty("casTokenEmail") == null ? PROPERTIES.getProperty("cas.token.email") : System.getProperty("casTokenEmail");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getCasTokenIssuer() {
        return casTokenIssuer = System.getProperty("casTokenIssuer") == null ? PROPERTIES.getProperty("cas.token.issuer") : System.getProperty("casTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getCasTokenSubject() {
        return casTokenSubject = System.getProperty("casTokenSubject") == null ? PROPERTIES.getProperty("cas.token.subject") : System.getProperty("casTokenSubject");
    }
}