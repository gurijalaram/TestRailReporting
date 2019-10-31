package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;

import java.util.logging.Level;


public class Constants {
    public static final String defaultEnvironmentValue = "cid-te";
    public static final String defaultEnvironmentKey = "env";
    public static String environment = defaultEnvironmentValue;

    private static final ConstantsInit constantsInit;

    static {

        if (System.getProperty(defaultEnvironmentKey) == null) {
            System.setProperty(defaultEnvironmentKey, defaultEnvironmentValue);
        } else {
            environment = System.getProperty(defaultEnvironmentKey);
        }

        constantsInit = ConfigFactory.create(ConstantsInit.class);
    }

    public static final String defaultUserName = "admin@apriori.com";
    public static final String defaultPassword = "admin";
    public static final String defaultAccessLevel = "admin";
    public static final Level consoleLogLevel = Level.parse(constantsInit.consoleLogLevelData());
    public static final String url = constantsInit.url();
    public static final String schemaBasePath = constantsInit.schemaBasePath();
    public static final String internalApiURL = constantsInit.internalApiURL();
    public static final String GRID_SERVER_URL = constantsInit.gridServerUrl();
    public static final String cidURL = constantsInit.cidURL();
    public static final String cirURL = constantsInit.cirURL();
    public static final String usersFile = constantsInit.usersCsvFileName();
    public static final Boolean useDifferentUsers = constantsInit.useDifferentUsers();
}