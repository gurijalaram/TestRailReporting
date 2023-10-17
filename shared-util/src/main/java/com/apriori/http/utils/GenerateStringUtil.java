package com.apriori.http.utils;

import io.qameta.allure.Attachment;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class GenerateStringUtil {

    /**
     * Generate a serial date string and concatenate to the supplied string.
     * This assures the returned string is unique.
     *
     * @param str The base string
     * @return string with concatenated serial date string
     */
    public static String saltString(String str) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        return str.concat("_" + strDate + "_");
    }

    /**
     * Generate a uuid and concatenate to the supplied string.
     * This assures the returned string is unique.
     *
     * @param str The base string (String must be no longer than 27 characters)
     * @return string with concatenated serial date string
     */
    public static String saltStringUUID(String str) {
        return str.concat(UUID.randomUUID().toString());
    }

    /**
     * Generate a random string with special characters
     *
     * @param max               the maximum number of characters
     * @param specialCharacters an array of special characters to concatenate to the string
     * @return random string
     */
    public static String generateString(int max, char[] specialCharacters) {
        String str = generateString(max);
        for (char c : specialCharacters) {
            str = str.concat(Character.toString(c));
        }

        return str;
    }

    /**
     * Converts the first letter of a string to upper case, the rest of the string is lower case
     * (eg. "FOO" to "Foo")
     *
     * @param string
     * @return
     */
    public static String getFirstLetterUpperCase(String string) {
        StringBuilder firstLetterUpperCase = new StringBuilder();
        firstLetterUpperCase.append(string, 0, 1);
        firstLetterUpperCase.append(string.substring(1).toLowerCase());
        return firstLetterUpperCase.toString();
    }

    /**
     * Checks if a scrollbar exists for the provided element
     *
     * @param element Weblement tp check if it is scrollable
     * @return True if scrollbar exists
     */
    public static boolean scrollBarExists(WebElement element) {
        int scrollHeight = Integer.parseInt(element.getAttribute("scrollHeight"));
        int offsetHeight = Integer.parseInt(element.getAttribute("offsetHeight"));
        return scrollHeight > offsetHeight;
    }

    /**
     * Generate a random string
     *
     * @param max the maximum number of characters
     * @return random string
     */
    public static String generateString(int max) {
        Random random = new Random();
        return random.ints(97, 123)
            .limit(max)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

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
     * Generates the component name by adding random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateComponentName(String componentName) {
        final int index = componentName.indexOf(".");
        final String randomSymbols = new Random().nextInt(1000) + "-" + System.nanoTime();

        if (index != -1) {
            return componentName.substring(0, index)
                + randomSymbols
                + componentName.substring(index);
        }

        return componentName + randomSymbols;
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
        return "autocloudref" + RandomStringUtils.randomAlphabetic(4).toLowerCase();
    }

    /**
     * Generates a userName for API
     *
     * @return string
     */
    @Attachment
    public String generateUserName() {
        return "AutoUser" + RandomStringUtils.randomAlphabetic(5) + System.nanoTime();
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
     * Generates a deploymentName for API
     *
     * @return string
     */
    @Attachment
    public String generateDeploymentName() {
        return "AutoDeployment" + RandomStringUtils.randomAlphabetic(3);
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

    /**
     * Generates a 12 character password string
     *
     * @return string
     */
    public String getRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10) + RandomStringUtils.random(2, "!#$%&()");
    }

    /**
     * Generates a specific length character alpanumeric string
     *
     * @return string
     */
    public String getRandomStringSpecLength(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Generates a specific length character numeric without zero
     *
     * @return string
     */
    public String getRandomNumbersStartsNoZero() {
        return RandomStringUtils.random(4, "123456789");
    }

    /**
     * Generates a 12 character numeric string
     *
     * @return String
     */
    public String getRandomNumbers() {
        return RandomStringUtils.randomNumeric(8);
    }

    /**
     * Generates a specific length character numeric string
     *
     * @return String
     */
    public String getRandomNumbersSpecLength(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * Generates a 102 character hash password string
     *
     * @return String
     */
    @Attachment
    public String getHashPassword() {
        return RandomStringUtils.random(102, "0123456789abcdefghijklmnopqrstuvwxyz:");
    }

    /**
     * Generates a random double with 2 decimals
     *
     * @return Double
     */
    public Double getRandomDoubleWithTwoDecimals() {
        return Double.valueOf(String.format("%.2f", 1 + (new Random().nextDouble() * (999 - 1))));
    }
}