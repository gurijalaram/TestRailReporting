package com.apriori.cis.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cis";
    private static final File INPUT_STREAM;
    private static final Properties PROPERTIES = new Properties();
    public static String environment;
    private static String costingThreads;
    private static String pollingTimeout;
    private static String cisServiceHost;
    private static String cisPartIdentity;
    private static String cisReportIdentity;
    private static String cisReportTypeIdentity;
    private static String cisBatchIdentity;
    private static String secretKey;
    private static String baseUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getDefaultUrl() {
        baseUrl = System.getProperty(DEFAULT_BASE_URL_KEY) == null ? PROPERTIES.getProperty("url.default") : System.getProperty(DEFAULT_BASE_URL_KEY);
        System.setProperty("baseUrl", baseUrl);

        return baseUrl.concat(PROPERTIES.getProperty("url.additional"));
    }

    /**
     * Get costing thread
     *
     * @return integer
     */
    public static Integer getCostingThreads() {
        costingThreads = costingThreads == null ? PROPERTIES.getProperty("costing.threads") : System.getProperty("costingThreads");

        return Integer.valueOf(costingThreads);
    }

    /**
     * Set costing thread
     */
    public static void setCostingThreads(Integer costingThreads) {
        Constants.costingThreads = costingThreads.toString();
    }

    /**
     * Get polling timeout
     *
     * @return integer
     */
    public static Integer getPollingTimeout() {
        pollingTimeout = pollingTimeout == null ? PROPERTIES.getProperty("polling.timeout") : System.getProperty("pollingTimeout");
        return Integer.valueOf(pollingTimeout);
    }

    /**
     * Set polling timeout
     */
    public static void setPollingTimeout(Integer pollingTimeout) {
        Constants.pollingTimeout = pollingTimeout.toString();
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        secretKey = secretKey == null ? PROPERTIES.getProperty("secretKey") : System.getProperty("secret.key");
        return secretKey;
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getCisServiceHost() {
        cisServiceHost = cisServiceHost == null ? PROPERTIES.getProperty("cisServiceHost") : System.getProperty("cis.service.host");
        return secretKey;
    }

    /**
     * Get batch identity
     *
     * @return string
     */
    public static String getCisBatchIdentity() {
        cisBatchIdentity = cisBatchIdentity == null ? PROPERTIES.getProperty("cis.batch.identity") : System.getProperty("cisBatchIdentity");
        return secretKey;
    }
}

