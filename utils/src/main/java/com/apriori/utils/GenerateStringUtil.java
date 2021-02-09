package com.apriori.utils;

import io.qameta.allure.Attachment;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GenerateStringUtil {

    static final Logger LOGGER = LoggerFactory.getLogger(GenerateStringUtil.class);

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
    public String generateComparisonName() {
        return "AutoComparison" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates CIC workflow name and adds random number and nano time
     *
     * @return string
     */
    public String generateWorkflowName() {
        return "AutoWorkflow" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates a fake email address and adds random number and nano time
     *
     * @return string
     */
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
        return "AutoSFID" + (int)(Math.random() * 10000000000L);
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
}