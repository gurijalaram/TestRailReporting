package com.apriori.ats.utils;

import com.apriori.utils.FileResourceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "ats";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String atsServiceHost;
    private static String atsTokenUsername;
    private static String atsTokenEmail;
    private static String atsTokenIssuer;
    private static String atsTokenSubject;
    private static String atsAuthApplication;
    private static String atsAuthTargetCloudContext;
    private static String secretKey;
    private static String baseUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            LOGGER.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
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
    public static String getAtsServiceHost() {
        atsServiceHost = System.getProperty("atsServiceHost") == null ? PROPERTIES.getProperty("ats.service.host") : System.getProperty("atsServiceHost");
        return atsServiceHost;
    }

    /**
     * Get token username
     *
     * @return string
     */
    public static String getAtsTokenUsername() {
        return atsTokenUsername = System.getProperty("atsTokenUsername") == null ? PROPERTIES.getProperty("ats.token.username") : System.getProperty("atsTokenUsername");
    }

    /**
     * Get token email
     *
     * @return string
     */
    public static String getAtsTokenEmail() {
        return atsTokenEmail = System.getProperty("atsTokenEmail") == null ? PROPERTIES.getProperty("ats.token.email") : System.getProperty("atsTokenEmail");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getAtsTokenIssuer() {
        return atsTokenIssuer = System.getProperty("atsTokenIssuer") == null ? PROPERTIES.getProperty("ats.token.issuer") : System.getProperty("atsTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getAtsTokenSubject() {
        return atsTokenSubject = System.getProperty("atsTokenSubject") == null ? PROPERTIES.getProperty("ats.token.subject") : System.getProperty("atsTokenSubject");
    }

    /**
     * Get auth application
     *
     * @return string
     */
    public static String getAtsAuthApplication() {
        return atsAuthApplication = System.getProperty("atsAuthApplication") == null ? PROPERTIES.getProperty("ats.auth.application") : System.getProperty("atsAuthApplication");
    }

    /**
     * Get auth target cloud context
     *
     * @return string
     */
    public static String getAtsAuthTargetCloudContext() {
        return atsAuthTargetCloudContext = System.getProperty("atsAuthTargetCloudContext") == null ? PROPERTIES.getProperty("ats.auth.targetCloudContext") : System.getProperty("atsAuthTargetCloudContext");
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("atsSecretKey") == null ? PROPERTIES.getProperty("ats.secret.key") : System.getProperty("atsSecretKey");
    }
}
