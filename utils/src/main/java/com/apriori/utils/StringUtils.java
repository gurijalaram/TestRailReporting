package com.apriori.utils;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

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
}
