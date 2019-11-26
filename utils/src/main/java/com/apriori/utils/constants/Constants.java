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

    public static final String scenarioExportChapterUrl = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/2019_R2/startHelp.html#context/CIA_UG/Scen_Expt_chap#page/CI_ADMIN_USER_GUIDE/Scen_Expt_chap.html#wwpID0E0UY0HA";
    public static final String reportingDomainDesignerUrl = "https://cid-te.awsdev.apriori.com/jasperserver-pro/domaindesigner.html";
    public static final String reportsHomeUrl = "https://cid-te.awsdev.apriori.com/jasperserver-pro/";
    public static final String reportingHelpUrl = "http://help.jaspersoft.com/Default";
    public static final String privacyPolicyUrl = "https://www.apriori.com/privacy-policy";

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
    public static final String ciaURL = constantsInit.ciaURL();
    public static final String usersFile = constantsInit.usersCsvFileName();
    public static final Boolean useDifferentUsers = constantsInit.useDifferentUsers();

    public static String getBuildMode() {
        if (buildMode == null) {
            buildMode = System.getProperty(defaultBuildModeKey, defaultBuildModeValue);
        }

        return buildMode;
    }

}