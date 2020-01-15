package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;

import java.util.logging.Level;


public class Constants {
    public static final String defaultProjectIDValue = "177";
    public static final String defaultProjectIDKey = "RUN_ID";

    public static final String defaultBuildModeKey = "mode";
    public static final String defaultBuildModeValue = "QA";

    public static final String defaultBaseUrlKey = "url";

    public static final String defaultUserName = "admin@apriori.com";
    public static final String defaultPassword = "admin";
    public static final String defaultAccessLevel = "admin";

    public static final String defaultEnvironmentKey = "env";
    public static final String defaultEnvironmentValue = "cid-te";

    public static final String scenarioExportChapterUrlPartOne = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String scenarioExportChapterUrlPartTwo = "CIA_UG";
    public static final String cidteReportingDomainDesignerUrl = "https://cid-aut.awsdev.apriori.com/jasperserver-pro/domaindesigner.html";
    public static final String cidautReportingDomainDesignerUrl = "https://cid-te.awsdev.apriori.com/jasperserver-pro/domaindesigner.html";
    public static final String reportsHomeUrl = "https://cid-te.awsdev.apriori.com/jasperserver-pro/";
    public static final String reportingHelpUrl = "http://help.jaspersoft.com/Default";
    public static final String privacyPolicyUrl = "https://www.apriori.com/privacy-policy";

    public static final String cidTeHeaderText = "CI Design (TE)";
    public static final String cidAutHeaderText = "CI Design (AUTOMATIONENVIRONMENT)";

    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";

    public static String RUN_ID = defaultProjectIDValue;

    public static String environment = System.getProperty(defaultEnvironmentKey, defaultEnvironmentValue);

    private static final ConstantsInit constantsInit;

    private static String buildMode;

    static {
        System.setProperty(defaultEnvironmentKey, environment);

        constantsInit = ConfigFactory.create(ConstantsInit.class);
    }

    private static String baseUrl;
    public static final Level consoleLogLevel = Level.parse(constantsInit.consoleLogLevelData());
    public static final String schemaBasePath = constantsInit.schemaBasePath();
    public static final String GRID_SERVER_URL = constantsInit.gridServerUrl();
    public static final String internalApiURL = getBaseUrl() + constantsInit.internalApiURL();
    public static final String cidURL = getBaseUrl() + constantsInit.cidURL();
    public static final String cirURL = getBaseUrl() + constantsInit.cirURL();
    public static final String ciaURL = getBaseUrl() + constantsInit.ciaURL();
    public static final String usersFile = constantsInit.usersCsvFileName();
    public static final Boolean useDifferentUsers = constantsInit.useDifferentUsers();

    public static String getBuildMode() {
        if (buildMode == null) {
            buildMode = System.getProperty(defaultBuildModeKey, defaultBuildModeValue);
        }

        return buildMode;
    }

    public static String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = System.getProperty(defaultBaseUrlKey, constantsInit.url());
        }

        return baseUrl;
    }

}