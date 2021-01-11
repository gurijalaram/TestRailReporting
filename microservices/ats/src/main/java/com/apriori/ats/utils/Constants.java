package com.apriori.ats.utils;

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
    private static String atsServiceHost;
    private static String atsTokenUsername;
    private static String atsTokenEmail;
    private static String atsTokenIssuer;
    private static String atsTokenSubject;
    private static String atsAuthApplication;
    private static String atsAuthTargetCloudContext;
    private static String secretKey;

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
