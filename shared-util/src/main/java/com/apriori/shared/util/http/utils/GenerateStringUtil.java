package com.apriori.shared.util.http.utils;

import io.qameta.allure.Attachment;
import org.apache.commons.lang3.RandomStringUtils;

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
     * Generates a text with mark that the text was auto generated
     *
     * @param dataMarker - data marker
     * @return string
     */
    @Attachment
    public String generateStringForAutomation(String dataMarker) {
        return String.format("AutoGenerated-%s-%s", dataMarker, new Random().nextInt(1000) + "-" + System.nanoTime());
    }

    /**
     * Generates the component name by adding random number and nano time
     *
     * @return string
     */
    @Attachment
    public String generateComponentName(String componentName) {
        final int index = componentName.indexOf(".");
        final String randomSymbols = RandomStringUtils.randomNumeric(12);

        if (index != -1) {
            return componentName.substring(0, index)
                + "_"
                + randomSymbols
                + componentName.substring(index);
        }

        return componentName + "_" + randomSymbols;
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
     * Generates an alphabetic string for API
     *
     * @param marker - data marker
     * @param length - length of string
     * @return string
     */
    @Attachment
    public String generateAlphabeticString(String marker, int length) {
        return String.format("Auto%s%s", marker, RandomStringUtils.randomAlphabetic(length));
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