package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;

import java.util.logging.Level;


public class Constants {
    public static final String defaultProjectIDValue = "177";
    public static final String defaultProjectIDKey = "RUN_ID";

    public static final String defaultBuildModeKey = "mode";
    public static final String defaultBuildModeValue = "QA";

    public static final String defaultUserName = "admin@apriori.com";
    public static final String defaultPassword = "admin";
    public static final String defaultAccessLevel = "admin";

    public static final String defaultEnvironmentKey = "env";
    public static final String defaultEnvironmentValue = "cid-te";

    public static String RUN_ID = defaultProjectIDValue;

    public static String environment = System.getProperty(defaultEnvironmentKey, defaultEnvironmentValue);

    private static final ConstantsInit constantsInit;

    private static String buildMode;

    static {
        System.setProperty(defaultEnvironmentKey, environment);

        constantsInit = ConfigFactory.create(ConstantsInit.class);
    }

    public static final Level consoleLogLevel = Level.parse(constantsInit.consoleLogLevelData());
    public static final String url = constantsInit.url();
    public static final String schemaBasePath = constantsInit.schemaBasePath();
    public static final String internalApiURL = constantsInit.internalApiURL();
    public static final String GRID_SERVER_URL = constantsInit.gridServerUrl();
    public static final String cidURL = constantsInit.cidURL();
    public static final String cirURL = constantsInit.cirURL();
    public static final String usersFile = constantsInit.usersCsvFileName();
    public static final Boolean useDifferentUsers = constantsInit.useDifferentUsers();

    public static String getBuildMode() {
        if (buildMode == null) {
            buildMode = System.getProperty(defaultBuildModeKey, defaultBuildModeValue);
        }

        return buildMode;
    }

}