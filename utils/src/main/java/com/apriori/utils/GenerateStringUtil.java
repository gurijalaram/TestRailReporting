package com.apriori.utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
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
}