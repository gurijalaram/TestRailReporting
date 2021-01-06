package com.apriori.utils.constants;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class CommonConstants {

    public static final String DEFAULT_PROJECT_ID_VALUE = "177";
    public static final String DEFAULT_PROJECT_ID_KEY = "RUN_ID";

    public static final String DEFAULT_USER_NAME = "admin@apriori.com";
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_ACCESS_LEVEL = "admin";

    public static String RUN_ID = DEFAULT_PROJECT_ID_VALUE;

    private static String serviceHost;
    private static String servicePort;
    private static String secretKey;
    private static String atsServiceHost;
    private static String atsTokenIssuer;
    private static String atsTokenSubject;
    private static String atsAuthTargetCloudContext;
    private static String fmsServiceHost;
    private static String cisCustomerIdentity;
    private static String cisServiceHost;
    private static String cisPartIdentity;
    private static String cisReportIdentity;
    private static String cisReportTypeIdentity;
    private static String cisBatchIdentity;
    private static String ntsTargetCloudContext;
    private static String ntsServiceHost;
    private static String ntsEmailContent;

    public static final String defaultServiceHostKey = "serverHost";
    public static final String defaultServicePortKey = "serverPort";
    public static final String defaultSecretKeyKey = "secretKey";

    private static String csvFile;
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

    static {

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("common.properties");

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Level consoleLogLevel = Level.parse(PROPERTIES.getProperty("console.log.level"));
    public static final String schemaBasePath = PROPERTIES.getProperty("schema.base.path");

    /**
     * Get true/false value of whether to use different user
     * @return string
     */
    public static String getUseDifferentUser() {
        return PROPERTIES.getProperty("different.users");
    }

    /**
     * Get csv file to use
     * @return string
     */
    public static String getCsvFile() {
        return csvFile = csvFile == null ? System.getProperty("csvFile", "common-users.csv") : System.getProperty("csvFile");
    }

    public static String getServiceHost() {
        if (serviceHost == null) {
            serviceHost = System.getProperty(defaultServiceHostKey, PROPERTIES.getProperty("service.host"));
        }

        return serviceHost;
    }

    public static String getServicePort() {
        if (servicePort == null) {
            servicePort = System.getProperty(defaultServicePortKey, PROPERTIES.getProperty("service.port"));
        }
        return servicePort;
    }

    public static String getSecretKey() {
        if (secretKey == null) {
            secretKey = System.getProperty(defaultSecretKeyKey, PROPERTIES.getProperty("secret.key"));
        }

        return secretKey;
    }

    public static String getAtsServiceHost() {
        if (atsServiceHost == null) {
            atsServiceHost = System.getProperty("atsServiceHost", PROPERTIES.getProperty("ats.service.host"));
        }

        return atsServiceHost;
    }

    public static String getFmsServiceHost() {
        if (fmsServiceHost == null) {
            fmsServiceHost = System.getProperty("fmsServiceHost", PROPERTIES.getProperty("fms.service.host"));
        }

        return fmsServiceHost;
    }

    public static String getAtsTokenIssuer() {
        if (atsTokenIssuer == null) {
            atsTokenIssuer = System.getProperty("atsTokenIssuer", PROPERTIES.getProperty("ats.token.issuer"));
        }

        return atsTokenIssuer;
    }

    public static String getAtsTokenSubject() {
        if (atsTokenSubject == null) {
            atsTokenSubject = System.getProperty("atsTokenSubject", PROPERTIES.getProperty("ats.token.subject"));
        }

        return atsTokenSubject;
    }

    public static String getAtsAuthTargetCloudContext() {
        if (atsAuthTargetCloudContext == null) {
            atsAuthTargetCloudContext = System.getProperty("atsAuthTargetCloudContext", PROPERTIES.getProperty("ats.auth.targetCloudContext"));
        }

        return atsAuthTargetCloudContext;
    }

    public static String getCisCustomerIdentity() {
        if (cisCustomerIdentity == null) {
            cisCustomerIdentity = System.getProperty("cisCustomerIdentity", PROPERTIES.getProperty("cis.customer.identity"));
        }

        return cisCustomerIdentity;
    }

    public static String getCisServiceHost() {
        if (cisServiceHost == null) {
            cisServiceHost = System.getProperty("cisServiceHost", PROPERTIES.getProperty("cis.service.host"));
        }

        return cisServiceHost;
    }

    public static String getCisPartIdentity() {
        if (cisPartIdentity == null) {
            cisPartIdentity = System.getProperty("cisPartIdentity", PROPERTIES.getProperty("cis.part.identity"));
        }

        return cisPartIdentity;
    }

    public static String getCisReportIdentity() {
        if (cisReportIdentity == null) {
            cisReportIdentity = System.getProperty("cisReportIdentity", PROPERTIES.getProperty("cis.report.identity"));
        }

        return cisReportIdentity;
    }


    public static void setCisReportIdentity(String identity) {
        cisReportIdentity = System.setProperty("cisReportIdentity", identity);

    }

    public static String getCisReportTypeIdentity() {
        if (cisReportTypeIdentity == null) {
            cisReportTypeIdentity = System.getProperty("cisReportTypeIdentity", PROPERTIES.getProperty("cis.reportType.identity"));
        }

        return cisReportTypeIdentity;
    }

    public static void setCisBatchIdentity(String identity) {
        cisBatchIdentity = System.getProperty("cisBatchIdentity", identity);
    }

    public static String getCisBatchIdentity() {
        if (cisBatchIdentity == null) {
            cisBatchIdentity = System.getProperty("cisBatchIdentity", PROPERTIES.getProperty(".cis.batch.identity"));
        }

        return cisBatchIdentity;
    }

    public static String getNtsTargetCloudContext() {
        if (ntsTargetCloudContext == null) {
            ntsTargetCloudContext = System.getProperty("ntsTargetCloudContext", PROPERTIES.getProperty("nts.auth.targetCloudContext"));
        }

        return ntsTargetCloudContext;

    }

    public static String getNtsServiceHost() {
        if (ntsServiceHost == null) {
            ntsServiceHost = System.getProperty("ntsServiceHost", PROPERTIES.getProperty("nts.service.host"));
        }

        return ntsServiceHost;

    }

    public static String getNtsEmailContent() {
        if (ntsEmailContent == null) {
            ntsEmailContent = System.getProperty("ntsEmailContent", PROPERTIES.getProperty("nts.email.content"));
        }

        return ntsEmailContent;
    }
}