package com.apriori.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Util {

    static final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * Returns a formatted date and time string
     *
     * @return
     */
    public static String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    /**
     * Extracts current date to be used in various queries
     *
     * @return
     */
    public static String now() {

        String now = "dd-HH-mm-ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(now);
        return (sdf.format(cal.getTime()) + UUID.randomUUID().toString().replace("-", "").substring(0, 5)).toUpperCase();

    }

    /**
     * highlights given element. This would be mainly used for debugging
     *
     * @param driver
     * @param element
     */
    public static void highlightElement(WebDriver driver, WebElement element) {

        // Original in Python: https://gist.github.com/3086536
        String originalStyle = element.getAttribute("style");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Failed to highlight element");
        }
        js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", element);
    }

    /**
     * Shuffles the provided stream
     *
     * @return Stream
     */
    public static <T> Collector<T, ?, Stream<T>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        });
    }


    public static Map<String, String> getTokenAuthorizationForm(final String token) {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("apriori.tenantGroup", "default");
                put("apriori.tenant", "default");
            }};
    }

    public static Map<String, String> getDefaultAuthorizationForm(final String username, final String password) {
        return new HashMap<String, String>() {{
                put("grant_type", "password");
                put("client_id", "apriori-web-cost");
                put("client_secret", "donotusethiskey");
                put("scope", "tenantGroup%3Ddefault%20tenant%3Ddefault");
                put("username", username);
                put("password", password);
            }};
    }

    /**
     * Get file from resource folder current module.
     *
     * @param resourceFileName
     * @return
     */
    public static File getLocalResourceFile(String resourceFileName) {
        try {
            return new File(
                    URLDecoder.decode(
                            ClassLoader.getSystemResource(resourceFileName).getFile(),
                            "UTF-8"
                    )
            );
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    public static File getResourceAsFile(String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceFileName);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            logger.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get resource file stream from a jar file. {getResource}
     *
     * @param resourceFileName
     * @return
     */
    public static InputStream getResourceFileStream(String resourceFileName) {
        return ClassLoader.getSystemResourceAsStream(resourceFileName);
    }


    /**
     * Generates the scenario name and adds random number and nano time
     *
     * @return string
     */
    public String getScenarioName() {
        return "AutoScenario" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }

    /**
     * Generates the comparison name and adds random number and nano time
     *
     * @return string
     */
    public String getComparisonName() {
        return "AutoComparison" + new Random().nextInt(1000) + "-" + System.nanoTime();
    }
}