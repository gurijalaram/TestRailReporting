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
    public static final String defaultEnvironmentValue = "cid-aut";

    public static final String scenarioExportChapterUrlPartOne = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String scenarioExportChapterUrlPartTwo = "CIA_UG";
    public static final String scenarioExportChapterPageTitle = "2 Scenario and System Data Exports";
    public static final String reportsUrlSuffix = "jasperserver-pro/";
    public static final String domainDesignerUrlSuffix = String.format("%sdomaindesigner.html", reportsUrlSuffix);
    public static final String reportsLastSuffix = "flow.html?_flowId=homeFlow";
    public static final String reportingHelpUrl = "http://help.jaspersoft.com/Default";
    public static final String privacyPolicyUrl = "https://www.apriori.com/privacy-policy";

    public static final String cidTeHeaderText = "CI Design (TE)";
    public static final String cidAutHeaderText = "CI Design (AUTOMATIONENVIRONMENT)";

    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";

    public static String RUN_ID = defaultProjectIDValue;

    public static String environment = System.getProperty(defaultEnvironmentKey, defaultEnvironmentValue);

    private static String serviceHost;
    private static String servicePort;
    private static String serviceName;
    private static String secretKey;
    private static String cdsIdentityUser;
    private static String cdsIdentityRole;
    private static String cdsIdentityCustomer;
    private static String cdsIdentityApplication;
    public static final String defaultServiceHostKey = "serverHost";
    public static final String defaultServicePortKey = "serverPort";
    public static final String defaultServiceNameKey = "serverName";
    public static final String defaultSecretKeyKey = "secretKey";
    public static final String defaultCdsIdentityUserKey = "cdsIdentityUser";
    public static final String defaultCdsIdentityRoleKey = "cdsIdentityRole";
    public static final String defaultCdsIdentityCustomerKey = "cdsIdentityCustomer";
    public static final String defaultCdsIdentityApplicationKey = "cdsIdentityApplication";


    private static final ConstantsInit constantsInit;

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

    private static String buildMode;

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

    public static String getServiceHost() {
        if (serviceHost == null) {
            serviceHost = System.getProperty(defaultServiceHostKey, constantsInit.serviceHost());
        }

        return serviceHost;
    }

    public static String getServicePort() {
        if (servicePort == null) {
            servicePort = System.getProperty(defaultServicePortKey, constantsInit.servicePort());
        }
        return servicePort;
    }

    public static String getServiceName() {
        if (serviceName == null) {
            serviceName = System.getProperty(defaultServiceNameKey, constantsInit.serviceName());
        }

        return serviceName;
    }

    public static String getSecretKey() {
        if (secretKey == null) {
            secretKey = System.getProperty(defaultSecretKeyKey, constantsInit.secretKey());
        }

        return secretKey;
    }

    public static String getCdsIdentityUser() {
        if (cdsIdentityUser == null) {
            cdsIdentityUser = System.getProperty(defaultCdsIdentityUserKey, constantsInit.cdsIdentityUser());
        }

        return cdsIdentityUser;
    }

    public static String getCdsIdentityRole() {
        if (cdsIdentityRole == null) {
            cdsIdentityRole = System.getProperty(defaultCdsIdentityRoleKey, constantsInit.cdsIdentityRole());
        }

        return cdsIdentityRole;
    }

    public static String getCdsIdentityCustomer() {
        if (cdsIdentityCustomer == null) {
            cdsIdentityCustomer = System.getProperty(defaultCdsIdentityCustomerKey, constantsInit.cdsIdentityCustomer());
        }

        return cdsIdentityCustomer;
    }

    public static String getCdsIdentityApplication() {
        if (cdsIdentityApplication == null) {
            cdsIdentityApplication = System.getProperty(defaultCdsIdentityApplicationKey, constantsInit.cdsIdentityApplication());
        }

        return cdsIdentityApplication;
    }

}