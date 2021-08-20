package com.apriori.utils;

import io.qameta.allure.Attachment;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class GenerateStringUtil {

    private static final Logger logger = LoggerFactory.getLogger(GenerateStringUtil.class);

    /**
     * Generates the scenario name and adds random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateScenarioName() {
        return "AutoScenario" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates the comparison name and adds random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateComparisonName() {
        return "AutoComparison" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates CIC workflow name and adds random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateWorkflowName() {
        return "AutoWorkflow" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates a fake email address and adds random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateEmail() {
        return "fakeuser" + new Random().nextInt(1000) + "@apriori.com";
    }

    /**
     * Generates a customer name for API
     *
     * @return string
     */
    @Attachment
    public String generateCustomerName() {
        return "AutoCustomer" + RandomStringUtils.randomAlphabetic(6);
    }

    /**
     * Generates a salesforceID for API
     *
     * @return string
     */
    @Attachment
    public String generateSalesForceId() {
        return "AutoSFID" + RandomStringUtils.randomNumeric(10);
    }

    /**
     * Generates a cloudref for API
     *
     * @return string
     */
    @Attachment
    public String generateCloudReference() {
        return "AutoCloudRef" + RandomStringUtils.randomAlphabetic(4);
    }

    /**
     * Generates a userName for API
     *
     * @return string
     */
    @Attachment
    public String generateUserName() {
        return "AutoUser" + RandomStringUtils.randomAlphabetic(5);
    }

    /**
     * Generates a siteName for API
     *
     * @return string
     */
    @Attachment
    public String generateSiteName() {
        return "AutoSite" + RandomStringUtils.randomAlphabetic(5);
    }

    /**
     * Generates a siteID for API
     *
     * @return string
     */
    @Attachment
    public String generateSiteID() {
        return "Auto" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generates a realmkey for API
     *
     * @return string
     */
    @Attachment
    public String generateRealmKey() {
        return "AutoRealmKey" + RandomStringUtils.randomNumeric(26);
    }

    /**
     * Generates a filter name
     *
     * @return string
     */
    @Attachment
    public String generateFilterName() {
        return "AutoFilter" + RandomStringUtils.randomAlphabetic(6);
    }

    /**
     * Generates a notes
     *
     * @return string
     */
    @Attachment
    public String generateNotes() {
        return "AutoNotes" + RandomStringUtils.randomAlphabetic(5);
    }

    /**
     * Generates a 12 character alpanumeric string
     *
     * @return string
     */
    public String getRandomString() {
        return RandomStringUtils.randomAlphanumeric(12);
    }
}