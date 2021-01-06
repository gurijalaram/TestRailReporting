package com.apriori.fms.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "ats";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    private static String fmsServiceHost;
    private static String fmsFileIdentity;

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
        return fmsServiceHost = fmsServiceHost == null ? PROPERTIES.getProperty("fms.service.host") : System.getProperty("fmsServiceHost");
    }

    /**
     * Get file identity
     *
     * @return string
     */
    public static String getFmsFileIdentity() {
        return fmsFileIdentity = fmsFileIdentity == null ? PROPERTIES.getProperty("fms.file.identity") : System.getProperty("fmsFileIdentity");
    }

}
