package com.apriori.fms.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "fms";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    private static String fmsServiceHost;
    private static String fmsFileIdentity;
    private static String fmsTokenIssuer;
    private static String fmsTokenSubject;
    private static String serviceHost;

    static {
        System.setProperty(DEFAULT_ENVIRONMENT_KEY, ENVIRONMENT);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
