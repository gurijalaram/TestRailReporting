package com.apriori.utils.constants;

import org.aeonbits.owner.ConfigFactory;

import java.util.logging.Level;


public class Constants {
    public static final String DEFAULT_PROJECT_ID_VALUE = "177";
    public static final String DEFAULT_PROJECT_ID_KEY = "RUN_ID";

    public static final String DEFAULT_BUILD_MODE_KEY = "mode";
    public static final String DEFAULT_BUILD_MODE_VALUE = "QA";

    public static final String DEFAULT_BASE_URL_KEY = "url";

    public static final String DEFAULT_USER_NAME = "admin@apriori.com";
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_ACCESS_LEVEL = "admin";

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cid-aut";

    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_ONE = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_TWO = "CIA_UG";
    public static final String SCENARIO_EXPORT_CHAPTER_PAGE_TITLE = "2 Scenario and System Data Exports";
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTS_LAST_SUFFIX = "flow.html?_flowId=homeFlow";
    public static final String REPORTING_HELP_URL = "http://help.jaspersoft.com/Default";
    public static final String PRIVACY_POLICY_URL = "https://www.apriori.com/privacy-policy";

    public static final String CID_TE_HEADER_TEXT = "CI Design (TE)";
    public static final String CID_AUT_HEADER_TEXT = "CI Design (AUTOMATIONENVIRONMENT)";

    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";
    public static final String HORIZONTAL_SCROLL = "horizontal_scroll";

    public static final String DEFAULT_SCENARIO_NAME = "Initial";

    public static String RUN_ID = DEFAULT_PROJECT_ID_VALUE;

    public static String environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);

    private static String serviceHost;
    private static String servicePort;
    private static String serviceName;
    private static String secretKey;
    private static String cdsIdentityUser;
    private static String cdsIdentityRole;
    private static String cdsIdentityCustomer;
    private static String cdsIdentityApplication;
    private static String atsServiceHost;
    private static String atsTokenUsername;
    private static String atsTokenEmail;
    private static String atsTokenIssuer;
    private static String atsTokenSubject;
    private static String atsAuthApplication;
    private static String atsAuthTargetCloudContext;
    private static String fmsServiceHost;
    private static String fmsFileIdentity;
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
        System.setProperty(DEFAULT_ENVIRONMENT_KEY, environment);

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
            buildMode = System.getProperty(DEFAULT_BUILD_MODE_KEY, DEFAULT_BUILD_MODE_VALUE);
        }

        return buildMode;
    }

    public static String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = System.getProperty(DEFAULT_BASE_URL_KEY, constantsInit.url());
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

    public static String getAtsServiceHost() {
        if (atsServiceHost == null) {
            atsServiceHost = System.getProperty("atsServiceHost", constantsInit.atsServiceHost());
        }

        return atsServiceHost;
    }

    public static String getFmsServiceHost() {
        if (fmsServiceHost == null) {
            fmsServiceHost = System.getProperty("fmsServiceHost", constantsInit.fmsServiceHost());
        }

        return fmsServiceHost;
    }

    public static String getFmsFileIdentity() {
        if (fmsFileIdentity == null) {
            fmsFileIdentity = System.getProperty("fmsFileIdentity", constantsInit.fmsFileIdentity());
        }

        return fmsFileIdentity;
    }

    public static String getAtsTokenUsername() {
        if (atsTokenUsername == null) {
            atsTokenUsername = System.getProperty("atsTokenUsername", constantsInit.atsTokenUsername());
        }

        return atsTokenUsername;
    }

    public static String getAtsTokenEmail() {
        if (atsTokenEmail == null) {
            atsTokenEmail = System.getProperty("atsTokenEmail", constantsInit.atsTokenEmail());
        }

        return atsTokenEmail;
    }

    public static String getAtsTokenIssuer() {
        if (atsTokenIssuer == null) {
            atsTokenIssuer = System.getProperty("atsTokenIssuer", constantsInit.atsTokenIssuer());
        }

        return atsTokenIssuer;
    }

    public static String getAtsTokenSubject() {
        if (atsTokenSubject == null) {
            atsTokenSubject = System.getProperty("atsTokenSubject", constantsInit.atsTokenSubject());
        }

        return atsTokenSubject;
    }

    public static String getAtsAuthApplication() {
        if (atsAuthApplication == null) {
            atsAuthApplication = System.getProperty("atsAuthApplication", constantsInit.atsAuthApplication());
        }

        return atsAuthApplication;
    }

    public static String getAtsAuthTargetCloudContext() {
        if (atsAuthTargetCloudContext == null) {
            atsAuthTargetCloudContext = System.getProperty("atsAuthTargetCloudContext", constantsInit.atsAuthTargetCloudContext());
        }

        return atsAuthTargetCloudContext;
    }

}