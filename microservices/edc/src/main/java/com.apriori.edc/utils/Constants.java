package com.apriori.edc.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "edc";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    private static String edcTokenSubject;
    private static String edcTokenIssuer;
    private static String edcServiceHost;

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
    public static String getEdcServiceHost() {
        return edcServiceHost = edcServiceHost == null ? PROPERTIES.getProperty("edc.service.host") : System.getProperty("edcServiceHost");
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getEdcTokenIssuer() {
        return edcTokenIssuer = edcTokenIssuer == null ? PROPERTIES.getProperty("edc.token.issuer") : System.getProperty("edcTokenIssuer");
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getEdcTokenSubject() {
        return edcTokenSubject = edcTokenSubject == null ? PROPERTIES.getProperty("edc.token.subject") : System.getProperty("edcTokenSubject");
    }
}
