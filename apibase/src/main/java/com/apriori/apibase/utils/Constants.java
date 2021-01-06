package com.apriori.apibase.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    private static String atsAuthTargetCloudContext;

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "apibase";
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

    public static String getAtsAuthTargetCloudContext() {
        return atsAuthTargetCloudContext = atsAuthTargetCloudContext == null ? System.getProperty("atsAuthTargetCloudContext", PROPERTIES.getProperty("ats.auth.targetCloudContext")) : atsAuthTargetCloudContext;
    }
}
