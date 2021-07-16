package com.apriori.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    static final Logger logger = LoggerFactory.getLogger(Util.class);

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


    /**
     * run any javascript query
     *
     * @return String
     */
    public static String runAnyJavaScript(WebDriver driver, String scriptToBeExecuted) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        return String.valueOf(js.executeScript(scriptToBeExecuted));
    }
}
