package main.java.utils;

import main.java.constants.Constants;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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
            logger.debug("InterruptedException", e);
        }
        js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", element);
    }

    /**
     * Shuffles the provided stream
     * @return Stream
     */
    public static <T> Collector<T, ?, Stream<T>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        });
    }

    //TODO z: just to contain all process with default authorization form in one place and make it comfortable to use
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
}