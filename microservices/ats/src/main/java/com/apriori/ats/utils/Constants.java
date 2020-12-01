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

    public static String getAtsServiceHost() {
        return atsServiceHost = atsServiceHost == null ? System.getProperty("atsServiceHost", PROPERTIES.getProperty("ats.service.host")) : atsServiceHost;
    }

    public static String getAtsTokenUsername() {
        return atsTokenUsername = atsTokenUsername == null ? System.getProperty("atsTokenUsername", PROPERTIES.getProperty("ats.token.username")) : atsTokenUsername;
    }

    public static String getAtsTokenEmail() {
        return atsTokenEmail = atsTokenEmail == null ? System.getProperty("atsTokenEmail", PROPERTIES.getProperty("ats.token.email")) : atsTokenEmail;
    }

    public static String getAtsTokenIssuer() {
        return atsTokenIssuer = atsTokenIssuer == null ? System.getProperty("atsTokenIssuer", PROPERTIES.getProperty("ats.token.issuer")) : atsTokenIssuer;
    }

    public static String getAtsTokenSubject() {
        return atsTokenSubject = atsTokenSubject == null ? System.getProperty("atsTokenSubject", PROPERTIES.getProperty("ats.token.subject")) : atsTokenSubject;
    }

    public static String getAtsAuthApplication() {
        return atsAuthApplication = atsAuthApplication == null ? System.getProperty("atsAuthApplication", PROPERTIES.getProperty("ats.auth.application")) : atsAuthApplication;
    }

    public static String getAtsAuthTargetCloudContext() {
        return atsAuthTargetCloudContext = atsAuthTargetCloudContext == null ? System.getProperty("atsAuthTargetCloudContext", PROPERTIES.getProperty("ats.auth.targetCloudContext")) : atsAuthTargetCloudContext;
    }
}
