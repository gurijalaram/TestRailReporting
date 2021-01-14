package com.apriori.fms.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "fms";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String fmsServiceHost;
    private static String fmsFileIdentity;
    private static String fmsTokenIssuer;
    private static String fmsTokenSubject;
    private static String serviceHost;
    private static String fmsAuthTargetCloudContext;
    private static String secretKey;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            PROPERTIES.list(System.out);
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
    public static String getFmsServiceHost() {
        return fmsServiceHost = System.getProperty("fmsServiceHost") == null ? PROPERTIES.getProperty("fms.service.host") : System.getProperty("fmsServiceHost");
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getServiceHost() {
        return serviceHost = System.getProperty("serviceHost") == null ? PROPERTIES.getProperty("service.host") : System.getProperty("serviceHost");
    }

    /**
     * Get file identity
     *
     * @return string
     */
    public static String getFmsFileIdentity() {
        return fmsFileIdentity = System.getProperty("fmsFileIdentity") == null ? PROPERTIES.getProperty("fms.file.identity") : System.getProperty("fmsFileIdentity");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getFmsTokenIssuer() {
        return fmsTokenIssuer = System.getProperty("fmsTokenIssuer") == null ? PROPERTIES.getProperty("fms.token.issuer") : System.getProperty("fmsTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getFmsTokenSubject() {
        return fmsTokenSubject = System.getProperty("fmsTokenSubject") == null ? PROPERTIES.getProperty("fms.token.subject") : System.getProperty("fmsTokenSubject");
    }

    /**
     * Get target cloud context
     *
     * @return string
     */
    public static String getFmsAuthTargetCloudContext() {
        return fmsAuthTargetCloudContext = System.getProperty("fmsAuthTargetCloudContext") == null ? PROPERTIES.getProperty("fms.auth.targetCloudContext") : System.getProperty("fmsAuthTargetCloudContext");
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("fmsSecretKey") == null ? PROPERTIES.getProperty("fms.secret.key") : System.getProperty("fmsSecretKey");
    }
}
