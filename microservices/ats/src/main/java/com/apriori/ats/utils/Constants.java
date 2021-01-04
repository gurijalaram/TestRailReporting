package com.apriori.ats.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    private static String atsServiceHost;
    private static String atsTokenUsername;
    private static String atsTokenEmail;
    private static String atsTokenIssuer;
    private static String atsTokenSubject;
    private static String atsAuthApplication;
    private static String atsAuthTargetCloudContext;

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "ats";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

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
    public static String getAtsServiceHost() {
        return atsServiceHost = atsServiceHost == null ? System.getProperty("atsServiceHost", PROPERTIES.getProperty("ats.service.host")) : atsServiceHost;
    }

    /**
     * Get token username
     *
     * @return string
     */
    public static String getAtsTokenUsername() {
        return atsTokenUsername = atsTokenUsername == null ? System.getProperty("atsTokenUsername", PROPERTIES.getProperty("ats.token.username")) : atsTokenUsername;
    }

    /**
     * Get token email
     *
     * @return string
     */
    public static String getAtsTokenEmail() {
        return atsTokenEmail = atsTokenEmail == null ? System.getProperty("atsTokenEmail", PROPERTIES.getProperty("ats.token.email")) : atsTokenEmail;
    }

    /**
     * Get token issuer
     *
     * @return string
     */
    public static String getAtsTokenIssuer() {
        return atsTokenIssuer = atsTokenIssuer == null ? System.getProperty("atsTokenIssuer", PROPERTIES.getProperty("ats.token.issuer")) : atsTokenIssuer;
    }

    /**
     * Get token subject
     *
     * @return string
     */
    public static String getAtsTokenSubject() {
        return atsTokenSubject = atsTokenSubject == null ? System.getProperty("atsTokenSubject", PROPERTIES.getProperty("ats.token.subject")) : atsTokenSubject;
    }

    /**
     * Get auth application
     *
     * @return string
     */
    public static String getAtsAuthApplication() {
        return atsAuthApplication = atsAuthApplication == null ? System.getProperty("atsAuthApplication", PROPERTIES.getProperty("ats.auth.application")) : atsAuthApplication;
    }

    /**
     * Get auth target cloud context
     *
     * @return string
     */
    public static String getAtsAuthTargetCloudContext() {
        return atsAuthTargetCloudContext = atsAuthTargetCloudContext == null ? System.getProperty("atsAuthTargetCloudContext", PROPERTIES.getProperty("ats.auth.targetCloudContext")) : atsAuthTargetCloudContext;
    }
}
