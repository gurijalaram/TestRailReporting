package com.apriori.utils.constants;

import com.apriori.utils.enums.ProcessGroupEnum;

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
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cid-qa";

    public static final String DEFAULT_SCENARIO_NAME_KEY = "scenarioName";
    public static final String DEFAULT_EXPORT_SET_NAME_KEY = "exportSetName";
    public static final String DEFAULT_ELEMENT_NAME_KEY = "elementName";
    public static final String DEFAULT_SCENARIO_TYPE_KEY = "scenarioType";

    public static final String DEFAULT_USER_NAME_KEY = "username";
    public static final String DEFAULT_PASSWORD_KEY = "password";

    public static final String SCENARIO_NAME = System.getProperty(DEFAULT_SCENARIO_NAME_KEY);
    public static final String EXPORT_SET_NAME = System.getProperty(DEFAULT_EXPORT_SET_NAME_KEY);
    public static final String ELEMENT_NAME = System.getProperty(DEFAULT_ELEMENT_NAME_KEY);
    public static final String SCENARIO_TYPE = System.getProperty(DEFAULT_SCENARIO_TYPE_KEY);

    public static final String PROP_USER_NAME = System.getProperty(DEFAULT_USER_NAME_KEY);
    public static final String PROP_USER_PASSWORD = System.getProperty(DEFAULT_PASSWORD_KEY);

    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_ONE = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_TWO = "CIA_UG";
    public static final String CIA_USER_GUIDE_URL_SUBSTRING = "CI_ADMIN_USER_GUIDE";
    public static final String CIA_USER_GUIDE_TITLE = "Cost Insight Admin\nUser Guide";
    public static final String SCENARIO_EXPORT_CHAPTER_PAGE_TITLE = "2 Scenario and System Data Exports";
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String SAVED_CONFIG_NAME = "Saved Config";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTS_LAST_SUFFIX = "flow.html?_flowId=homeFlow";
    public static final String REPORTING_HELP_URL = "https://help.jaspersoft.com/Default";
    public static final String PRIVACY_POLICY_URL = "https://www.apriori.com/privacy-policy";
    public static final String PISTON_ASSEMBLY_CID_NAME = "PISTON_ASSEMBLY";
    public static final String DTC_METRICS_FOLDER = "DTC Metrics";
    public static final String GENERAL_FOLDER = "General";
    public static final String PUBLIC_WORKSPACE = "Public";
    public static final String PRIVATE_WORKSPACE = "Private";
    public static final String ASSEMBLY_STRING = "[assembly]";
    public static final String PART_NAME_INITIAL_EXPECTED_MACHINING_DTC = "PMI_SYMMETRYCREO (Initial) ";
    public static final String PART_NAME_EXPECTED_MACHINING_DTC = "PMI_FLATNESSCREO (Initial)";
    public static final String CASTING_DIE_SAND_NAME = String.format(
            "%s, %s",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup(),
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
    );
    public static final String STOCK_MACHINING_TWO_MODEL_NAME = String.format(
            "%s, %s",
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(),
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
    );

    public static final String CID_AUT_HEADER_TEXT = "CI Design AUTOMATION";
    public static final String CID_QA_HEADER_TEXT = "CI Design QUAIL";

    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";
    public static final String HORIZONTAL_SCROLL = "horizontal_scroll";

    public static final String DEFAULT_SCENARIO_NAME = "Initial";
    public static final String PART_SCENARIO_TYPE = "Part";
    public static final String ASSEMBLY_SCENARIO_TYPE = "Assembly";
    public static final String COMPARISON_SCENARIO_TYPE = "Comparison";

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
    private static String cisCustomerIdentity;
    private static String cisServiceHost;
    private static String cisPartIdentity;
    private static String cisReportIdentity;
    private static String cisReportTypeIdentity;
    private static String cisBatchIdentity;
    private static String apitestsBasePath;
    private static String apitestsResourcePath;
    private static String ntsTargetCloudContext;
    private static String ntsServiceHost;
    private static String ntsEmailRecipientAddress;
    private static String ntsEmailSubject;
    private static String ntsEmailContent;
    private static String ntsEmailAttachment;

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
    public static final String cicURL = constantsInit.cicURL();
    public static final String cidAppURL = getBaseUrl();
    public static final String headerText = constantsInit.logoutHeaderText();
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

    public static String getCisCustomerIdentity() {
        if (cisCustomerIdentity == null) {
            cisCustomerIdentity = System.getProperty("cisCustomerIdentity", constantsInit.cisCustomerIdentity());
        }

        return cisCustomerIdentity;
    }

    public static String getCisServiceHost() {
        if (cisServiceHost == null) {
            cisServiceHost = System.getProperty("cisServiceHost", constantsInit.cisServiceHost());
        }

        return cisServiceHost;
    }

    public static String getCisPartIdentity() {
        if (cisPartIdentity == null) {
            cisPartIdentity = System.getProperty("cisPartIdentity", constantsInit.cisPartIdentity());
        }
        
        return cisPartIdentity;
    }


    public static void setCisPartIdentity(String identity) {
        cisPartIdentity = System.setProperty("cisPartIdentity", identity);
    }

    public static String getCisReportIdentity() {
        if (cisReportIdentity == null) {
            cisReportIdentity = System.getProperty("cisReportIdentity", constantsInit.cisReportIdentity());
        }

        return cisReportIdentity;
    }


    public static void setCisReportIdentity(String identity) {
        cisReportIdentity = System.setProperty("cisReportIdentity", identity);

    }

    public static String getCisReportTypeIdentity() {
        if (cisReportTypeIdentity == null) {
            cisReportTypeIdentity = System.getProperty("cisReportTypeIdentity", constantsInit.cisReportTypeIdentity());
        }

        return cisReportTypeIdentity;
    }

    public static void setCisBatchIdentity(String identity) {
        cisBatchIdentity = System.getProperty("cisBatchIdentity", identity);
    }

    public static String getCisBatchIdentity() {
        if (cisBatchIdentity == null) {
            cisBatchIdentity = System.getProperty("cisBatchIdentity", constantsInit.cisBatchIdentity());
        }

        return cisBatchIdentity;
    }

    public static String getApitestsBasePath() {
        if (apitestsBasePath == null) {
            apitestsBasePath = System.getProperty("apitestsBasePath", constantsInit.apitestsBasePath());
        }

        return apitestsBasePath;

    }
    
    public static String getApitestsResourcePath() {
        if (apitestsResourcePath == null) {
            apitestsResourcePath = System.getProperty("apitestsResourcePath", constantsInit.apitestsResourcePath());
        }

        return apitestsResourcePath;

    }

    public static String getNtsTargetCloudContext() {
        if (ntsTargetCloudContext == null) {
            ntsTargetCloudContext = System.getProperty("ntsTargetCloudContext", constantsInit.ntsTargetCloudContext());
        }

        return ntsTargetCloudContext;

    }

    public static String getNtsServiceHost() {
        if (ntsServiceHost == null) {
            ntsServiceHost = System.getProperty("ntsServiceHost", constantsInit.ntsServiceHost());
        }

        return ntsServiceHost;

    }

    public static String getNtsEmailRecipientAddress() {
        if (ntsEmailRecipientAddress == null) {
            ntsEmailRecipientAddress = System.getProperty("ntsEmailRecipientAddress",
                    constantsInit.ntsEmailRecipientAddress());
        }

        return ntsEmailRecipientAddress;

    }
    
    public static String getNtsEmailSubject() {
        if (ntsEmailSubject == null) {
            ntsEmailSubject = System.getProperty("ntsEmailSubject", constantsInit.ntsEmailSubject());
        }
        
        return ntsEmailSubject;
    }

    public static String getNtsEmailContent() {
        if (ntsEmailContent == null) {
            ntsEmailContent = System.getProperty("ntsEmailContent", constantsInit.ntsEmailContent());
        }

        return ntsEmailContent;
    }

    public static String getNtsEmailAttachment() {
        if (ntsEmailAttachment == null) {
            ntsEmailAttachment = System.getProperty("ntsEmailAttachment", constantsInit.ntsEmailAttachment());
        }

        return ntsEmailAttachment;
    }
    
}